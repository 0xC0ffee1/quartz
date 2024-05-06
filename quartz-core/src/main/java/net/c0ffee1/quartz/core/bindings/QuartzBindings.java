package net.c0ffee1.quartz.core.bindings;

import net.c0ffee1.quartz.core.config.ConfigManager;
import net.c0ffee1.quartz.core.config.QuartzConfigManager;
import net.c0ffee1.quartz.core.service.CommonServiceManager;
import net.c0ffee1.quartz.core.service.ServiceManager;

public record QuartzBindings(Class<? extends ConfigManager> configManager,
                             Class<? extends ServiceManager> serviceManager) implements Bindings{
    public static class QuartzBindingsBuilder implements BindingsBuilder{
        private Class<? extends ConfigManager> configManager = QuartzConfigManager.class;
        private Class<? extends ServiceManager> serviceManager = CommonServiceManager.class;
        public QuartzBindingsBuilder setConfigManager(Class<? extends ConfigManager> configManager) {
            this.configManager = configManager;
            return this;
        }

        public QuartzBindingsBuilder setServiceManager(Class<? extends ServiceManager> serviceManager) {
            this.serviceManager = serviceManager;
            return this;
        }

        public Bindings build() {
            return new QuartzBindings(configManager, serviceManager);
        }
    }
}
