package net.c0ffee1.quartz.platforms.bukkit;

import net.c0ffee1.quartz.core.Quartz;
import net.c0ffee1.quartz.core.QuartzApplication;
import net.c0ffee1.quartz.core.bindings.Bindings;
import net.c0ffee1.quartz.core.config.ConfigManager;
import net.c0ffee1.quartz.core.config.QuartzConfigManager;
import net.c0ffee1.quartz.platforms.bukkit.config.BukkitConfigManager;
import net.c0ffee1.quartz.platforms.bukkit.platform.BukkitPlatform;

public interface QuartzBukkitPlugin extends QuartzApplication {
    @SuppressWarnings(value = "unchecked")
    default <T extends QuartzBukkitPlugin> boolean initQuartz(Class<T> entryClass){
        BukkitPlatform<T> platform = new BukkitPlatform<>(entryClass, (T) this);
        return Quartz.init(platform);
    }

}
