package net.c0ffee1.quartz.platforms.bukkit.config;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import net.c0ffee1.quartz.core.config.parsers.ConfigParser;
import net.c0ffee1.quartz.core.config.parsers.YamlParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class BukkitYamlParser extends YamlParser {
    @Override
    public boolean loadConfig(InputStream inputStream, Object configInstance)  {
        try {
            mapper.readerForUpdating(configInstance).readValue(inputStream);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean writeConfig(Path path, Object configInstance) {
        try {
            mapper.writeValue(path.toFile(), configInstance.getClass());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }
}
