package net.c0ffee1.quartz.core.config;

import java.util.Collection;
import java.util.List;

public interface ConfigManager {
    Collection<Object> getLoadedConfigs();
    void reloadAllConfigs();
    boolean loadConfig(Object config);

    Object getConfig(Class<?> clazz);

    void writeToFile(Object config);
}
