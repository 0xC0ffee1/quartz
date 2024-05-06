package net.c0ffee1.quartz.platforms.bukkit.platform;

import com.google.inject.Inject;
import com.google.inject.spi.ProvisionListener;
import net.c0ffee1.quartz.platforms.bukkit.annotations.RegisterEvents;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ListenerProvisionListener implements ProvisionListener {

    //In certain scenarios events might get fired twice if user includes a non-singleton dependency many times
    //Also a binded shared class annotated with this also gets registered twice
    private final static Set<Class<?>> registeredListeners = new CopyOnWriteArraySet<>();

    private final JavaPlugin plugin;

    public ListenerProvisionListener(JavaPlugin javaPlugin){
        this.plugin = javaPlugin;
    }

    @Override
    public <T> void onProvision(ProvisionInvocation<T> provision) {
        T instance = provision.provision();
        if(instance instanceof PluginReceiver receiver) receiver.setPlugin(plugin);
        if (instance.getClass().isAnnotationPresent(RegisterEvents.class) && instance instanceof Listener listener) {
            if(registeredListeners.contains(listener.getClass())) return;
            Bukkit.getPluginManager().registerEvents(listener, plugin);
            registeredListeners.add(listener.getClass());
        }
    }
}
