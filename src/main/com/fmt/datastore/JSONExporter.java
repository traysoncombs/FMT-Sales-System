package com.fmt.datastore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class to export the database to JSON.
 */
public final class JSONExporter {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private static final String OUTPUT_DIR = "data";
    private static final String PEOPLE_OUTPUT = OUTPUT_DIR + "/Persons";
    private static final String STORES_OUTPUT = OUTPUT_DIR + "/Stores";
    private static final String ITEMS_OUTPUT = OUTPUT_DIR + "/Items";
    private static final String INVOICE_ITEMS_OUTPUT = OUTPUT_DIR + "/InvoiceItems";
    private static final String INVOICES_OUTPUT = OUTPUT_DIR + "/Invoices";

    /**
     * Exports the datastore to three JSON files specified by the OUTPUT
     * constants of this class.
     *
     * @param db The datastore to export.
     */
    public static void exportToJSON(LegacyDatastore db) {
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

            writer = new FileWriter(INVOICES_OUTPUT + ".json");
            parent = new JsonObject();
            parent.add("invoices", gson.toJsonTree(db.getInvoices()));
            writer.write(gson.toJson(db.getInvoices()));
            writer.close();

            writer = new FileWriter(INVOICE_ITEMS_OUTPUT + ".json");
            parent = new JsonObject();
            parent.add("invoice_items", gson.toJsonTree(db.getInvoiceItems()));
            writer.write(gson.toJson(db.getInvoiceItems()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error exporting db to json.");
        }
    }
}
