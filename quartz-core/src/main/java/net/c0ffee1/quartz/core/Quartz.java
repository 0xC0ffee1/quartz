package net.c0ffee1.quartz.core;

import com.google.inject.Guice;
import com.google.inject.Injector;

import lombok.Getter;
import lombok.Setter;
import net.c0ffee1.quartz.core.config.ConfigManager;
import net.c0ffee1.quartz.core.config.QuartzConfigManager;
import net.c0ffee1.quartz.core.platform.PlatformModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

public final class Quartz {
    private static Injector injector;

    public static boolean init(PlatformModule<?> module){
        injector = Guice.createInjector(module);
        injector.injectMembers(module.getApplication());
        return true; //TODO add checks for failure
    }
//
//    /**
//     * Sets the global {@link ConfigManager} instance.
//     * <p>
//     * This method should be called to initialize the configuration management
//     * system before any call to {@link #getConfigManager()}.
//     *
//     * @param manager The {@link ConfigManager} instance to be used globally.
//     */
//    public static void setConfigManager(ConfigManager manager) {
//        configManagerRef.set(manager);
//    }
//
//    /**
//     * Retrieves the global {@link ConfigManager} instance.
//     * <p>
//     * DO NOT use this method directly. Always inject it using @Inject!
//     *
//     * @return The global {@link ConfigManager} instance or null if not set.
//     */
//    public static @Nullable ConfigManager getConfigManager() throws IllegalStateException {
//        ConfigManager value = configManagerRef.get();
//        return value;
//    }
}
