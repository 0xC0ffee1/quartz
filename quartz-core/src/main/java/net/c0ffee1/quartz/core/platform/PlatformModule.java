package net.c0ffee1.quartz.core.platform;

import com.google.inject.Module;
import net.c0ffee1.quartz.core.QuartzApplication;

public interface PlatformModule<P extends QuartzApplication> extends Module {
    boolean init();
    void registerServices();

    Class<?> getEntryPoint();

    QuartzApplication getApplication();
}
