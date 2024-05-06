package net.c0ffee1.quartz.platforms.bukkit.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

public class JacksonConfigurationSection extends FileConfiguration {
    private JsonNode root;
    private final ObjectMapper objectMapper;

    public JacksonConfigurationSection(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public @NotNull String saveToString() {
        try {
            return objectMapper.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadFromString(@NotNull String contents) throws InvalidConfigurationException {
        try {
            root = objectMapper.readTree(contents);
            this.map.clear(); // Assuming 'map' is a structure in FileConfiguration for holding config data.
            fromNodeTree(root, this);
        } catch (JsonProcessingException e) {
            throw new InvalidConfigurationException("Invalid JSON format", e);
        }
    }

    private void fromNodeTree(JsonNode node, ConfigurationSection section) throws JsonProcessingException {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                JsonNode valueNode = entry.getValue();

                // If it's a complex node, create a section; otherwise, set the value directly.
                if (valueNode.isObject()) {
                    ConfigurationSection subSection = section.createSection(entry.getKey());
                    fromNodeTree(valueNode, subSection);
                }
                else {
                    section.set(entry.getKey(), objectMapper.convertValue(valueNode, Object.class));
                }
            }
        }
    }
}
