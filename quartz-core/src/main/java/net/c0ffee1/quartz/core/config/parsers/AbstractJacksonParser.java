package net.c0ffee1.quartz.core.config.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.c0ffee1.quartz.core.config.nodes.ConfigNode;
import net.c0ffee1.quartz.core.config.nodes.JacksonNode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public abstract class AbstractJacksonParser implements ConfigParser{
    @Override
    public ConfigNode getConfigTree(InputStream inputStream)  {
        try {
            return JacksonNode.of(getMapper().readTree(inputStream));
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean loadConfig(ConfigNode node, Object instance) {
        try {
            getMapper().readerForUpdating(instance).readValue(((JacksonNode) node).getJsonNode());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getConfigAsString(Object config) {
        try {
            return getMapper().writeValueAsString(config);
        }
        catch (Exception e){
            throw new RuntimeException();
        }
    }

    @Override
    public boolean writeConfig(Path path, Object configInstance) {
        try {
            getMapper().writeValue(path.toFile(), configInstance);
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public abstract ObjectMapper getMapper();

}
