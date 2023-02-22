package com.fmt.models;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Abstract class to make JSON serialization easier.
 * At this point Database handles all serialization so this
 * isn't strictly necessary and may be removed at some
 * point in the future.
 */
public abstract class JsonSerializable {
    protected static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * JSON serializes this object.
     *
     * @return JSON serialized string of this object.
     */
    public String toJSON() {
        return gson.toJson(this, this.getClass());
    }

    /**
     * Deserializes a JSON string into an object that inherits from JsonSerializable.
     *
     * @param json String containing the JSON.
     * @param clazz The class of the object that is being deserialized.
     * @return An instance of clazz created from JSON
     */
    public static <T extends JsonSerializable> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
