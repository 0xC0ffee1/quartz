package net.c0ffee1.quartz.platforms.bukkit.config;

import com.google.inject.Singleton;
import net.c0ffee1.quartz.core.annotations.Config;
import net.c0ffee1.quartz.core.config.QuartzConfigManager;
import net.c0ffee1.quartz.core.config.parsers.AbstractJacksonParser;
import net.c0ffee1.quartz.core.config.parsers.ConfigParser;
import net.c0ffee1.quartz.core.config.parsers.ParserRegistry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
@Singleton
public class BukkitConfigManager extends QuartzConfigManager {
    private HashMap<Class<?>, ConfigurationSection> cachedSections = new HashMap<>();

    public @Nullable ConfigurationSection getBukkitSection(Class<?> clazz){
        return cachedSections.get(clazz);
    }

    @Override
    public boolean loadConfig(Object config) {
        super.loadConfig(config);

        Config configAnnotation = config.getClass().getAnnotation(Config.class);
        ConfigParser parser = ParserRegistry.getParser(configAnnotation.type());

        if(parser instanceof AbstractJacksonParser jacksonParser){
            JacksonConfigurationSection section = new JacksonConfigurationSection(jacksonParser.getMapper());
            try {
                section.loadFromString(jacksonParser.getConfigAsString(config));
            } catch (InvalidConfigurationException e) {
                throw new RuntimeException("Invalid config " + config.getClass().getSimpleName() + ":" +
                        parser.getClass().getSimpleName(), e);
            }
            cachedSections.put(config.getClass(), section);
        }

        return true;
    }
}
