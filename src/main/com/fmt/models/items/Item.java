package com.fmt.models.items;

import java.sql.ResultSet;
import java.sql.SQLException;
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
     * @return An instance of a subclass of InvoiceItem
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

    /**
     * Takes a ResultSet containing the fields:
     * `code`, `name`, `model`, `unit`, `unit_price`,
     * `hourly_rate`, `discriminator`
     * @param rs ResultSet containing necessary fields.
     * @return Item of type specified by the discriminator or null
     * if discriminator is invalid.
     * @throws SQLException if ResultSet doesn't contain necessary fields.
     */
    public static Item fromRow(ResultSet rs) throws SQLException {
        String code = rs.getString("code");
        String name = rs.getString("name");
        switch (rs.getString("discriminator")) {
            case "E":
                return new EquipmentItem(code, name, rs.getString("model"));
            case "P":
                return new ProductItem(code, name, rs.getString("unit"), rs.getDouble("unit_price"));
            case "S":
                return new ServiceItem(code, name, rs.getDouble("hourly_rate"));
            default:
                // This should never happen as discriminator is an enum and non-nullable
                return null;

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
