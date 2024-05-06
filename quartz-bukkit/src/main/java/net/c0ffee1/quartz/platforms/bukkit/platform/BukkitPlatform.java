package net.c0ffee1.quartz.platforms.bukkit.platform;

import com.google.inject.matcher.Matchers;
import net.c0ffee1.quartz.core.QuartzApplication;
import net.c0ffee1.quartz.core.bindings.Bindings;
import net.c0ffee1.quartz.core.config.parsers.ParserRegistry;
import net.c0ffee1.quartz.core.config.parsers.TomlParser;
import net.c0ffee1.quartz.core.config.parsers.YamlParser;
import net.c0ffee1.quartz.core.platform.CommonPlatformModule;
import net.c0ffee1.quartz.platforms.bukkit.config.BukkitConfigManager;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;

public class BukkitPlatform<P extends QuartzApplication> extends CommonPlatformModule<P> {
    public BukkitPlatform(Class<P> entryClass, P application) {
        super(entryClass, application, Bindings.builder()
                .setConfigManager(BukkitConfigManager.class)
                .build());
    }
    public BukkitPlatform(Class<P> entryClass, P application, Bindings bindings) {
        super(entryClass, application, bindings);
    }
    public void onDisable(PluginDisableEvent event){

    }

    @Override
    public boolean init() {
        if(getApplication() instanceof JavaPlugin javaPlugin) {
            bind(JavaPlugin.class).toInstance(javaPlugin);
            bindListener(Matchers.any(), new ListenerProvisionListener(javaPlugin));
        }
        ParserRegistry.addParser("toml-bukkit", new TomlParser());
        ParserRegistry.addParser("yaml-bukkit", new YamlParser());
        return true;
    }
}
