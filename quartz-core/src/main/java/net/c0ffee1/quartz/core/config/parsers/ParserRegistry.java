package net.c0ffee1.quartz.core.config.parsers;

import java.util.concurrent.ConcurrentHashMap;

public abstract class ParserRegistry {
    private static final ConcurrentHashMap<String, ConfigParser> parsers = new ConcurrentHashMap<>();

    public static void addParser(String key, ConfigParser parser){
        parsers.put(key, parser);
    }

    public static ConfigParser getParser(String key){
        return parsers.get(key);
    }
}
