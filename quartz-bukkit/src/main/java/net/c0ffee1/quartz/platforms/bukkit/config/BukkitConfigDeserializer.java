package net.c0ffee1.quartz.platforms.bukkit.config;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BukkitConfigDeserializer extends JsonDeserializer<ConfigurationSerializable> {

    @Override
    public ConfigurationSerializable deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        return deserializeNode(node);
    }

    private ConfigurationSerializable deserializeNode(JsonNode node) throws IOException {
        if (!node.has("==")) {
            throw new IOException("Type specifier '==' not found in node");
        }

        String className = node.get("==").asText();
        try {
            Class<?> clazz = Class.forName(className);
            if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
                Map<String, Object> data = convertNodeToMap(node);
                return ConfigurationSerialization.deserializeObject(data, (Class<? extends ConfigurationSerializable>) clazz);
            } else {
                throw new IOException("Class does not implement ConfigurationSerializable: " + className);
            }
        } catch (ClassNotFoundException e) {
            throw new IOException("Failed to deserialize object due to missing class: " + className, e);
        }
    }

    private Map<String, Object> convertNodeToMap(JsonNode node) {
        Map<String, Object> map = new HashMap<>();
        node.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode value = entry.getValue();
            if (value.isObject()) {
                map.put(key, convertNodeToMap(value));
            } else {
                map.put(key, value.asText());
            }
        });
        return map;
    }
}