package net.c0ffee1.quartz.core.config.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class YamlParser implements ConfigParser{
    protected final ObjectMapper mapper = new YAMLMapper();
    @Override
    public boolean loadConfig(InputStream inputStream, Object configInstance)  {
        try {
            mapper.readerForUpdating(configInstance).readValue(inputStream);
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean writeConfig(Path path, Object configInstance) {
        try {
            mapper.writeValue(path.toFile(), configInstance);
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String[] getExtensions() {
        return new String[]{"yaml", "yml"};
    }
}
