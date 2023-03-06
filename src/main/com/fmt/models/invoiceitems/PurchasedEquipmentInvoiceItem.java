package com.fmt.models.invoiceitems;

public class PurchasedEquipmentInvoiceItem extends InvoiceItem {
    private final Double purchasePrice;

    public PurchasedEquipmentInvoiceItem(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    @Override
    public Double getTax() {
        return 0.0;
    }

    @Override
    public Double getGrossCost() {
        return purchasePrice;
    }
}
