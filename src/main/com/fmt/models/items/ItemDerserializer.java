package com.fmt.models.items;

import com.google.gson.*;

import java.lang.reflect.Type;


/**
 * Type adapter to handle deserialization of invoice items.
 * This is necessary because GSON doesn't know how to deserialize
 * instances of a subclass given an abstract parent class, so we
 * are able to determine the subclass type by checking the unique
 * attributes associated with each.
 */
public class ItemDerserializer implements JsonDeserializer<Item> {
    @Override
    public Item deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        if (obj.has("model")) {
            return new EquipmentItem(
                    obj.get("code").getAsString(),
                    obj.get("name").getAsString(),
                    obj.get("model").getAsString()
            );
        } else if (obj.has("unit")) {
            return new ProductItem(
                    obj.get("code").getAsString(),
                    obj.get("name").getAsString(),
                    obj.get("unit").getAsString(),
                    obj.get("unitPrice").getAsDouble()
            );
        } else if (obj.has("hourlyRate")) {
            return new ServiceItem(
                    obj.get("code").getAsString(),
                    obj.get("name").getAsString(),
                    obj.get("hourlyRate").getAsDouble()
            );
        }
        return null;
    }
}
