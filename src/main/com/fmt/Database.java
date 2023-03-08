package com.fmt;

import com.fmt.models.Invoice;
import com.fmt.models.Person;
import com.fmt.models.Store;
import com.fmt.models.invoiceitems.InvoiceItem;
import com.fmt.models.items.Item;

import com.fmt.models.items.ItemDerserializer;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Class to hold lists of all the relevant models and facilitate the
 * relating, importing, and exporting of the necessary data.
 */
public class Database {
    private final ArrayList<Person> people = new ArrayList<>();
    private final ArrayList<Store> stores = new ArrayList<>();
    private final ArrayList<Item> items = new ArrayList<>();
    private final ArrayList<Invoice> invoices = new ArrayList<>();
    private final ArrayList<InvoiceItem<?>> invoiceItems = new ArrayList<>();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Item.class, new ItemDerserializer())
            .setPrettyPrinting()
            .create();


    /**
     * Constructs a Database given the files to each dataset, as well as
     * the type of data being imported. Supports JSON and CSV.
     *
     * @param peopleFile Path to the file containing people.
     * @param storesFile Path to the file containing stores.
     * @param itemsFile Path to the file containing invoice items.
     * @param format Format of the data to be imported, JSON or CSV.
     */
    public Database(String peopleFile, String storesFile, String itemsFile, String invoiceFile, String invoiceItemsFile, DataFormat format) {
        switch (format) {
            case CSV:
                // Must import people before stores in order to relate the managers
                this.importFromCSV(peopleFile, FieldType.PEOPLE);
                this.importFromCSV(itemsFile, FieldType.ITEMS);
                this.importFromCSV(invoiceItemsFile, FieldType.INVOICE_ITEMS);
                this.importFromCSV(invoiceFile, FieldType.INVOICE);
                this.importFromCSV(storesFile, FieldType.STORES);
                break;
            case JSON:
                // Order doesn't matter as much here as stores have already been related.
                this.importFromJSON(peopleFile, FieldType.PEOPLE);
                this.importFromJSON(storesFile, FieldType.STORES);
                this.importFromJSON(itemsFile, FieldType.ITEMS);
        }
    }

    /**
     * Imports a csv file with data containing type.
     *
     * @param csvFile Path to the CSV file that should be imported
     * @param type Type of data being imported, can be people, stores, or items.
     */
    public void importFromCSV(String csvFile, FieldType type) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(csvFile));
            lines.remove(""); // Remove any empty lines
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error opening file.");
        }

        for (String line : lines.subList(1, lines.size())) {
            switch (type) {
                case PEOPLE:
                    people.add(Person.fromCSV(line));
                    break;
                case STORES:
                    Store store = Store.fromCSV(line, this);
                    // If the store is null, exclude it from the db.
                    if (store == null) break;
                    else stores.add(store);
                    break;
                case ITEMS:
                    items.add(Item.fromCSV(line));
                    break;
                case INVOICE:
                    invoices.add(Invoice.fromCSV(line, this));
                    break;
                case INVOICE_ITEMS:
                    invoiceItems.add(InvoiceItem.fromCSV(line, this));
                    break;
            }
        }
    }

    /**
     * Imports a JSON file with data containing type.
     *
     * @param jsonFile Path to the JSON file that should be imported
     * @param type Type of data being imported, can be people, stores, or items.
     */
    public void importFromJSON(String jsonFile, FieldType type) {
        FileReader reader;
        try {
            reader = new FileReader(jsonFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error opening file.");
        }
        // This is necessary because the schema requires that the array is contained in an object
        // and this is a way to do it with GSON.
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
        switch (type) {
            case PEOPLE:
                TypeToken<ArrayList<Person>> personsArray = new TypeToken<ArrayList<Person>>(){};
                people.addAll(gson.fromJson(jsonObject.get("persons"), personsArray));
                break;
            case STORES:
                TypeToken<ArrayList<Store>> storesArray = new TypeToken<ArrayList<Store>>(){};
                stores.addAll(gson.fromJson(jsonObject.get("stores"), storesArray));
                break;
            case ITEMS:
                TypeToken<ArrayList<Item>> itemsArray = new TypeToken<ArrayList<Item>>(){};
                items.addAll(gson.fromJson(jsonObject.get("items"), itemsArray));
                break;
        }
    }


    /**
     * Returns a person in the database with the specified code if they exist.
     *
     * @param code Code of the person to be found.
     * @return Person with the specified code, or null if they don't exist.
     */
    public Person getPersonByCode(String code) {
        for (Person p : this.people) {
            if (p.getPersonCode().equals(code)) {
                return p;
            }
        }
        return null;
    }

    public Item getItemByCode(String code) {
        for (Item i : this.items) {
            if (i.getItemCode().equals(code)) {
                return i;
            }
        }
        return null;
    }

    public ArrayList<InvoiceItem<?>> getInvoiceItemsByCode(String code) {
        ArrayList<InvoiceItem<?>> invoiceItemsWithCode = new ArrayList<>();
        for (InvoiceItem<?> i : this.invoiceItems) {
            if (i.getInvoiceCode().equals(code)) {
                invoiceItemsWithCode.add(i);
            }
        }
        return invoiceItemsWithCode;
    }

    public ArrayList<Invoice> getInvoicesByStore(String storeCode) {
        ArrayList<Invoice> invoicesWithStore = new ArrayList<>();
        for (Invoice i : this.invoices) {
            if (i.getStoreCode().equals(storeCode)) {
                invoicesWithStore.add(i);
            }
        }
        return invoicesWithStore;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Database database = (Database) o;
        return people.equals(database.people) && stores.equals(database.stores) && items.equals(database.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(people, stores, items);
    }


    public ArrayList<Person> getPeople() {
        return people;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<Invoice> getInvoices() {
        return invoices;
    }

    public enum FieldType {
        PEOPLE,
        STORES,
        ITEMS,
        INVOICE_ITEMS,
        INVOICE,
    }

    public enum DataFormat {
        CSV,
        JSON,
    }
}