package com.fmt;


import com.fmt.models.Invoice;
import com.fmt.models.Store;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * A class containing methods to generate reports based off a Database.
 */
public class ReportGenerator {
    private final Database db;

    public ReportGenerator(Database db) {
        this.db = db;
    }

    /**
     * Generates a summary report for each invoice stored in the database.
     *
     * @return A summary report for invoices
     */
    public String generateSummaryReport() {
        // Create a copy of invoices so the order of the array in Database isn't changed.
        ArrayList<Invoice> invoices = new ArrayList<>(db.getInvoices());
        double totalTax = 0.0;
        double totalCost = 0.0;
        int totalItems = 0;

        // Sort invoices by net cost in descending order
        invoices.sort(Comparator.comparingDouble(Invoice::getNetCost).reversed());

        StringBuilder out = new StringBuilder("+----------------------------------------------------------------------------------------+\n" +
                "| Summary Report - By Total                                                              |\n" +
                "+----------------------------------------------------------------------------------------+\n" +
                "Invoice #  Store      Customer                       # Items            Tax          Total\n");

        // Generate each invoices summary
        for (Invoice invoice : invoices) {
            totalTax += invoice.getTotalTax();
            totalCost += invoice.getGrossCost();
            totalItems += invoice.getTotalItems();
            out.append(invoice.generateSummaryReport());
        }

        out.append("+----------------------------------------------------------------------------------------+\n");
        out.append(
                String.format("%54d          $ %9.2f $ %12.2f\n", totalItems, totalTax, totalCost)
        );

        return out.toString();
    }

    /**
     * Generates a summary report for each store based off the database.
     *
     * @return A summary report for stores
     */
    public String generateStoreSummary() {
        ArrayList<Store> stores = new ArrayList<>(db.getStores());
        double total = 0.0;
        int totalNumSales = 0;

        // Sorts stores by last name, then first, then sales.
        stores.sort(
                (store1, store2) -> {
                    int managerLast = store1.getManager().getLastName().compareTo(store2.getManager().getLastName());
                    int managerFirst = store1.getManager().getFirstName().compareTo(store2.getManager().getFirstName());

                    if (managerLast != 0) {
                        // Last names are different
                        return managerLast;
                    } else if (managerFirst != 0) {
                        // First names are different
                        return managerFirst;
                    } else {
                        // Swap store1 and store2 so they go in descending order
                        return store2.getTotalSales().compareTo(store1.getTotalSales());
                    }
                }
        );

        StringBuilder out = new StringBuilder("+----------------------------------------------------------------+\n" +
                "| Store Sales Summary Report                                     |\n" +
                "+----------------------------------------------------------------+\n" +
                "Store      Manager                        # Sales      Grand Total\n");
        // Generate each stores summary and add it to the report.
        for (Store store : stores) {
            total += store.getTotalSales();
            totalNumSales += store.getNumSales();
            out.append(store.generateSummary());
        }

        out.append("+----------------------------------------------------------------+\n");
        out.append(
                String.format("%43d          $ %11.2f\n", totalNumSales, total)
        );

        return out.toString();
    }

    /**
     * Generates in depth reports for each invoice and concatenates them.
     *
     * @return In depth report for each invoice
     */
    public String generateInvoiceReports() {
        StringBuilder out = new StringBuilder();

        for (Invoice i : db.getInvoices()) {
            out.append(i.generateFullReport()).append("\n\n");
        }

        return out.toString();
    }
}
