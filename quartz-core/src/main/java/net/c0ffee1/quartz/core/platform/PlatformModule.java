package net.c0ffee1.quartz.core.platform;

import com.google.inject.Module;
import net.c0ffee1.quartz.core.QuartzApplication;
import net.c0ffee1.quartz.core.config.ConfigManager;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface PlatformModule<P extends QuartzApplication> extends Module {
    boolean init();
    Class<P> getEntryClass();
    QuartzApplication getApplication();

    Map<Class<?>, Class<?>> getImplementations();

    Class<? extends Annotation>[] getServiceAnnotations();
}
