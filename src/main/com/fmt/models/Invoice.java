package com.fmt.models;

import com.fmt.Database;
import com.fmt.models.invoiceitems.InvoiceItem;

import java.time.LocalDate;
import java.util.ArrayList;

public class Invoice {
    private final String invoiceCode;
    private final String storeCode;
    private final Person customer;
    private final Person salesPerson;
    private final LocalDate invoiceDate;
    private ArrayList<InvoiceItem> invoiceItems;

    public Invoice(String invoiceCode, String storeCode, Person customer, Person salesPerson, LocalDate invoiceDate) {
        this.invoiceCode = invoiceCode;
        this.storeCode = storeCode;
        this.customer = customer;
        this.salesPerson = salesPerson;
        this.invoiceDate = invoiceDate;
    }


    public static Invoice fromCSV(String csv, Database db) {

    }

}
