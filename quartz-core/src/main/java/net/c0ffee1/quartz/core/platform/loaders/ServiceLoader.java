package net.c0ffee1.quartz.core.platform.loaders;

import net.c0ffee1.quartz.core.annotations.Command;
import net.c0ffee1.quartz.core.annotations.Component;
import net.c0ffee1.quartz.core.annotations.Service;
import net.c0ffee1.quartz.core.service.ServicePriority;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ServiceLoader extends AbstractLoader<Class<?>> {
    private final Class<? extends Annotation>[] annotations;
    public ServiceLoader(Reflections reflections, Class<? extends Annotation>[] annotations) {
        super(reflections);
        this.annotations = annotations;
    }

    @Override
    public void load(Consumer<Class<?>> consumer) {
        getAutoRegistrable(annotations).forEach(consumer);
    }

    @SafeVarargs
    private Collection<Class<?>> getAutoRegistrable(Class<? extends Annotation>... annotations){
        Set<Class<?>> set = new HashSet<>();
        for (Class<? extends Annotation> annotation : annotations) {
            set.addAll(getReflections().getTypesAnnotatedWith(annotation));
        }

        return set.stream()
                .filter(clazz -> Arrays.stream(annotations).anyMatch(clazz::isAnnotationPresent))
                .sorted(Comparator.comparingInt(clazz -> getPriority(clazz, annotations).ordinal()))
                .collect(Collectors.toList());
    }

    // Helper method to get priority from annotations
    private ServicePriority getPriority(Class<?> clazz, Class<? extends Annotation>[] annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            if (clazz.isAnnotationPresent(annotation)) {
                try {
                    Annotation ann = clazz.getAnnotation(annotation);
                    Method priorityMethod = annotation.getDeclaredMethod("priority");
                    return (ServicePriority) priorityMethod.invoke(ann);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    return ServicePriority.NORMAL;
                }
            }
        }
        return ServicePriority.NORMAL; // Default priority if none is found or an error occurs
    }
}
