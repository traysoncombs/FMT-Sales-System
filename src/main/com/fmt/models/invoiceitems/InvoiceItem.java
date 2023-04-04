package com.fmt.models.invoiceitems;

import com.fmt.Database;
import com.fmt.models.items.EquipmentItem;
import com.fmt.models.items.Item;
import com.fmt.models.items.ProductItem;
import com.fmt.models.items.ServiceItem;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Models an item invoice item and contains the factory
 * method the build them from CSV.
 *
 * @param <T>
 */
public abstract class InvoiceItem<T extends Item> {
    protected final String invoiceCode;
    protected T item;

    public InvoiceItem(String invoiceCode, T item) {
        this.invoiceCode = invoiceCode;
        this.item = item;
    }

    /**
     * Factory method to build an instance of InvoiceItem from
     * CSV. The appropriate sub class is determined by the code
     * of the item associated with the invoice item.
     *
     * @param csv The InvoiceItem in CSV format
     * @param db A reference to the database containing Items
     * @return An instance of the relevant subclass of InvoiceItem
     */
    public static InvoiceItem<?> fromCSV(String csv, Database db) {
        String[] data = csv.split(",");
        Item item = db.getItemByCode(data[1]);

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
