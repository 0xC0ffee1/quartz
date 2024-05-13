package net.c0ffee1.quartz.platforms.bukkit.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;

public class BukkitTomlMapper extends TomlMapper {
    public BukkitTomlMapper() {
        super();
        SimpleModule module = new SimpleModule();
        // Register the deserializer for all ConfigurationSerializable implementations
        module.addDeserializer(ConfigurationSerializable.class, new BukkitConfigDeserializer());
        module.addSerializer(ConfigurationSerializable.class, new BukkitConfigSerializer());
        this.registerModule(module);
    }
}