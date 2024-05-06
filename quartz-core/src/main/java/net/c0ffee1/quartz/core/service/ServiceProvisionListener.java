package net.c0ffee1.quartz.core.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.spi.ProvisionListener;
import net.c0ffee1.quartz.core.annotations.Component;
import net.c0ffee1.quartz.core.annotations.PostRegister;
import net.c0ffee1.quartz.core.annotations.Service;
import net.c0ffee1.quartz.core.annotations.UsesConfig;
import net.c0ffee1.quartz.core.config.ConfigManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ServiceProvisionListener implements ProvisionListener {
    private final Class<? extends Annotation>[] annotations;
    private final Provider<ServiceManager> provider;

    public ServiceProvisionListener(Provider<ServiceManager> provider, Class<? extends Annotation>[] annotations) {
        this.annotations = annotations;
        this.provider = provider;
    }

    @Override
    public <T> void onProvision(ProvisionInvocation<T> provision) {
        T instance = provision.provision();


        boolean any = false;
        for(Class<? extends Annotation> anno : annotations){
            if(instance.getClass().isAnnotationPresent(anno)){
                any = true;
                break;
            }
        }
        if(!any) return;

        provider.get().registerService(instance);
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