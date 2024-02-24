package net.c0ffee1.quartz.core.platform.loaders;

import net.c0ffee1.quartz.core.annotations.Command;
import net.c0ffee1.quartz.core.annotations.Component;
import net.c0ffee1.quartz.core.annotations.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ServiceLoader extends AbstractLoader<Class<?>> {
    public ServiceLoader(Reflections reflections) {
        super(reflections);
    }

    @Override
    public void load(Consumer<Class<?>> consumer) {
        getAutoRegistrable(Service.class, Component.class, Command.class).forEach(consumer);
    }

    @SafeVarargs
    private Set<Class<?>> getAutoRegistrable(Class<? extends Annotation>... annotations){
        Set<Class<?>> set = new HashSet<>();
        for(Class<? extends Annotation> annotation : annotations){
            set.addAll(getReflections().getTypesAnnotatedWith(annotation));
        }
        return set;
    }
}
