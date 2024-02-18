package bukkit;

import net.c0ffee1.quartz.core.Quartz;
import net.c0ffee1.quartz.platforms.bukkit.BukkitPlatform;
import net.c0ffee1.quartz.platforms.bukkit.QuartzBukkitPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;

public class TestBukkitPlugin extends JavaPlugin implements QuartzBukkitPlugin {

    @Override
    public void onEnable() {
        ConfigurationBuilder builder = new ConfigurationBuilder().forPackage(getClass().getPackageName(), getClassLoader());
        Quartz.init(new BukkitPlatform<>(builder, this, TestBukkitPlugin.class));
    }

    @Override
    public @NotNull Logger getSLF4JLogger() {
        return super.getSLF4JLogger();
    }
}
