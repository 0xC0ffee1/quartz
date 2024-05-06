package net.c0ffee1.quartz.core.config.parsers;

import net.c0ffee1.quartz.core.config.nodes.ConfigNode;

import java.io.InputStream;
import java.nio.file.Path;

public interface ConfigParser {
    ConfigNode getConfigTree(InputStream inputStream);
    boolean loadConfig(ConfigNode node, Object instance);

    boolean writeConfig(Path path, Object configInstance);

    String[] getExtensions();
}
