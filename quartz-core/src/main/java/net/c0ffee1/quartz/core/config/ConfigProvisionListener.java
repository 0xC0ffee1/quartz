package net.c0ffee1.quartz.core.config;

import com.google.inject.Inject;
import com.google.inject.spi.ProvisionListener;
import net.c0ffee1.quartz.core.annotations.Config;

public class ConfigProvisionListener implements ProvisionListener {
    @Inject
    private ConfigManager configManager;

    @Override
    public <T> void onProvision(ProvisionInvocation<T> provision) {
        T instance = provision.provision();
        if (instance.getClass().isAnnotationPresent(Config.class)) {
            configManager.loadConfig(instance);
        }
    }
}
