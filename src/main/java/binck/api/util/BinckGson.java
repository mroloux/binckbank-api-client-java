package binck.api.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;

public class BinckGson {

    public static Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Instant.class, instantDeserializer())
                .create();
    }

    private static JsonDeserializer<Instant> instantDeserializer() {
        return (JsonElement json, Type typeOfT, JsonDeserializationContext ctx) -> Instant.parse(json.getAsJsonPrimitive().getAsString());
    }
}
