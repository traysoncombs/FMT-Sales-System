package com.fmt;

import com.fmt.models.items.Item;
import com.fmt.models.items.ItemDerserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import javax.xml.crypto.Data;
import java.io.FileWriter;
import java.io.IOException;

public class DatabaseExporter {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Item.class, new ItemDerserializer())
            .setPrettyPrinting()
            .create();
    private static final String OUTPUT_DIR = "data";
    private static final String PEOPLE_OUTPUT = OUTPUT_DIR + "/Persons";
    private static final String STORES_OUTPUT = OUTPUT_DIR + "/Stores";
    private static final String ITEMS_OUTPUT = OUTPUT_DIR + "/Items";
    private static final String INVOICE_ITEMS_OUTPUT = OUTPUT_DIR + "/InvoiceItems";
    private static final String INVOICES_OUTPUT = OUTPUT_DIR + "/Invoices";


    /**
     * Exports the database to three JSON files specified by the OUTPUT
     * constants of this class.
     */
    public static void exportToJSON(Database db) {
        try {
            FileWriter writer = new FileWriter(PEOPLE_OUTPUT + ".json");
            JsonObject parent = new JsonObject();
            parent.add("persons", gson.toJsonTree(db.getPeople()));
            writer.write(gson.toJson(parent));
            writer.close();

            writer = new FileWriter(STORES_OUTPUT + ".json");
            parent = new JsonObject();
            parent.add("stores", gson.toJsonTree(db.getStores()));
            writer.write(gson.toJson(db.getStores()));
            writer.close();

            writer = new FileWriter(ITEMS_OUTPUT + ".json");
            parent = new JsonObject();
            parent.add("items", gson.toJsonTree(db.getItems()));
            writer.write(gson.toJson(db.getItems()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error exporting db to json.");
        }
    }

    /**
     * Exports the database to three respective XML files.
     */
    public void exportToXML(Database db) {
        try {
            FileWriter writer = new FileWriter(PEOPLE_OUTPUT + ".xml");
            XStream xStream = new XStream(new DomDriver());
            writer.write(xStream.toXML(db.getPeople()));
            writer.close();

            writer = new FileWriter(STORES_OUTPUT + ".xml");
            writer.write(xStream.toXML(db.getStores()));
            writer.close();

            writer = new FileWriter(ITEMS_OUTPUT + ".xml");
            writer.write(xStream.toXML(db.getItems()));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error exporting db to XML.");
        }
    }

}
