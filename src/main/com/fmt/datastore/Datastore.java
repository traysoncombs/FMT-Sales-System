package com.fmt.datastore;

import com.fmt.models.Invoice;
import com.fmt.models.Person;
import com.fmt.models.Store;
import com.fmt.models.invoiceitems.InvoiceItem;
import com.fmt.models.items.Item;

import java.util.ArrayList;

public interface Datastore {
    /**
     * Returns a person in the database with the specified code if they exist.
     *
     * @param code Code of the person to be found.
     * @return Person with the specified code, or null if they don't exist.
     */
    Person getPersonByCode(String code);

    /**
     * Returns an item with the specified code, if it exists.
     *
     * @param code item code
     * @return Item with the specified code
     */
    Item getItemByCode(String code);

    /**
     * Gets a list of invoice items belonging to an Invoice.
     *
     * @param code Invoice code
     * @return A list of invoice items belonging to the specified Invoice.
     */
    ArrayList<InvoiceItem<?>> getInvoiceItemsByCode(String code);

    /**
     * Gets a list of invoices associated with a store.
     *
     * @param storeCode A stores code.
     * @return A list of Invoices associated with storeCode.
     */
    ArrayList<Invoice> getInvoicesByStore(String storeCode);

    ArrayList<Person> getPeople();
    ArrayList<Store> getStores();
    ArrayList<Item> getItems();
    ArrayList<InvoiceItem<?>> getInvoiceItems();
    ArrayList<Invoice> getInvoices();
}
