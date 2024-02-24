package net.c0ffee1.quartz.platforms.bukkit;

import net.c0ffee1.quartz.core.QuartzApplication;
import net.c0ffee1.quartz.core.platform.CommonPlatformModule;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlatform<P extends QuartzApplication> extends CommonPlatformModule<P> {
    public BukkitPlatform(Class<P> entryClass, P application) {
        super(entryClass, application);
    }

    @Override
    public boolean init() {
        if(getApplication() instanceof JavaPlugin) {
            bind(JavaPlugin.class).toInstance((JavaPlugin) getApplication());
        }
        return true;
    }
}
