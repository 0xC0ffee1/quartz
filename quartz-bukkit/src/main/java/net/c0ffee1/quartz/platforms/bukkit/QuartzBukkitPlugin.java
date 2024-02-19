package net.c0ffee1.quartz.platforms.bukkit;

import net.c0ffee1.quartz.core.Quartz;
import net.c0ffee1.quartz.core.QuartzApplication;

public interface QuartzBukkitPlugin extends QuartzApplication {
    @SuppressWarnings(value = "unchecked")
    default <T extends QuartzBukkitPlugin> boolean initQuartz(Class<T> entryClass){
        return Quartz.init(new BukkitPlatform<T>((T) this, entryClass));
    }
}
