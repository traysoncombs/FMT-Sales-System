package com.fmt;

import org.junit.Test;

public class FMTTest {
    private Database csvDB = new Database("Persons.test.csv", "Stores.test.csv", "Items.test.csv", Database.DataFormat.CSV);
    private Database jsonDB = new Database("Persons.test.json", "Stores.test.json", "Items.test.json", Database.DataFormat.JSON);

    @Test
    public void compareDBS() {
        assert csvDB.equals(jsonDB);
    }

}
