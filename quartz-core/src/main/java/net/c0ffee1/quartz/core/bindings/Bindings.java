package net.c0ffee1.quartz.core.bindings;

import net.c0ffee1.quartz.core.config.ConfigManager;
import net.c0ffee1.quartz.core.service.ServiceManager;

import java.util.Map;


/**
 * Allows you to specify your own implementations of core components.
 */

public interface Bindings {
    Class<? extends ConfigManager> configManager();

    Class<? extends ServiceManager> serviceManager();


    static BindingsBuilder builder(){
        return new QuartzBindings.QuartzBindingsBuilder();
    }

    interface BindingsBuilder{
        Bindings build();

        BindingsBuilder setConfigManager(Class<? extends ConfigManager> configManager);
        BindingsBuilder setServiceManager(Class<? extends ServiceManager> serviceManager);

    }
}
