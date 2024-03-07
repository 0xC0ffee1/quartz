package net.c0ffee1.quartz.core.service;

import com.google.inject.Inject;
import com.google.inject.spi.ProvisionListener;
import net.c0ffee1.quartz.core.annotations.PostRegister;
import net.c0ffee1.quartz.core.annotations.UsesConfig;
import net.c0ffee1.quartz.core.config.ConfigManager;

import java.lang.reflect.Method;

public class ServiceProvisionListener implements ProvisionListener {

    @Inject
    private ConfigManager configManager;

    @Override
    public <T> void onProvision(ProvisionInvocation<T> provision) {
        T instance = provision.provision();
        if(instance.getClass().isAnnotationPresent(UsesConfig.class)){
            configManager.registerListener(instance);
        }
        for (Method method : instance.getClass().getMethods()) {
            if (method.isAnnotationPresent(PostRegister.class)) {
                method.setAccessible(true);
                try {
                    method.invoke(instance);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to execute @PostRegister method", e);
                }
            }
        }

    }
}