package com.fmt.models.items;

import com.fmt.models.JsonSerializable;

import java.util.Objects;

/**
 * A class that models an invoice item.
 */
public abstract class InvoiceItem extends JsonSerializable {
    protected final String code;
    protected final String name;

    /**
     *
     * @param code The unique code assigned to this item.
     * @param name The name of this item.
     */
    public InvoiceItem(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Creates an instance of the subclass of InvoiceItem that is
     * associated with the CSV string.
     *
     * @param csv CSV string containing the item.
     * @return A instance of a subclass of InvoiceItem
     */
    public static InvoiceItem fromCSV(String csv) {
        String[] data = csv.split(",");
        switch (data[1]) {
            case "E":
                return new EquipmentInvoiceItem(data[0], data[2], data[3]);
            case "P":
                return new ProductInvoiceItem(data[0], data[2], data[3], Float.parseFloat(data[4]));
            case "S":
                return new ServiceInvoiceItem(data[0], data[2], Float.parseFloat(data[3]));
            default:
                throw new RuntimeException("Malformed item CSV, item type doesn't exist");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceItem that = (InvoiceItem) o;
        return Objects.equals(code, that.code) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }
}
