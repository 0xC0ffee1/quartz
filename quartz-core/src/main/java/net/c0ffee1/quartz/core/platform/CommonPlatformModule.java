package net.c0ffee1.quartz.core.platform;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.matcher.Matchers;
import lombok.Getter;
import net.c0ffee1.quartz.core.bindings.Bindings;
import net.c0ffee1.quartz.core.QuartzApplication;
import net.c0ffee1.quartz.core.config.ConfigManager;
import net.c0ffee1.quartz.core.config.ConfigProvisionListener;
import net.c0ffee1.quartz.core.platform.loaders.ConfigLoader;
import net.c0ffee1.quartz.core.platform.loaders.ServiceLoader;
import net.c0ffee1.quartz.core.service.ServiceProvisionListener;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;


public abstract class CommonPlatformModule<P extends QuartzApplication> extends AbstractModule implements PlatformModule<P>{
    @Getter
    private Reflections reflections;
    private final P application;
    private final Class<P> entryClass;
    private final Bindings bindings;



    public CommonPlatformModule(Class<P> entryClass, P application){
        this(entryClass, application, null);
    }

    public CommonPlatformModule(Class<P> entryClass, P application, @Nullable Bindings bindings){
        this.application = application;
        this.entryClass = entryClass;
        this.bindings = bindings == null ? Bindings.builder().build() : bindings;
    }


    @Override
    protected void configure() {
        bind(ConfigManager.class).to(bindings.configManager());
        ConfigProvisionListener listener = new ConfigProvisionListener();
        requestInjection(listener);
        requestInjection(this);

        bindListener(Matchers.any(), listener);
        bind(getEntryClass()).toInstance(application);
        bind(QuartzApplication.class).toInstance(application);
        reflections = new Reflections(getEntryClass().getPackageName());
        init();
        //@TODO add dynamic loaders and refactor load method to be internal, pass module in method
        new ServiceLoader(reflections).load(this::registerService);
        new ConfigLoader(reflections).load(this::makeConfigSingleton);
        ServiceProvisionListener serviceProvision = new ServiceProvisionListener();
        requestInjection(serviceProvision);
        bindListener(Matchers.any(), serviceProvision);
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
