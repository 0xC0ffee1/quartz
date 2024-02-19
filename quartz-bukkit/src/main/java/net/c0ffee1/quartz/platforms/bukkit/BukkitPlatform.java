package net.c0ffee1.quartz.platforms.bukkit;

import net.c0ffee1.quartz.core.Quartz;
import net.c0ffee1.quartz.core.QuartzApplication;
import net.c0ffee1.quartz.core.platform.CommonPlatformModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.util.ConfigurationBuilder;

public class BukkitPlatform<P extends QuartzBukkitPlugin> extends CommonPlatformModule<P> {
    private final P app;
    private final Class<P> pluginClass;

    public BukkitPlatform(P instance, Class<P> pluginClass){
        this.app = instance;
        this.pluginClass = pluginClass;
    }

    public BukkitPlatform(ConfigurationBuilder configBuilder, P instance, Class<P> pluginClass){
        super(configBuilder);
        this.app = instance;
        this.pluginClass = pluginClass;
    }

    @Override
    public QuartzApplication getApplication() {
        return app;
    }

    @Override
    public boolean init() {
        registerServices();
        if(app instanceof JavaPlugin) {
            bind(JavaPlugin.class).toInstance((JavaPlugin) app);
        }
        bind(pluginClass).toInstance(app);
        return true;
    }

    @Override
    public Class<?> getEntryPoint() {
        return pluginClass;
    }
}
