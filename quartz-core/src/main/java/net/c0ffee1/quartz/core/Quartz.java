package net.c0ffee1.quartz.core;

import com.google.inject.Guice;
import com.google.inject.Injector;

import net.c0ffee1.quartz.core.platform.PlatformModule;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Quartz {
    private static Injector injector;
    public static boolean init(PlatformModule<? extends QuartzApplication> platform){
        injector = Guice.createInjector(platform);
        injector.injectMembers(platform);
        injector.injectMembers(platform.getApplication());
        return true; //TODO add checks for failure
    }
}
