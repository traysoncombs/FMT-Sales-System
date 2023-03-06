package com.fmt.models.invoiceitems;

import com.fmt.models.items.Item;

public abstract class InvoiceItem {
    private String invoiceCode;
    protected Item item;

    public abstract Double getTax();

    public abstract Double getGrossCost();

    public double getNetCost() {
        return this.getGrossCost() - this.getTax();
    }
}
