package com.fmt.models;

import com.fmt.Database;
import com.fmt.models.invoiceitems.InvoiceItem;
import com.fmt.models.items.Item;

import java.time.LocalDate;
import java.util.ArrayList;


public class Invoice {
    private final String invoiceCode;
    private final String storeCode;
    private final Person customer;
    private final Person salesPerson;
    private final LocalDate invoiceDate;

    private final ArrayList<InvoiceItem<?>> invoiceItems;

    public Invoice(String invoiceCode, String storeCode, Person customer, Person salesPerson, LocalDate invoiceDate, ArrayList<InvoiceItem<?>> invoiceItems) {
        this.invoiceCode = invoiceCode;
        this.storeCode = storeCode;
        this.customer = customer;
        this.salesPerson = salesPerson;
        this.invoiceDate = invoiceDate;
        this.invoiceItems = invoiceItems;
    }

    public static Invoice fromCSV(String csv, Database db) {
        String[] data = csv.split(",");
        return new Invoice(
                data[0],
                data[1],
                db.getPersonByCode(data[2]),
                db.getPersonByCode(data[3]),
                LocalDate.parse(data[4]),
                db.getInvoiceItemsByCode(data[0])
        );
    }

    public Double getNetCost(){
        return invoiceItems.stream().mapToDouble(InvoiceItem::getNetCost).sum();
    }

    public double getTotalTax() {
        return invoiceItems.stream().mapToDouble(InvoiceItem::getTax).sum();
    }

    public double getGrossCost() {
        return getNetCost() + getTotalTax();
    }

    public Integer getTotalItems() {
        return invoiceItems.size();
    }

    public String generateReport() {
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
            out.append(item);
            out.append(String.format("                                                         $ %13.2f\n", cost));
        }

        out.append("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                      -=-=-=-=-=-=-=-\n");
        out.append(String.format("                                               Subtotal: $ %13.2f\n", subtotal));
        out.append(String.format("                                               Tax:      $ %13.2f\n", tax));
        out.append(String.format("                                               Total:    $ %13.2f\n", subtotal + tax));
        return out.toString();
    }

    public String generateSummary() {
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

    public String getStoreCode() {
        return storeCode;
    }

}
