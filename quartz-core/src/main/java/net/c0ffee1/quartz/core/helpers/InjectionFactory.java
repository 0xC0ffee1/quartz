package net.c0ffee1.quartz.core.helpers;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class InjectionFactory {
    private final Injector injector;

    @Inject
    public InjectionFactory(Injector injector) {
        this.injector = injector;
    }

    public <T> T createInstance(Class<T> clazz) {
        return injector.getInstance(clazz);
    }
}