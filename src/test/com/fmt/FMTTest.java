package com.fmt;

import org.junit.Test;

public class FMTTest {
    private Database csvDB = new Database("data/Persons.csv", "data/Stores.csv", "data/Items.csv", Database.DataFormat.CSV);
    private Database jsonDB = new Database("data/Persons.json", "data/Stores.json", "data/Items.json", Database.DataFormat.JSON);

    @Test
    public void compareDBS() {
        assert csvDB.equals(jsonDB);
    }

}
