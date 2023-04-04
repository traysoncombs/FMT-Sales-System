package com.fmt.models.items;

import java.util.Objects;

/**
 * A class that models an invoice item.
 */
public abstract class Item {
    protected final String itemCode;
    protected final String name;

    /**
     * @param itemCode The unique code assigned to this item.
     * @param name     The name of this item.
     */
    public Item(String itemCode, String name) {
        this.itemCode = itemCode;
        this.name = name;
    }

    /**
     * Creates an instance of the subclass of InvoiceItem that is
     * associated with the CSV string.
     *
     * @param csv CSV string containing the item.
     * @return A instance of a subclass of InvoiceItem
     */
    public static Item fromCSV(String csv) {
        String[] data = csv.split(",");
        switch (data[1]) {
            case "E":
                return new EquipmentItem(data[0], data[2], data[3]);
            case "P":
                return new ProductItem(data[0], data[2], data[3], Double.parseDouble(data[4]));
            case "S":
                return new ServiceItem(data[0], data[2], Double.parseDouble(data[3]));
            default:
                throw new RuntimeException("Malformed item CSV, item type doesn't exist");
        }
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item that = (Item) o;
        return Objects.equals(itemCode, that.itemCode) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemCode, name);
    }
}
