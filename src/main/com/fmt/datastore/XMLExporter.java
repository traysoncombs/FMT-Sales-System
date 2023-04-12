package com.fmt.datastore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class to export the database to XML.
 */
public final class XMLExporter {
    private static final String OUTPUT_DIR = "data";
    private static final String PEOPLE_OUTPUT = OUTPUT_DIR + "/Persons";
    private static final String STORES_OUTPUT = OUTPUT_DIR + "/Stores";
    private static final String ITEMS_OUTPUT = OUTPUT_DIR + "/Items";
    private static final String INVOICE_ITEMS_OUTPUT = OUTPUT_DIR + "/InvoiceItems";
    private static final String INVOICES_OUTPUT = OUTPUT_DIR + "/Invoices";

    /**
     * Exports the database to three respective XML files.
     *
     * @param db The database to export.
     */
    public static void exportToXML(LegacyDatastore db) {
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

            writer = new FileWriter(INVOICES_OUTPUT + ".xml");
            writer.write(xStream.toXML(db.getInvoices()));
            writer.close();

            writer = new FileWriter(INVOICE_ITEMS_OUTPUT + ".xml");
            writer.write(xStream.toXML(db.getInvoiceItems()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error exporting db to XML.");
        }
    }
}
