package com.fmt.models.items;

import java.util.Objects;

/**
 * Models a service item.
 */
public class ServiceInvoiceItem extends InvoiceItem {
    private final Float hourlyRate;
    public ServiceInvoiceItem(String code, String name, Float hourlyRate) {
        super(code, name);
        this.hourlyRate = hourlyRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ServiceInvoiceItem that = (ServiceInvoiceItem) o;
        return Objects.equals(hourlyRate, that.hourlyRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hourlyRate);
    }
}
