package net.c0ffee1.quartz.platforms.bukkit.platform;

import com.google.inject.matcher.Matchers;
import net.c0ffee1.quartz.core.QuartzApplication;
import net.c0ffee1.quartz.core.bindings.Bindings;
import net.c0ffee1.quartz.core.config.parsers.ParserRegistry;
import net.c0ffee1.quartz.core.config.parsers.TomlParser;
import net.c0ffee1.quartz.core.config.parsers.YamlParser;
import net.c0ffee1.quartz.core.platform.CommonPlatformModule;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlatform<P extends QuartzApplication> extends CommonPlatformModule<P> {
    public BukkitPlatform(Class<P> entryClass, P application) {
        super(entryClass, application);
    }
    public BukkitPlatform(Class<P> entryClass, P application, Bindings bindings) {
        super(entryClass, application, bindings);
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
