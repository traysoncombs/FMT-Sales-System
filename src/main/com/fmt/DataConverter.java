package com.fmt;

/**
 * Author: Trayson Combs
 * Date: 2023/02/22
 *
 * Collection of classes to model and store data associated with
 * the FMT sales system.
 */

public class DataConverter {
    public static void main(String[] args) {
        Database db = new Database("data/Persons.csv", "data/Stores.csv", "data/Items.csv", Database.DataFormat.CSV);
        db.exportToJSON();
    }
}
