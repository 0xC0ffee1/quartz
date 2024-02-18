package net.c0ffee1.quartz.platforms.bukkit;

import net.c0ffee1.quartz.core.Quartz;
import net.c0ffee1.quartz.core.QuartzApplication;
import org.bukkit.plugin.java.JavaPlugin;

public interface QuartzBukkitPlugin extends QuartzApplication {
    default boolean startQuartz(){
        Quartz.init(new BukkitPlatform(this, getClass()));
        return true;
    }
}
