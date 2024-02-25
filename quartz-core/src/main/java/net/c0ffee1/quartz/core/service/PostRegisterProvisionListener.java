package net.c0ffee1.quartz.core.service;

import com.google.inject.spi.ProvisionListener;
import net.c0ffee1.quartz.core.annotations.PostRegister;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PostRegisterProvisionListener implements ProvisionListener {
    @Override
    public <T> void onProvision(ProvisionInvocation<T> provision) {
        T instance = provision.provision();
        for (Method method : instance.getClass().getMethods()) {
            if (method.isAnnotationPresent(PostRegister.class)) {
                method.setAccessible(true);
                try {
                    method.invoke(instance);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to execute @PostRegister method", e);
                }
                break;
            }
        }
    }
}