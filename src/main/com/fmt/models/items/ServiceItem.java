package com.fmt.models.items;

import java.util.Objects;

/**
 * Models a service item.
 */
public class ServiceItem extends Item {
    private final Double hourlyRate;

    public ServiceItem(String code, String name, Double hourlyRate) {
        super(code, name);
        this.hourlyRate = hourlyRate;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ServiceItem that = (ServiceItem) o;
        return Objects.equals(hourlyRate, that.hourlyRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hourlyRate);
    }
}
