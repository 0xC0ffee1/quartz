package net.c0ffee1.quartz.core.bindings;

import net.c0ffee1.quartz.core.config.ConfigManager;
import net.c0ffee1.quartz.core.config.QuartzConfigManager;

public record QuartzBindings(Class<? extends ConfigManager> configManager) implements Bindings{
    public static class QuartzBindingsBuilder implements BindingsBuilder{
        private Class<? extends ConfigManager> configManager = QuartzConfigManager.class;
        public QuartzBindingsBuilder setConfigManager(Class<? extends ConfigManager> configManager) {
            this.configManager = configManager;
            return this;
        }

        public Bindings build() {
            return new QuartzBindings(configManager);
        }
    }
}
