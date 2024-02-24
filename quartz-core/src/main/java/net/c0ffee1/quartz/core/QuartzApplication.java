package net.c0ffee1.quartz.core;

import net.c0ffee1.quartz.core.config.ConfigManager;
import org.slf4j.Logger;

import java.nio.file.Path;

public interface QuartzApplication {

    Path getDataFolderPath();
    ConfigManager createConfigManager();
    Logger getSlf4jLogger();
}
