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
public class InvoiceItemDerserializer implements JsonDeserializer<InvoiceItem> {
    @Override
    public InvoiceItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        if (obj.has("model")) {
            return new EquipmentInvoiceItem(
                    obj.get("code").getAsString(),
                    obj.get("name").getAsString(),
                    obj.get("model").getAsString()
            );
        } else if (obj.has("unit")) {
            return new ProductInvoiceItem(
                    obj.get("code").getAsString(),
                    obj.get("name").getAsString(),
                    obj.get("unit").getAsString(),
                    obj.get("unitPrice").getAsFloat()
            );
        } else if (obj.has("hourlyRate")) {
            return new ServiceInvoiceItem(
                    obj.get("code").getAsString(),
                    obj.get("name").getAsString(),
                    obj.get("hourlyRate").getAsFloat()
            );
        }
        return null;
    }
}
