package com.fmt;

import com.google.gson.JsonObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.FileWriter;
import java.io.IOException;

public class DatabaseExporter {
    private static final String OUTPUT_DIR = "data";
    private static final String PEOPLE_OUTPUT = OUTPUT_DIR + "/Persons";
    private static final String STORES_OUTPUT = OUTPUT_DIR + "/Stores";
    private static final String ITEMS_OUTPUT = OUTPUT_DIR + "/Items";


    /**
     * Exports the database to three JSON files specified by the OUTPUT
     * constants of this class.
     */
    public void exportToJSON() {
        try {
            FileWriter writer = new FileWriter(PEOPLE_OUTPUT + ".json");
            JsonObject parent = new JsonObject();
            parent.add("persons", gson.toJsonTree(people));
            writer.write(gson.toJson(parent));
            writer.close();

            writer = new FileWriter(STORES_OUTPUT + ".json");
            parent = new JsonObject();
            parent.add("stores", gson.toJsonTree(stores));
            writer.write(gson.toJson(stores));
            writer.close();

            writer = new FileWriter(ITEMS_OUTPUT + ".json");
            parent = new JsonObject();
            parent.add("items", gson.toJsonTree(items));
            writer.write(gson.toJson(items));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error exporting db to json.");
        }
    }

    /**
     * Exports the database to three respective XML files.
     */
    public void exportToXML() {
        try {
            FileWriter writer = new FileWriter(PEOPLE_OUTPUT + ".xml");
            XStream xStream = new XStream(new DomDriver());
            writer.write(xStream.toXML(people));
            writer.close();

            writer = new FileWriter(STORES_OUTPUT + ".xml");
            writer.write(xStream.toXML(stores));
            writer.close();

            writer = new FileWriter(ITEMS_OUTPUT + ".xml");
            writer.write(xStream.toXML(items));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error exporting db to XML.");
        }
    }

}
