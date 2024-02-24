package net.c0ffee1.quartz.core.platform;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Scopes;
import com.google.inject.matcher.Matchers;
import net.c0ffee1.quartz.core.QuartzApplication;
import net.c0ffee1.quartz.core.config.ConfigManager;
import net.c0ffee1.quartz.core.config.ConfigProvisionListener;
import net.c0ffee1.quartz.core.config.QuartzConfigManager;
import net.c0ffee1.quartz.core.platform.loaders.ConfigLoader;
import net.c0ffee1.quartz.core.platform.loaders.ServiceLoader;
import net.c0ffee1.quartz.core.service.PostRegisterTypeListener;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public abstract class CommonPlatformModule<P extends QuartzApplication> extends AbstractModule implements PlatformModule<P>{
    private Reflections reflections;
    private final P application;
    private final Class<P> entryClass;

    public CommonPlatformModule(Class<P> entryClass, P application){
        this.application = application;
        this.entryClass = entryClass;
    }

    @Override
    protected void configure() {
        //@TODO add bindings options
        bind(ConfigManager.class).to(QuartzConfigManager.class);
        ConfigProvisionListener listener = new ConfigProvisionListener();
        requestInjection(listener);

        bindListener(Matchers.any(), listener);
        bind(getEntryClass()).toInstance(application);
        bind(QuartzApplication.class).toInstance(application);
        reflections = new Reflections(getEntryClass().getPackageName());
        init();
        new ServiceLoader(reflections).load(this::registerService);
        new ConfigLoader(reflections).load(this::makeConfigSingleton);
        bindListener(Matchers.any(), new PostRegisterTypeListener());
    }

    @Override
    public Class<P> getEntryClass() {
        return entryClass;
    }

    @Override
    public P getApplication() {
        return application;
    }

    private void registerService(Class<?> serviceClass) {
        bind(serviceClass).asEagerSingleton();
    }

    private void makeConfigSingleton(Class<?> configClass){
        bind(configClass).in(Scopes.SINGLETON);
    }
}
