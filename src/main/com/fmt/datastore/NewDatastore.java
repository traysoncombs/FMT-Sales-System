package com.fmt.datastore;

import com.fmt.ConnectionFactory;
import com.fmt.models.Invoice;
import com.fmt.models.Person;
import com.fmt.models.Store;
import com.fmt.models.invoiceitems.InvoiceItem;
import com.fmt.models.items.Item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Class to hold all instances of models and facilitate the relation of different models.
 * This class is used to relate models to prevent the storing of redundant objects that would
 * occur if a new object was created within each class whenever a relation was necessary. This class
 * allows references to be used instead.
 */
public class NewDatastore implements Datastore {
    private final HashMap<String, Person> people = new HashMap<>();
    private final HashMap<String, Store> stores = new HashMap<>();
    private final HashMap<String, Item> items = new HashMap<>();
    private SortedList<Invoice> invoices;
    private final HashMap<String, ArrayList<InvoiceItem<?>>> invoiceItems = new HashMap<>();

    /**
     * Creates a new datastore and imports all necessary data from connectionFactory.
     *
     * @throws SQLException on database error
     * @param invoiceComparator How invoices should be sorted.
     */
    public NewDatastore(Comparator<Invoice> invoiceComparator) throws SQLException {
        this.invoices = new SortedList<>(invoiceComparator);
        this.importPeople();
        this.importItems();
        this.importInvoiceItems();
        this.importInvoices();
        this.importStores();
    }

