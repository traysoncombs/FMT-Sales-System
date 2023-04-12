package com.fmt.models.invoiceitems;

import com.fmt.datastore.Datastore;
import com.fmt.models.Persistable;
import com.fmt.models.items.EquipmentItem;
import com.fmt.models.items.Item;
import com.fmt.models.items.ProductItem;
import com.fmt.models.items.ServiceItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Models an item invoice item and contains the factory
 * method the build them from CSV.
 *
 * @param <T>
 */
public abstract class InvoiceItem<T extends Item> implements Persistable {
    protected final String invoiceCode;
    protected T item;

    public InvoiceItem(String invoiceCode, T item) {
        this.invoiceCode = invoiceCode;
        this.item = item;
    }

    /**
     * Factory method to build an instance of InvoiceItem from
     * CSV. The appropriate subclass is determined by the code
     * of the item associated with the invoice item.
     *
     * @param csv The InvoiceItem in CSV format
     * @param ds A reference to the database containing Items
     * @return An instance of the relevant subclass of InvoiceItem
     */
    public static InvoiceItem<?> fromCSV(String csv, Datastore ds) {
        String[] data = csv.split(",");
        Item item = ds.getItemByCode(data[1]);

        if (item == null) {
            System.out.println("Item doesn't exist in database");
            return null;
        }

        String code = data[0];

        if (item instanceof EquipmentItem) {
            return data[2].equals("L") ?
                    new LeasedEquipmentInvoiceItem(
                            code,
                            (EquipmentItem) item,
                            Double.parseDouble(data[3]),
                            LocalDate.parse(data[4]),
                            LocalDate.parse(data[5])
                    ) :
                    new PurchasedEquipmentInvoiceItem(
                            code,
                            (EquipmentItem) item,
                            Double.parseDouble(data[3])
                    );
        } else if (item instanceof ProductItem) {
            return new ProductInvoiceItem(code, (ProductItem) item, Double.parseDouble(data[2]));
        } else if (item instanceof ServiceItem) {
            return new ServiceInvoiceItem(code, (ServiceItem) item, Double.parseDouble(data[2]));
        } else {
            throw new IllegalArgumentException("Malformed invoice item: Item doesn't exist.");
        }
    }

    /**
     * Takes a ResultSet containing the fields :
     * `invoice_code`, `item_code`, `quantity`, `purchase_price`,
     * `hours_billed`, `fee`, `start_date`, `end_date`, `discriminator`
     *
     * @param rs ResultSet containing relevant fields.
     * @param ds Datastore containing relevant items.
     * @return InvoiceItem or null if associated item doesn't exist.
     * @throws SQLException if ResultSet doesn't contain necessary fields.
     */
    public static InvoiceItem<?> fromRow(ResultSet rs, Datastore ds) throws SQLException {
        Item item = ds.getItemByCode(rs.getString("item_code"));
        String invoice_code = rs.getString("invoice_code");
        // If the item doesn't exist, we just ignore the invoice item.
        if (item == null) return null;

        switch (rs.getString("discriminator")) {
            case "P":
                return new ProductInvoiceItem(
                        invoice_code,
                        (ProductItem) item,
                        rs.getDouble("quantity")
                );
            case "PE":
                return new PurchasedEquipmentInvoiceItem(
                        invoice_code,
                        (EquipmentItem) item,
                        rs.getDouble("purchase_price")
                );
            case "S":
                return new ServiceInvoiceItem(
                        invoice_code,
                        (ServiceItem) item,
                        rs.getDouble("hours_billed")
                );
            case "L":
                return new LeasedEquipmentInvoiceItem(
                        invoice_code,
                        (EquipmentItem) item,
                        rs.getDouble("fee"),
                        LocalDate.parse(rs.getString("start_date")),
                        LocalDate.parse(rs.getString("end_date"))
                );
            default:
                return null;
        }
    }

    public abstract String generateReport();

    public abstract Double getTax();

    public abstract Double getNetCost();

    public Double getGrossCost() {
        return this.getNetCost() + this.getTax();
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceItem<?> that = (InvoiceItem<?>) o;
        return Objects.equals(invoiceCode, that.invoiceCode) && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceCode, item);
    }
}
