package net.c0ffee1.quartz.core.bindings;

import net.c0ffee1.quartz.core.config.ConfigManager;


/**
 * Allows you to specify your own implementations of core components.
 */

public interface Bindings {
    Class<? extends ConfigManager> configManager();

    static BindingsBuilder builder(){
        return new QuartzBindings.QuartzBindingsBuilder();
    }

    interface BindingsBuilder{
        Bindings build();

        BindingsBuilder setConfigManager(Class<? extends ConfigManager> configManager);
    }
}
