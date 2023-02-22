package com.fmt;

import com.fmt.models.Person;
import com.fmt.models.Store;
import com.fmt.models.items.InvoiceItem;

import com.fmt.models.items.InvoiceItemDerserializer;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
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
    private final ArrayList<InvoiceItem> items = new ArrayList<>();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(InvoiceItem.class, new InvoiceItemDerserializer())
            .setPrettyPrinting()
            .create();
    private static final String OUTPUT_DIR = "data";
    private static final String PEOPLE_OUTPUT = OUTPUT_DIR + "/Persons.json";
    private static final String STORES_OUTPUT = OUTPUT_DIR + "/Stores.json";
    private static final String ITEMS_OUTPUT = OUTPUT_DIR + "/Items.json";

    /**
     * Constructs a Database given the files to each dataset, as well as
     * the type of data being imported. Supports JSON and CSV.
     *
     * @param peopleFile Path to the file containing people.
     * @param storesFile Path to the file containing stores.
     * @param itemsFile Path to the file containing invoice items.
     * @param format Format of the data to be imported, JSON or CSV.
     */
    public Database(String peopleFile, String storesFile, String itemsFile, DataFormat format) {
        switch (format) {
            case CSV:
                // Must import people before stores in order to relate the managers
                this.importFromCSV(peopleFile, FieldType.PEOPLE);
                this.importFromCSV(itemsFile, FieldType.ITEMS);
                this.importFromCSV(storesFile, FieldType.STORES);

                break;
            case JSON:
                // Order doesn't matter as much here as stores has already been related.
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
                    stores.add(Store.fromCSV(line, this));
                    break;
                case ITEMS:
                    items.add(InvoiceItem.fromCSV(line));
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
                TypeToken<ArrayList<InvoiceItem>> itemsArray = new TypeToken<ArrayList<InvoiceItem>>(){};
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
        for (Person p : people) {
            if (p.getPersonCode().equals(code)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Exports the database to three JSON files specified by the OUTPUT
     * constants of this class.
     */
    public void exportToJSON() {
        try {
            FileWriter writer = new FileWriter(PEOPLE_OUTPUT);
            JsonObject parent = new JsonObject();
            parent.add("persons", gson.toJsonTree(people));
            writer.write(gson.toJson(parent));
            writer.close();

            writer = new FileWriter(STORES_OUTPUT);
            parent = new JsonObject();
            parent.add("stores", gson.toJsonTree(stores));
            writer.write(gson.toJson(stores));
            writer.close();

            writer = new FileWriter(ITEMS_OUTPUT);
            parent = new JsonObject();
            parent.add("items", gson.toJsonTree(items));
            writer.write(gson.toJson(items));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error exporting db to json.");
        }
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

    public enum FieldType {
        PEOPLE,
        STORES,
        ITEMS,
    }

    public enum DataFormat {
        CSV,
        JSON,
    }
}