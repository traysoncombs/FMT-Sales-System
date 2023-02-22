package com.fmt.models.items;

import java.util.Objects;

/**
 * Models a product item.
 */
public class ProductInvoiceItem extends InvoiceItem {
    private final String unit;
    private final Float unitPrice;
    public ProductInvoiceItem(String code, String name, String unit, Float unitPrice) {
        super(code, name);
        this.unit = unit;
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductInvoiceItem that = (ProductInvoiceItem) o;
        return Objects.equals(unit, that.unit) && Objects.equals(unitPrice, that.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), unit, unitPrice);
    }
}
