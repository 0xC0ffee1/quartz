package net.c0ffee1.quartz.core.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.c0ffee1.quartz.core.QuartzApplication;
import net.c0ffee1.quartz.core.annotations.Config;
import net.c0ffee1.quartz.core.annotations.OnConfigReload;
import net.c0ffee1.quartz.core.annotations.UsesConfig;
import net.c0ffee1.quartz.core.config.nodes.ConfigNode;
import net.c0ffee1.quartz.core.config.parsers.ConfigParser;
import net.c0ffee1.quartz.core.config.parsers.ParserRegistry;
import net.c0ffee1.quartz.core.config.parsers.TomlParser;
import net.c0ffee1.quartz.core.config.parsers.YamlParser;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;


@Singleton
public class QuartzConfigManager implements ConfigManager {
    protected final HashMap<Class<?>, Object> configMap = new HashMap<>();
    protected final HashMap<Class<?>, HashSet<Object>> configListeners = new HashMap<>();

    protected final HashMap<String, ConfigNode> cache = new HashMap<>();

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
        cache.clear();
        for(Object instance : configMap.values()){
            loadConfig(instance);
        }
    }

    private void callListeners(Class<?> configClass){
        HashSet<Object> listeners = configListeners.get(configClass);
        if(listeners == null) return;
        for(Object o : listeners){
            for (Method method : o.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(OnConfigReload.class)) continue;
                try {
                    method.setAccessible(true);
                    if (method.getParameterCount() == 0) {
                        // Invoke the method on the object
                        method.invoke(o);
                    } else {
                        // Handle or log methods that require parameters
                        application.getSlf4jLogger().info("Method " + method.getName() + " in " + o.getClass().getSimpleName()
                                + " has parameters. @OnConfigLoad methods should not have parameters.");
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ConfigNode getTree(ConfigParser parser, Path path, InputStream inputStream){
        if(cache.containsKey(path.toString())){
            return cache.get(path.toString());
        }
        else{
            ConfigNode tree = parser.getConfigTree(inputStream);
            cache.put(path.toString(), tree);
            return tree;
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
            String resourcePath = String.format("config/%s.%s", configAnnotation.file(), ext);
            InputStream in = getResource(resourcePath);
            if(in == null) {
                application.getSlf4jLogger().error("Resource path was null " + resourcePath);
                continue;
            }
            filePath = application.getDataFolderPath().resolve(resourcePath);
            File file = filePath.toFile();
            if(!file.exists()){
                if(configAnnotation.persistent() && file.getParentFile().mkdirs()){
                    createResource(filePath, in);
                }
                else {

                    ConfigNode tree = getTree(parser, filePath, in);
                    cache.put(filePath.toString(), tree);
                    ConfigNode configNode = tree.path(configAnnotation.node());
                    parser.loadConfig(configNode, config);

                    configMap.put(config.getClass(), config);
                    callListeners(config.getClass());
                    return false;
                }
            }
            else break;
        }


        if(filePath == null){
            application.getSlf4jLogger().info(application.getDataFolderPath().toString());
            application.getSlf4jLogger().error("Could not load config " + configAnnotation.node());
            return false;
        }

        try (FileInputStream inputStream = new FileInputStream(filePath.toFile())){
            ConfigNode tree = getTree(parser, filePath, inputStream);
            cache.put(filePath.toString(), tree);
            ConfigNode configNode = tree.path(configAnnotation.node());
            parser.loadConfig(configNode, config);

            application.getSlf4jLogger().debug("Loaded config " + configAnnotation.node() + " into " + config.getClass().getSimpleName());
            configMap.put(config.getClass(), config);
            callListeners(config.getClass());
            return true;
        }
        catch (IOException e){
            application.getSlf4jLogger().error("(This is probably a bug) Could not find file " + configAnnotation.node());
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
        InputStream in = application.getClass().getResourceAsStream(path);
        return in == null ? getClass().getResourceAsStream(path) : in;
    }

    @Override
    public void writeToFile(Object config) {

    }

    @Override
    public void registerListener(Object listener) {
        if(!listener.getClass().isAnnotationPresent(UsesConfig.class)){
            throw new IllegalStateException(listener.getClass().getSimpleName() + " must be annotated with UsesConfig");
        }
        UsesConfig usesConfig = listener.getClass().getAnnotation(UsesConfig.class);
        for(Class<?> configClass : usesConfig.value()){
            if(!configListeners.containsKey(configClass)){
                configListeners.put(configClass, new HashSet<>());
            }
            configListeners.get(configClass).add(listener);
        }
    }
}
