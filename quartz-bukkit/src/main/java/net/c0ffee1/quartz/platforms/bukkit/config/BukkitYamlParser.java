package net.c0ffee1.quartz.platforms.bukkit.config;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import net.c0ffee1.quartz.core.config.parsers.ConfigParser;
import net.c0ffee1.quartz.core.config.parsers.YamlParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class BukkitYamlParser extends YamlParser {
    private final BukkitYAMLMapper mapper = new BukkitYAMLMapper();

    @Override
    public BukkitYAMLMapper getMapper() {
        return mapper;
    }
}
