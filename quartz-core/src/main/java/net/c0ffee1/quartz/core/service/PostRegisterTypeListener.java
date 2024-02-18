package net.c0ffee1.quartz.core.service;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import net.c0ffee1.quartz.core.annotations.PostRegister;


import java.lang.reflect.Method;

public class PostRegisterTypeListener implements TypeListener {
    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        for (Method method : type.getRawType().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostRegister.class)) {
                encounter.register(new PostInjectionEventListener<>());
                break;
            }
        }
    }
}
