package com.fmt.models;

import com.fmt.datastore.Datastore;
import com.fmt.models.invoiceitems.InvoiceItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;


/**
 * A class to model an invoice.
 */
public class Invoice {
    private final String invoiceCode;
    private final String storeCode;
    private final Person customer;

    public Person getSalesPerson() {
        return salesPerson;
    }

    private final Person salesPerson;
    private final LocalDate invoiceDate;
    private final ArrayList<InvoiceItem<?>> invoiceItems;

    /**
     * Constructs an invoice. Mostly used internally by fromCSV.
     *
     * @param invoiceCode  The code of the invoice
     * @param storeCode    Code of the associated store.
     * @param customer     Person object associated with customer.
     * @param salesPerson  Person object associated with sales person.
     * @param invoiceDate  Date associated.
     * @param invoiceItems All items that were sold on this invoice.
     */
    public Invoice(
            String invoiceCode,
            String storeCode,
            Person customer,
            Person salesPerson,
            LocalDate invoiceDate,
            ArrayList<InvoiceItem<?>> invoiceItems
    ) {
        this.invoiceCode = invoiceCode;
        this.storeCode = storeCode;
        this.customer = customer;
        this.salesPerson = salesPerson;
        this.invoiceDate = invoiceDate;
        this.invoiceItems = invoiceItems;
    }

    /**
     * Factory method to build an invoice given a CSV string. The Database
     * that is passed should contain all the items referenced by this invoice.
     *
     * @param csv Invoice in CSV format.
     * @param ds  Reference to the database where invoice items are stored.
     * @return An instance of Invoice.
     */
    public static Invoice fromCSV(String csv, Datastore ds) {
        String[] data = csv.split(",");
        Person customer = ds.getPersonByCode(data[2]);
        Person salesPerson = ds.getPersonByCode(data[3]);


        if (customer == null || salesPerson == null) return null;

        return new Invoice(
                data[0],
                data[1],
                customer,
                salesPerson,
                LocalDate.parse(data[4]),
                ds.getInvoiceItemsByCode(data[0])
        );
    }

    /**
     * Takes a ResultSet containing the fields:
     * `invoice_code`, `store_code`, `customer_code`,
     * `salesperson_code`, `invoice_date`.
     *
     * @param rs ResultSet containing necessary fields
     * @param ds Datastore containing relevant people and invoice items.
     * @return Invoice or null if customer or salesperson codes don't exist.
     * @throws SQLException if ResultSet doesn't contain necessary fields.
     */
    public static Invoice fromRow(ResultSet rs, Datastore ds) throws SQLException {
        Person customer = ds.getPersonByCode(rs.getString("customer_code"));
        Person salesPerson = ds.getPersonByCode(rs.getString("salesperson_code"));
        String invoice_code = rs.getString("invoice_code");

        if (customer == null || salesPerson == null) return null;

        return new Invoice(
                invoice_code,
                rs.getString("store_code"),
                customer,
                salesPerson,
                LocalDate.parse(rs.getString("invoice_date")),
                ds.getInvoiceItemsByCode(invoice_code)
        );
    }

    /**
     * Generates a beautiful in depth report containing all information
     * relevant to the invoice.
     *
     * @return A string containing the report.
     */
    public String generateFullReport() {
        double subtotal = 0.0;
        Double tax = 0.0;

        StringBuilder out = new StringBuilder(String.format("Invoice: %8s\n", invoiceCode) +
                String.format("Store:     %1s\n", storeCode) +
                String.format("Date:%16s\n", invoiceDate.toString()) +
                String.format("Customer:  %s\n\n", customer.toString()) +
                String.format("Sales Person:\n           %s\n", salesPerson.toString()) +
                "Item                                                               Total\n" +
                "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                       =-=-=-=-=-=-=-\n");

        for (InvoiceItem<?> item : invoiceItems) {
            double cost = item.getNetCost();
            subtotal += cost;
            tax += item.getTax();
            out.append(item.generateReport());
            out.append(String.format("                                                         $ %13.2f\n", cost));
        }

        out.append("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                      -=-=-=-=-=-=-=-\n");
        out.append(String.format("                                               Subtotal: $ %13.2f\n", subtotal));
        out.append(String.format("                                               Tax:      $ %13.2f\n", tax));
        out.append(String.format("                                               Total:    $ %13.2f\n", subtotal + tax));
        return out.toString();
    }

    /**
     * Generates a simpler report containing less information
     * than the full report.
     *
     * @return A summary report.
     */
    public String generateSummaryReport() {
        return String.format(
                "%-10s %-10s %-30s %-10s $ %9.2f $ %12.2f\n",
                invoiceCode,
                storeCode,
                customer.getLastName() + "," + customer.getFirstName(),
                invoiceItems.size(),
                getTotalTax(),
                getGrossCost()
        );
    }

    /**
     * Calculated the total cost of all the invoice
     * items excluding tax.
     *
     * @return Net cost
     */
    public Double getNetCost() {
        return invoiceItems.stream()
                .mapToDouble(InvoiceItem::getNetCost)
                .sum();
    }

    /**
     * Calculates the total tax of all
     * the invoice items
     *
     * @return Total tax
     */
    public double getTotalTax() {
        return invoiceItems.stream()
                .mapToDouble(InvoiceItem::getTax)
                .sum();
    }

    public double getGrossCost() {
        return getNetCost() + getTotalTax();
    }

    public Integer getTotalItems() {
        return invoiceItems.size();
    }

    public String getStoreCode() {
        return storeCode;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public Person getCustomer() {
        return customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(invoiceCode, invoice.invoiceCode) && Objects.equals(storeCode, invoice.storeCode) && Objects.equals(customer, invoice.customer) && Objects.equals(salesPerson, invoice.salesPerson) && Objects.equals(invoiceDate, invoice.invoiceDate) && Objects.equals(invoiceItems, invoice.invoiceItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceCode, storeCode, customer, salesPerson, invoiceDate, invoiceItems);
    }
}
