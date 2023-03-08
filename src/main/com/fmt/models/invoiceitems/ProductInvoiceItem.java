package com.fmt.models.invoiceitems;


import com.fmt.models.items.Item;
import com.fmt.models.items.ProductItem;

public class ProductInvoiceItem extends InvoiceItem<ProductItem> {
    private final Double quantity;

    public ProductInvoiceItem(String invoiceCode, ProductItem item, Double quantity) {
        super(invoiceCode, item);
        this.quantity = quantity;
    }

    @Override
    public Double getTax() {
        return Math.round(getNetCost() * 7.15) / 100.0;
    }

    @Override
    public Double getNetCost() {
        ProductItem productItem = item;
        return Math.round(productItem.getUnitPrice() * quantity * 100.0) / 100.0;
    }

    public String toString() {
        return String.format("%s      (Product)  %s\n", item.getItemCode(), item.getName()) +
                String.format("        %.2f @ $ %.2f/%s\n", quantity, item.getUnitPrice(), item.getUnit());
    }
}
