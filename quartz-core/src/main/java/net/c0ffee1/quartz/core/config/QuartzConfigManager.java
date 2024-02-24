package net.c0ffee1.quartz.core.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.c0ffee1.quartz.core.QuartzApplication;
import net.c0ffee1.quartz.core.annotations.Config;
import net.c0ffee1.quartz.core.config.parsers.ConfigParser;
import net.c0ffee1.quartz.core.config.parsers.ParserRegistry;
import net.c0ffee1.quartz.core.config.parsers.TomlParser;
import net.c0ffee1.quartz.core.config.parsers.YamlParser;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


@Singleton
public class QuartzConfigManager implements ConfigManager {
    private HashMap<Class<?>, Object> configMap = new HashMap<>();

    @Inject
    private QuartzApplication application;

    public QuartzConfigManager(){
        ParserRegistry.addParser("yaml", new YamlParser());
        ParserRegistry.addParser("toml", new TomlParser());
    }

    @Override
    public Collection<Object> getLoadedConfigs() {
        return configMap.values();
    }

    @Override
    public void reloadAllConfigs() {
        for(Object instance : configMap.values()){
            loadConfig(instance);
        }
    }

    @Override
    public boolean loadConfig(Object config) {
        Config configAnnotation = config.getClass().getAnnotation(Config.class);
        ConfigParser parser = ParserRegistry.getParser(configAnnotation.type());
        if(parser == null){
            application.getSlf4jLogger().error("Did not find config type " + configAnnotation.type());
            return false;
        }
        Path filePath = null;
        for(String ext : parser.getExtensions()){
            String resourcePath = String.format("config/%s.%s", configAnnotation.name(), ext);
            InputStream in = getResource(resourcePath);
            if(in == null) {
                continue;
            }
            filePath = application.getDataFolderPath().resolve(resourcePath);
            File file = filePath.toFile();
            if(!file.exists()){
                if(configAnnotation.persistent() && file.getParentFile().mkdirs()){
                    createResource(filePath, in);
                }
                else {
                    parser.loadConfig(in, config);
                    return false;
                }
            }
        }
        if(filePath == null){
            application.getSlf4jLogger().error("Could not load config " + configAnnotation.name());
            return false;
        }

        try (FileInputStream inputStream = new FileInputStream(filePath.toFile())){
            parser.loadConfig(inputStream, config);
            application.getSlf4jLogger().debug("Loaded config " + configAnnotation.name() + " into " + config.getClass().getSimpleName());
            configMap.put(config.getClass(), config);
            return true;
        }
        catch (IOException e){
            application.getSlf4jLogger().error("(This is probably a bug) Could not find file " + configAnnotation.name());
            return false;
        }
    }

    @Override
    public Object getConfig(Class<?> clazz) {
        return configMap.get(clazz);
    }

    public void createResource(Path path, InputStream inputStream){
        try {
            FileOutputStream outputStream = new FileOutputStream(path.toFile());
            outputStream.write(inputStream.readAllBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InputStream getResource(String path){
        path = "/"+path;
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        return in == null ? getClass().getResourceAsStream(path) : in;
    }

    @Override
    public void writeToFile(Object config) {

    }
}
