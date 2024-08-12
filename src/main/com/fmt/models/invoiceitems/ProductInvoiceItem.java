package com.fmt.models.invoiceitems;


import com.fmt.models.items.ProductItem;

/**
 * Models a product that was purchased.
 */
public class ProductInvoiceItem extends InvoiceItem<ProductItem> {
    private final Double quantity;

    public ProductInvoiceItem(String invoiceCode, ProductItem item, Double quantity) {
        super(invoiceCode, item);
        this.quantity = quantity;
    }

    /**
     * Calculates the 7.15% service tax
     * rounded to the nearest hundredth.
     */
    @Override
    public Double getTax() {
        return Math.round(getNetCost() * 7.15) / 100.0;
    }

    /**
     * Calculates the cost excluding tax
     * and rounded to the nearest hundredth
     * of this item.
     */
    @Override
    public Double getNetCost() {
        ProductItem productItem = item;
        return Math.round(productItem.getUnitPrice() * quantity * 100.0) / 100.0;
    }

    @Override
    public String generateReport() {
        return String.format("%s      (Product)  %s\n", item.getItemCode(), item.getName()) +
                String.format("        %.2f @ $ %.2f/%s\n", quantity, item.getUnitPrice(), item.getUnit());
    }
}
