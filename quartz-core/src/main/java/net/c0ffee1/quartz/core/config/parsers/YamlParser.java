package net.c0ffee1.quartz.core.config.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class YamlParser extends AbstractJacksonParser{
    protected final ObjectMapper mapper = new YAMLMapper();
    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }

    @Override
    public String[] getExtensions() {
        return new String[]{"yml", "yaml"};
    }
}
