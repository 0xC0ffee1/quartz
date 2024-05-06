package net.c0ffee1.quartz.core.platform;

import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import lombok.Getter;
import net.c0ffee1.quartz.core.Quartz;
import net.c0ffee1.quartz.core.annotations.Bind;
import net.c0ffee1.quartz.core.annotations.Component;
import net.c0ffee1.quartz.core.annotations.From;
import net.c0ffee1.quartz.core.annotations.Service;
import net.c0ffee1.quartz.core.bindings.Bindings;
import net.c0ffee1.quartz.core.QuartzApplication;
import net.c0ffee1.quartz.core.config.ConfigManager;
import net.c0ffee1.quartz.core.config.ConfigProvisionListener;
import net.c0ffee1.quartz.core.platform.loaders.BindLoader;
import net.c0ffee1.quartz.core.platform.loaders.ConfigLoader;
import net.c0ffee1.quartz.core.platform.loaders.ServiceLoader;
import net.c0ffee1.quartz.core.service.ServiceManager;
import net.c0ffee1.quartz.core.service.ServiceProvisionListener;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public abstract class CommonPlatformModule<P extends QuartzApplication> extends AbstractModule implements PlatformModule<P> {
    @Getter
    protected Reflections reflections;
    private final P application;
    private final Class<P> entryClass;
    private final Bindings bindings;

    private final Map<Class<?>, Class<?>> implementations = new HashMap<>();

    public CommonPlatformModule(Class<P> entryClass, P application){
        this(entryClass, application, null);
    }

    public CommonPlatformModule(Class<P> entryClass, P application, @Nullable Bindings bindings){
        this.application = application;
        this.entryClass = entryClass;
        this.bindings = bindings == null ? Bindings.builder().build() : bindings;
        initReflections();
    }

    public void initReflections(){
        ConfigurationBuilder config = new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(getEntryClass().getPackageName(), this.getEntryClass().getClassLoader()))
                .setScanners(Scanners.TypesAnnotated, Scanners.SubTypes);
        this.reflections = new Reflections(config);
    }

    @Override
    public Class<? extends Annotation>[] getServiceAnnotations() {
        return new Class[]{Service.class, Component.class};
    }

    @Override
    protected void configure() {
        bind(ConfigManager.class).to(bindings.configManager());
        bind(ServiceManager.class).to(bindings.serviceManager());

        //@TODO causes binded classes annotated with @RegisterEvents to fire twice
        Quartz.getBindings().forEach((k,v) -> bind((Class) k).toInstance(v));

        ServiceProvisionListener serviceListener = new ServiceProvisionListener(getProvider(ServiceManager.class),
                getServiceAnnotations());
        bindListener(Matchers.any(), serviceListener);
        bindListener(Matchers.any(), new ConfigProvisionListener(getProvider(ConfigManager.class)));

        bind(getEntryClass()).toInstance(application);
        bind(QuartzApplication.class).toInstance(application);
        init();
        //@TODO add dynamic loaders and refactor load method to be internal, pass module in method
        new ServiceLoader(reflections, getServiceAnnotations()).load(this::registerService);
        new ConfigLoader(reflections).load(this::makeConfigSingleton);
        new BindLoader(reflections).load(this::bindInterface);
    }


    @Override
    public Map<Class<?>, Class<?>> getImplementations() {
        return implementations;
    }

    @Override
    public Class<P> getEntryClass() {
        return entryClass;
    }

    @Override
    public P getApplication() {
        return application;
    }

    protected void registerService(Class<?> serviceClass) {
        bind(serviceClass).asEagerSingleton();
    }

    protected void bindInterface(Class<?> implClass) {
        Bind bindMeta = implClass.getAnnotation(Bind.class);
        Class<?> iClass = bindMeta.value();
        bind((Class) iClass).to(implClass);
        implementations.put(iClass, implClass);
    }

    protected void makeConfigSingleton(Class<?> configClass){
        bind(configClass).in(Scopes.SINGLETON);
    }
}
