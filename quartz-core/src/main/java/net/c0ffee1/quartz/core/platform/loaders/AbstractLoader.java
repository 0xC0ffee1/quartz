package net.c0ffee1.quartz.core.platform.loaders;

import org.reflections.Reflections;

public abstract class AbstractLoader<T> implements Loader<T> {
    private Reflections reflections;

    public AbstractLoader(Reflections reflections){
        this.reflections = reflections;
    }

    public Reflections getReflections() {
        return reflections;
    }
}
