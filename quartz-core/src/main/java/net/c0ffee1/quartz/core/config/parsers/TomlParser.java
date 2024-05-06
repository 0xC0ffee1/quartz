package net.c0ffee1.quartz.core.config.parsers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import net.c0ffee1.quartz.core.config.ConfigManager;
import net.c0ffee1.quartz.core.config.nodes.ConfigNode;
import net.c0ffee1.quartz.core.config.nodes.JacksonNode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;


public class TomlParser extends AbstractJacksonParser {
    protected final ObjectMapper mapper = new TomlMapper();

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }

    @Override
    public String[] getExtensions() {
        return new String[]{"toml"};
    }
}