    /**
     * Imports all people from the database.
     *
     * @throws SQLException on database error
     */
    private void importPeople() throws SQLException {
        String sql = "SELECT" +
                "    Person.code AS person_code," +
                "    last_name," +
                "    first_name," +
                "    Address.street," +
                "    Address.zip_code," +
                "    Address.city," +
                "    State.name AS state," +
                "    Country.name AS country," +
                "    GROUP_CONCAT(Email.email) as emails " +
                "FROM" +
                "    Person" +
                "        JOIN" +
                "    Address ON Person.address_id = Address.id" +
                "        JOIN" +
                "    State ON State.id = Address.state_id" +
                "        JOIN" +
                "    Country ON Country.id = Address.country_id" +
                "        LEFT JOIN" +
                "    Email ON Email.person_id = Person.id " +
                "GROUP BY Person.id;";

        PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql);
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            try {
                Person person = Person.fromRow(rs);
                this.people.put(person.getPersonCode(), person);
            } catch (SQLException e) {
                Logger.getLogger("fmt").severe("SQLException occurred while importing people.");
                e.printStackTrace();
            }
        }
        rs.close();
    }

    /**
     * Imports all items from the database.
     *
     * @throws SQLException on database error
     */
    private void importItems () throws SQLException {
        String sql = "SELECT" +
                "    code," +
                "    name," +
                "    model," +
                "    unit," +
                "    unit_price," +
                "    hourly_rate," +
                "    discriminator " +
                "FROM" +
                "    Item";

        PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql);
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            try {
                Item item = Item.fromRow(rs);
                // Again this shouldn't happen, but if it does
                // log the error and move along.
                if (item == null) {
                    Logger.getLogger("fmt").warning("Store had null field, excluding it from datastore");
                    continue;
                }
                this.items.put(item.getItemCode(), item);
            } catch (SQLException e) {
                Logger.getLogger("fmt").severe("SQLException occurred while importing items.");
                e.printStackTrace();
            }
        }
        rs.close();
    }

    /**
     * Imports all InvoiceItems from the database.
     *
     * @throws SQLException on database error
     */
    private void importInvoiceItems() throws SQLException {
        String sql = "SELECT" +
                "    Invoice.code AS invoice_code," +
                "    Item.code AS item_code," +
                "    quantity," +
                "    purchase_price," +
                "    hours_billed," +
                "    fee," +
                "    start_date," +
                "    end_date," +
                "    InvoiceItem.discriminator " +
                "FROM" +
                "    InvoiceItem" +
                "        JOIN" +
                "    Invoice ON Invoice.id = InvoiceItem.invoice_id" +
                "        JOIN" +
                "    Item ON Item.id = InvoiceItem.item_id;";

        PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql);
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            try {
                InvoiceItem<?> invoiceItem = InvoiceItem.fromRow(rs, this);
                if (invoiceItem == null) {
                    Logger.getLogger("fmt").warning("InvoiceItem had null field, excluding it from datastore");
                    continue;
                }
                // If an invoice doesn't have an entry in the hashmap yet then add it.
                if (!this.invoiceItems.containsKey(invoiceItem.getInvoiceCode())) {
                    this.invoiceItems.put(invoiceItem.getInvoiceCode(), new ArrayList<>());
                }
                this.invoiceItems.get(invoiceItem.getInvoiceCode()).add(invoiceItem);
            } catch (SQLException e) {
                Logger.getLogger("fmt").severe("SQLException occurred while importing invoices.");
                e.printStackTrace();
            }
        }
        rs.close();
    }

    /**
     * Imports all invoices from the database.
     *
     * @throws SQLException on database error
     */
    private void importInvoices() throws SQLException {
        String sql = "SELECT" +
                "    Invoice.code AS invoice_code," +
                "    Store.code AS store_code," +
                "    customer.code AS customer_code," +
                "    salesperson.code AS salesperson_code," +
                "    Invoice.invoice_date " +
                "FROM" +
                "    Invoice" +
                "        JOIN" +
                "    Store ON Store.id = Invoice.store_id" +
                "        JOIN" +
                "    Person customer ON customer.id = Invoice.customer_id" +
                "        JOIN" +
                "    Person salesperson ON salesperson.id = Invoice.salesperson_id;";

        PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql);
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            try {
                Invoice invoice = Invoice.fromRow(rs, this);
                if (invoice == null) {
                    Logger.getLogger("fmt").warning("Invoice had null field, excluding it from datastore");
                    continue;
                }
                this.invoices.add(invoice);
            } catch (SQLException e) {
                Logger.getLogger("fmt").severe("SQLException occurred while importing invoices.");
                e.printStackTrace();
            }
        }
        rs.close();
    }

    /**
     * Imports all stores from the database.
     * Requires all other data to be imported first.
     *
     * @throws SQLException on database error
     */
    private void importStores() throws SQLException {
        String sql = "SELECT" +
                "    Store.code AS store_code," +
                "    Person.code AS manager_code," +
                "    Address.street," +
                "    Address.city," +
                "    Address.zip_code," +
                "    State.name AS state," +
                "    Country.name AS country " +
                "FROM" +
                "    Store" +
                "        JOIN" +
                "    Person ON manager_id = Person.id" +
                "        JOIN" +
                "    Address ON Store.address_id = Address.id" +
                "        JOIN" +
                "    State ON State.id = Address.state_id" +
                "        JOIN" +
                "    Country ON Country.id = Address.country_id;";

        PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql);
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            try {
                Store store = Store.fromRow(rs, this);
                if (store == null) {
                    Logger.getLogger("fmt").warning("Store had null field, excluding it from datastore");
                    continue;
                }

                this.stores.put(store.getStoreCode(), store);
            } catch (SQLException e) {
                Logger.getLogger("fmt").severe("SQLException occurred while importing stores.");
                e.printStackTrace();
            }
        }
        rs.close();
    }

    public void changeInvoiceSorting(Comparator<Invoice> invoiceComparator) {
        this.invoices = SortedList.buildSortedList(this.invoices.toArray(), invoiceComparator);
    }

    @Override
    public Person getPersonByCode(String code) {
        return people.get(code);
    }

    @Override
    public Item getItemByCode(String code) {
        return items.get(code);
    }

    @Override
    public ArrayList<InvoiceItem<?>> getInvoiceItemsByCode(String code) {
        return invoiceItems.getOrDefault(code, new ArrayList<>());
    }

    @Override
    public ArrayList<Invoice> getInvoicesByStore(String storeCode) {
        return Arrays.stream(invoices.toArray())
                .filter(
                        (invoice) -> invoice.getStoreCode().equals(storeCode)
                ).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<Person> getPeople() {
        return new ArrayList<>(this.people.values());
    }

    @Override
    public ArrayList<Store> getStores() {
        return new ArrayList<>(this.stores.values());
    }

    @Override
    public ArrayList<Item> getItems() {
        return new ArrayList<>(this.items.values());
    }

    @Override
    public ArrayList<InvoiceItem<?>> getInvoiceItems() {
        ArrayList<InvoiceItem<?>> ret = new ArrayList<>();
        invoiceItems.values().forEach(ret::addAll);
        return ret;
    }

    @Override
    public ArrayList<Invoice> getInvoices() {
        return new ArrayList<>(Arrays.asList(this.invoices.toArray()));
    }
}
