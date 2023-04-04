package com.fmt.models.invoiceitems;

import com.fmt.models.items.EquipmentItem;

/**
 * Models an equipment item that was purchased.
 */
public class PurchasedEquipmentInvoiceItem extends InvoiceItem<EquipmentItem> {
    private final Double purchasePrice;

    public PurchasedEquipmentInvoiceItem(String itemCode, EquipmentItem item, Double purchasePrice) {
        super(itemCode, item);
        this.purchasePrice = purchasePrice;
    }

    @Override
    public Double getTax() {
        return 0.0;
    }

    @Override
    public Double getNetCost() {
        return purchasePrice;
    }

    @Override
    public String generateReport() {
        return String.format("%s      (Purchase)  %s %s\n", item.getItemCode(), item.getName(), item.getModel());
    }
}
