package net.c0ffee1.quartz.core.config;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.spi.ProvisionListener;
import net.c0ffee1.quartz.core.annotations.Config;

public class ConfigProvisionListener implements ProvisionListener {

    private final Provider<ConfigManager> configManagerProvider;

    public ConfigProvisionListener(Provider<ConfigManager> configManagerProvider) {
        this.configManagerProvider = configManagerProvider;
    }

    @Override
    public <T> void onProvision(ProvisionInvocation<T> provision) {
        T instance = provision.provision();
        if (instance.getClass().isAnnotationPresent(Config.class)) {
            configManagerProvider.get().loadConfig(instance);
        }
    }
}
