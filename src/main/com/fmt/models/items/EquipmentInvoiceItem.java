package com.fmt.models.items;

import java.util.Objects;

/**
 * Models an equipment item.
 */
public class EquipmentInvoiceItem extends InvoiceItem{
    private final String model;
    public EquipmentInvoiceItem(String code, String name, String model) {
        super(code, name);
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EquipmentInvoiceItem that = (EquipmentInvoiceItem) o;
        return Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), model);
    }
}
