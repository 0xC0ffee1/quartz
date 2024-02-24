package net.c0ffee1.quartz.core.service;

import com.google.inject.spi.InjectionListener;
import net.c0ffee1.quartz.core.annotations.PostRegister;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PostInjectionEventListener <I> implements InjectionListener<I> {
    @Override
    public void afterInjection(I injectee) {
        for (Method method : injectee.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostRegister.class)) {
                method.setAccessible(true);
                try {
                    method.invoke(injectee);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Failed to execute @PostRegister method", e);
                }
            }
        }
    }
}
