package net.c0ffee1.quartz.core.platform.loaders;


import net.c0ffee1.quartz.core.annotations.Config;

import org.reflections.Reflections;

import java.util.function.Consumer;

public class ConfigLoader extends AbstractLoader<Class<?>> {
    public ConfigLoader(Reflections reflections) {
        super(reflections);
    }

    @Override
    public void load(Consumer<Class<?>> consumer) {
        getReflections().getTypesAnnotatedWith(Config.class).forEach(consumer);
    }
}
