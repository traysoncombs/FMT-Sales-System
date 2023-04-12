package com.fmt.models;

import com.fmt.datastore.Datastore;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class to model a store.
 */
public class Store implements Persistable {
    private final String storeCode;
    private final Person manager;
    private final Address address;
    private final ArrayList<Invoice> sales;

    /**
     * Constructs a new store.
     *
     * @param storeCode The unique alphanumeric code that identifies this specific store.
     * @param manager   The person object who manages this store.
     * @param address   The address this store is located at.
     */
    public Store(String storeCode, Person manager, Address address, ArrayList<Invoice> sales) {
        this.storeCode = storeCode;
        this.manager = manager;
        this.address = address;
        this.sales = sales;
    }

    /**
     * This method constructs a new store object from a string of CSV data.
     * In the CSV the manager of the store is referenced by a code, so we need
     * to pass a reference to the database in order to relate the associated
     * manager to the store.
     *
     * @param csv The CSV string containing the store's information.
     * @param db  A reference to the Database containing the list of all people.
     *            This is used to relate the stores manager code to an actual person.
     * @return A store object built from the CSV string, or null if the manager doesn't exist.
     */
    public static Store fromCSV(String csv, Datastore db) {
        String[] data = csv.split(",");
        Address addr = Address.fromCSV(csv, 2);
        Person manager = db.getPersonByCode(data[1]); // This can be null so we need to be careful

        if (manager == null) {
            return null;
        }

        return new Store(data[0], manager, addr, db.getInvoicesByStore(data[0]));
    }

    /**
     * Takes a result set containing the fields:
     * `store_code`, `manager_code`, `street`, `city`, `zip_code`, `state`, `country`
     * and returns a store. The Datastore should contain sales and people.
     *
     * @param rs Result set containing necessary fields
     * @param ds Reference to data store containing sales and people.
     * @return Store or null if manager_code isn't associated with a person.
     * @throws SQLException if ResultSet doesn't contain necessary fields.
     */
    public static Store fromRow(ResultSet rs, Datastore ds) throws SQLException {
        Person manager = ds.getPersonByCode(rs.getString("manager_code"));

        if (manager == null) return null;

        Address address = Address.fromRow(rs);

        return new Store(
                rs.getString("store_code"),
                manager,
                address,
                ds.getInvoicesByStore(rs.getString("store_code"))
        );
    }

    /**
     * Calculates gross sales for this store.
     *
     * @return Total gross sales for this store.
     */
    public Double getTotalSales() {
        return sales.stream()
                .mapToDouble(Invoice::getGrossCost)
                .sum();
    }

    /**
     * Generates a summary report for this store.
     *
     * @return The stores summary report.
     */
    public String generateSummary() {
        return String.format(
                "%-10s %-30s %-10s $ %11.2f\n",
                storeCode,
                manager.getLastName() + "," + manager.getFirstName(),
                sales.size(),
                getTotalSales()
        );
    }

    public Integer getNumSales() {
        return sales.size();
    }

    public Person getManager() {
        return manager;
    }

    public String getStoreCode() {
        return storeCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Objects.equals(storeCode, store.storeCode) &&
                Objects.equals(manager, store.manager) &&
                Objects.equals(address, store.address) &&
                Objects.equals(sales, store.sales);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeCode, manager, address);
    }

    @Override
    public boolean saveToDB() {
        return false;
    }
}
