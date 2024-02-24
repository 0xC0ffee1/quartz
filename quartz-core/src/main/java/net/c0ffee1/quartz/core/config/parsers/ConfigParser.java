package net.c0ffee1.quartz.core.config.parsers;

import java.io.InputStream;
import java.nio.file.Path;

public interface ConfigParser {
    boolean loadConfig(InputStream inputStream, Object configInstance);
    boolean writeConfig(Path path, Object configInstance);

    String[] getExtensions();
}
