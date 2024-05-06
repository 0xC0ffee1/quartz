package net.c0ffee1.quartz.platforms.bukkit.platform;

import com.google.inject.Inject;
import com.google.inject.spi.ProvisionListener;
import net.c0ffee1.quartz.platforms.bukkit.annotations.RegisterEvents;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerProvisionListener implements ProvisionListener {


    private final JavaPlugin plugin;

    public ListenerProvisionListener(JavaPlugin javaPlugin){
        this.plugin = javaPlugin;
    }

    @Override
    public <T> void onProvision(ProvisionInvocation<T> provision) {
        T instance = provision.provision();
        if (instance.getClass().isAnnotationPresent(RegisterEvents.class) && instance instanceof Listener listener) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }
}
