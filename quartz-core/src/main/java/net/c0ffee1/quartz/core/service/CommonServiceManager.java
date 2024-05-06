package net.c0ffee1.quartz.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.c0ffee1.quartz.core.QuartzApplication;
import net.c0ffee1.quartz.core.annotations.OnUnregister;
import net.c0ffee1.quartz.core.annotations.PostRegister;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Singleton
public class CommonServiceManager implements ServiceManager{
    private final Map<Class<?>, Object> map = new ConcurrentHashMap<>();
    private final AtomicBoolean shuttingDown = new AtomicBoolean(false);

    @Inject
    private QuartzApplication application;

    private void callUnregister(Object o){
        if(shuttingDown.get()){
            application.getSlf4jLogger().error("ServiceManager#callUnregister called again after being already called!");
            return;
        }
        shuttingDown.set(true);
        for (Method method : o.getClass().getMethods()) {
            if (method.isAnnotationPresent(OnUnregister.class)) {
                method.setAccessible(true);
                try {
                    method.invoke(o);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to execute @OnUnregister method", e);
                }
            }
        }
    }

    @Override
    public void signalShutdown() {
        for(Object o : map.values()){
            callUnregister(o);
        }
    }

    @Override
    public void registerService(Object o) {
        map.put(o.getClass(), o);
    }
}
