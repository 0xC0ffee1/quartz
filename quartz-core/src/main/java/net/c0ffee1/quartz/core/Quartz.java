package net.c0ffee1.quartz.core;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import com.google.inject.util.Modules;
import lombok.Getter;
import lombok.Setter;
import net.c0ffee1.quartz.core.config.ConfigManager;
import net.c0ffee1.quartz.core.config.QuartzConfigManager;
import net.c0ffee1.quartz.core.platform.PlatformModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public final class Quartz {
    @Getter
    private static final Map<Class<?>, Object> bindings = new ConcurrentHashMap<>();

    public static boolean init(PlatformModule<?> module){
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(module.getApplication());
        module.getImplementations().forEach((k,v) -> bindings.put(k, injector.getInstance(k)));
        return true;
    }

    public static void putBinding(Object o){
        bindings.put(o.getClass(), o);
    }

    public static Object getBinding(Class<?> clazz){
        return bindings.get(clazz);
    }
}
