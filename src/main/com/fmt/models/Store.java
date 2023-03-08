package com.fmt.models;

import com.fmt.Database;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Class to model a store.
 */
public class Store {
    private final String storeCode;
    private final Person manager;
    private final Address address;
    private final ArrayList<Invoice> sales;

    /**
     * Constructs a new store.
     *
     * @param storeCode The unique alphanumeric code that identifies this specific store.
     * @param manager The person object who manager this store.
     * @param address The address this store is located at.
     */
    public Store(String storeCode, Person manager, Address address, ArrayList<Invoice> sales) {
        this.storeCode = storeCode;
        this.manager = manager;
        this.address = address;
        this.sales = sales;
    }

    public Double getTotalSales() {
        return sales.stream()
                .mapToDouble((invoice) -> invoice.getNetCost() + invoice.getTotalTax())
                .sum();
    }

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


    /**
     * This method constructs a new store object from a string of CSV data.
     * In the CSV the manager of the store is referenced by a code, so we need
     * to pass a reference to the database in order to relate the associated
     * manager to the store.
     *
     * @param csv The CSV string containing the stores information.
     * @param db A reference to the Database containing the list of all people.
     *           This is used to relate the stores manager code to an actual person.
     * @return A store object built from the CSV string, or null if the manager doesn't exist.
     */
    public static Store fromCSV(String csv, Database db) {
        String[] data = csv.split(",");
        Address addr = new Address(data[2], data[3], data[4], data[5], data[6]);
        Person manager = db.getPersonByCode(data[1]); // This can be null so we need to be careful

        if (manager == null) {
            System.out.println("Error: Manager personCode doesn't exist in database.");
            return null;
        }

        return new Store(data[0], manager, addr, db.getInvoicesByStore(data[0]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Objects.equals(storeCode, store.storeCode) && Objects.equals(manager, store.manager) && Objects.equals(address, store.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeCode, manager, address);
    }
}
