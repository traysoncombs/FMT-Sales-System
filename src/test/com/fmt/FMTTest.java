package com.fmt;

import org.junit.Test;

public class FMTTest {
    private Database csvDB = new Database(
            "data/Persons.csv",
            "data/Stores.csv",
            "data/Items.csv",
            "data/Invoices.csv",
            "data/InvoiceItems.csv",
            Database.DataFormat.CSV);
    private Database jsonDB = new Database(
            "data/Persons.json",
            "data/Stores.json",
            "data/Items.json",
            "data/Invoices.json",
            "data/InvoiceItems.json",
            Database.DataFormat.JSON);

    @Test
    public void compareDBS() {
        assert csvDB.equals(jsonDB);
    }

}
