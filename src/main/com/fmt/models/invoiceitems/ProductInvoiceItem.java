package com.fmt.models.invoiceitems;


import com.fmt.models.items.ProductItem;

public class ProductInvoiceItem extends InvoiceItem {
    private Double quantity;
    @Override
    public Double getTax() {
        return getGrossCost() * 7.15;
    }

    @Override
    public Double getGrossCost() {
        ProductItem productItem = (ProductItem) item;
        return productItem.getUnitPrice() * quantity;
    }
}
