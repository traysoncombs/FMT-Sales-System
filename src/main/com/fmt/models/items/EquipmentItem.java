package com.fmt.models.items;

import java.util.Objects;

/**
 * Models an equipment item.
 */
public class EquipmentItem extends Item {
    private final String model;
    public EquipmentItem(String code, String name, String model) {
        super(code, name);
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EquipmentItem that = (EquipmentItem) o;
        return Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), model);
    }

    @Override
    public boolean saveToDB() {
        return false;
    }
}
