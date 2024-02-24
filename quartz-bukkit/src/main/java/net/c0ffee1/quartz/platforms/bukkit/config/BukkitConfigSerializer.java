package net.c0ffee1.quartz.platforms.bukkit.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BukkitConfigSerializer extends StdSerializer<ConfigurationSerializable> {

    public BukkitConfigSerializer() {
        this(null);
    }

    public BukkitConfigSerializer(Class<ConfigurationSerializable> t) {
        super(t);
    }

    @Override
    public void serialize(ConfigurationSerializable value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Map<String, Object> serialized = value.serialize();

        // Add the class name as a type specifier
        String className = value.getClass().getName();
        Map<String, Object> withType = new HashMap<>(serialized);
        withType.put("==", className); // "==" as the type specifier, as in Bukkit's convention

        // Write the map as JSON
        gen.writeObject(withType);
    }
}