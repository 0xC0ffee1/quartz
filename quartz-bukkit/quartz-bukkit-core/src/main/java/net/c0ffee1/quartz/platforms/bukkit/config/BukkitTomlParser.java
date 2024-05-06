package net.c0ffee1.quartz.platforms.bukkit.config;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import net.c0ffee1.quartz.core.config.parsers.ConfigParser;
import net.c0ffee1.quartz.core.config.parsers.TomlParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class BukkitTomlParser extends TomlParser {
    private final BukkitTomlMapper mapper = new BukkitTomlMapper();

    @Override
    public BukkitTomlMapper getMapper() {
        return mapper;
    }
}
