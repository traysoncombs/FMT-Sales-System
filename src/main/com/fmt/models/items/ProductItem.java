package com.fmt.models.items;

import java.util.Objects;

/**
 * Models a product item.
 */
public class ProductItem extends Item {
    private final String unit;
    private final Double unitPrice;

    public ProductItem(String code, String name, String unit, Double unitPrice) {
        super(code, name);
        this.unit = unit;
        this.unitPrice = unitPrice;
    }

    public String getUnit() {
        return unit;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductItem that = (ProductItem) o;
        return Objects.equals(unit, that.unit) && Objects.equals(unitPrice, that.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), unit, unitPrice);
    }
}
