package net.c0ffee1.quartz.core.platform.loaders;

import net.c0ffee1.quartz.core.annotations.Command;
import net.c0ffee1.quartz.core.annotations.Component;
import net.c0ffee1.quartz.core.annotations.Config;
import net.c0ffee1.quartz.core.annotations.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
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
