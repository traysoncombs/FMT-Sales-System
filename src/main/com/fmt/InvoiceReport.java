package com.fmt;

/**
 * Author: Trayson Combs
 * Date: 2023/02/22
 *
 * Collection of classes to model and store data associated with
 * the FMT sales system.
 */

public class InvoiceReport {
    public static void main(String[] args) {
        Database db = new Database(
                "data/Persons.csv",
                "data/Stores.csv",
                "data/Items.csv",
                "data/Invoices.csv",
                "data/InvoiceItems.csv",
                Database.DataFormat.CSV);

        ReportGenerator reportGenerator = new ReportGenerator(db);
        System.out.println(reportGenerator.generateSummaryReport());
        System.out.println(reportGenerator.generateStoreSummary());
        System.out.println(reportGenerator.generateInvoiceSummaries());

    }



}
