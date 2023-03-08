package com.fmt.models.invoiceitems;

import com.fmt.Database;
import com.fmt.models.items.EquipmentItem;
import com.fmt.models.items.Item;
import com.fmt.models.items.ProductItem;
import com.fmt.models.items.ServiceItem;

import java.time.LocalDate;

public abstract class InvoiceItem<T extends Item> {
    protected final String invoiceCode;
    protected T item;

    public InvoiceItem(String invoiceCode, T item) {
        this.invoiceCode = invoiceCode;
        this.item = item;
    }

    public abstract Double getTax();

    public abstract Double getNetCost();

    public Double getGrossCost() {
        return this.getNetCost() + this.getTax();
    }

    public static InvoiceItem<?> fromCSV(String csv, Database db) {
        String[] data = csv.split(",");
        Item item = db.getItemByCode(data[1]);
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

    public String getInvoiceCode() {
        return invoiceCode;
    }
}
