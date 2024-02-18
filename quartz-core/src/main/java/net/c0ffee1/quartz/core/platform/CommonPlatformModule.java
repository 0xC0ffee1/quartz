package net.c0ffee1.quartz.core.platform;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import net.c0ffee1.quartz.core.QuartzApplication;
import net.c0ffee1.quartz.core.annotations.Command;
import net.c0ffee1.quartz.core.annotations.Component;
import net.c0ffee1.quartz.core.annotations.Service;
import net.c0ffee1.quartz.core.service.PostRegisterTypeListener;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;


public abstract class CommonPlatformModule<P extends QuartzApplication> extends AbstractModule implements PlatformModule<P>{
    private Reflections reflections;
    private ConfigurationBuilder configurationBuilder;

    public CommonPlatformModule(ConfigurationBuilder configurationBuilder){
        this.configurationBuilder = configurationBuilder;
    }

    public CommonPlatformModule(){

    }

    @Override
    public void registerServices() {
        reflections = configurationBuilder != null ? new Reflections(configurationBuilder) :
                new Reflections(getEntryPoint().getPackageName());
        getAutoRegistrable(Service.class, Component.class, Command.class).forEach(this::registerService);
        bindListener(Matchers.any(), new PostRegisterTypeListener());
    }

    @Override
    protected void configure() {
        init();
    }

    @SafeVarargs
    private Set<Class<?>> getAutoRegistrable(Class<? extends Annotation>... annotations){
        Set<Class<?>> set = new HashSet<>();
        for(Class<? extends Annotation> annotation : annotations){
            set.addAll(getReflections().getTypesAnnotatedWith(annotation));
        }
        return set;
    }

    public Reflections getReflections() {
        return reflections;
    }

    private void registerService(Class<?> serviceClass) {
        bind(serviceClass).asEagerSingleton();
    }
}
