package com.fmt.datastore;

import com.fmt.models.Invoice;
import com.fmt.models.Person;
import com.fmt.models.Store;
import com.fmt.models.invoiceitems.InvoiceItem;
import com.fmt.models.items.Item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;


/**
 * Class to hold lists of all the relevant models and facilitates the
 * relating and importing of the necessary data.
 */
public class LegacyDatastore implements Datastore {
    private final ArrayList<Person> people = new ArrayList<>();
    private final ArrayList<Store> stores = new ArrayList<>();
    private final ArrayList<Item> items = new ArrayList<>();
    private final ArrayList<Invoice> invoices = new ArrayList<>();
    private final ArrayList<InvoiceItem<?>> invoiceItems = new ArrayList<>();


    /**
     * Constructs a Database given the files to each dataset, as well as
     * the type of data being imported.
     *
     * @param peopleFile Path to the file containing people.
     * @param storesFile Path to the file containing stores.
     * @param itemsFile  Path to the file containing invoice items.
     */
    public LegacyDatastore(String peopleFile, String storesFile, String itemsFile, String invoiceFile, String invoiceItemsFile) {
        // Must import people before stores in order to relate the managers
        this.importFromCSV(peopleFile, FieldType.PEOPLE);
        this.importFromCSV(itemsFile, FieldType.ITEMS);
        this.importFromCSV(invoiceItemsFile, FieldType.INVOICE_ITEMS);
        this.importFromCSV(invoiceFile, FieldType.INVOICE);
        this.importFromCSV(storesFile, FieldType.STORES);
    }

    /**
     * Imports a csv file with data containing type.
     *
     * @param csvFile Path to the CSV file that should be imported
     * @param type    Type of data being imported, can be people, stores, or items.
     */
    public void importFromCSV(String csvFile, FieldType type) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(csvFile));
            lines.remove(""); // Remove any empty lines
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error opening file.");
        }

        for (String line : lines.subList(1, lines.size())) {
            switch (type) {
                case PEOPLE:
                    people.add(Person.fromCSV(line));
                    break;
                case STORES:
                    Store store = Store.fromCSV(line, this);
                    if (store == null) {
                        Logger.getLogger("fmt").warning("Store had null field, excluding it from datastore");
                        break;
                    }
                    stores.add(store);
                    break;
                case ITEMS:
                    items.add(Item.fromCSV(line));
                    break;
                case INVOICE:
                    Invoice invoice = Invoice.fromCSV(line, this);
                    if (invoice == null) {
                        Logger.getLogger("fmt").warning("Invoice had null field, excluding it from datastore");
                        break;
                    }
                    invoices.add(invoice);
                    break;
                case INVOICE_ITEMS:
                    InvoiceItem<?> item = InvoiceItem.fromCSV(line, this);
                    if (item == null) {
                        Logger.getLogger("fmt").warning("Item had null field, excluding it from datastore");
                        continue;
                    }
                    invoiceItems.add(item);
                    break;
            }
        }
    }

    public Person getPersonByCode(String code) {
        for (Person p : this.people) {
            if (p.getPersonCode().equals(code)) {
                return p;
            }
        }
        return null;
    }

    public Item getItemByCode(String code) {
        for (Item i : this.items) {
            if (i.getItemCode().equals(code)) {
                return i;
            }
        }
        return null;
    }

    public ArrayList<InvoiceItem<?>> getInvoiceItemsByCode(String code) {
        ArrayList<InvoiceItem<?>> invoiceItemsWithCode = new ArrayList<>();
        for (InvoiceItem<?> i : this.invoiceItems) {
            if (i.getInvoiceCode().equals(code)) {
                invoiceItemsWithCode.add(i);
            }
        }
        return invoiceItemsWithCode;
    }

    public ArrayList<Invoice> getInvoicesByStore(String storeCode) {
        ArrayList<Invoice> invoicesWithStore = new ArrayList<>();
        for (Invoice i : this.invoices) {
            if (i.getStoreCode().equals(storeCode)) {
                invoicesWithStore.add(i);
            }
        }
        return invoicesWithStore;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<InvoiceItem<?>> getInvoiceItems() {
        return invoiceItems;
    }

    public ArrayList<Invoice> getInvoices() {
        return invoices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LegacyDatastore datastore = (LegacyDatastore) o;
        return Objects.equals(people, datastore.people) &&
                Objects.equals(stores, datastore.stores) &&
                Objects.equals(items, datastore.items) &&
                Objects.equals(invoices, datastore.invoices) &&
                Objects.equals(invoiceItems, datastore.invoiceItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(people, stores, items, invoices, invoiceItems);
    }

    public enum FieldType {
        PEOPLE,
        STORES,
        ITEMS,
        INVOICE_ITEMS,
        INVOICE,
    }
}