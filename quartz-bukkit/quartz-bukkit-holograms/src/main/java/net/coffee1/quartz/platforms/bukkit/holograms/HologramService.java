package net.coffee1.quartz.platforms.bukkit.holograms;

import net.c0ffee1.quartz.api.hologram.Hologram;
import net.c0ffee1.quartz.api.hologram.HologramPool;
import net.c0ffee1.quartz.platforms.bukkit.annotations.RegisterEvents;
import net.c0ffee1.quartz.platforms.bukkit.platform.PluginReceiver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RegisterEvents
public class HologramService implements Listener, HologramPool<String, BukkitHologram>, PluginReceiver {
    private final Map<String, BukkitHologram> holograms = new ConcurrentHashMap<>();

    private Plugin javaPlugin;
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        spawnHolograms(event.getPlayer());
    }

    @Override
    public void setPlugin(Plugin plugin) {
        this.javaPlugin = plugin;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(javaPlugin, ()->{
            for(BukkitHologram hologram : holograms.values()){
                hologram.destroy(event.getPlayer());
            }
        });
    }

    public void spawnHolograms(Player player){
        for(BukkitHologram hologram : holograms.values()){
            if(!player.getWorld().equals(hologram.getLocation().getWorld())) continue;
            hologram.spawn(player);
        }
    }

    public void addHologram(String id, BukkitHologram hologram){
        holograms.put(id, hologram);
    }

    @Override
    public void destroyHologram(String id) {
        BukkitHologram hologram = holograms.remove(id);
        if(hologram == null) return;
        hologram.destroy();
    }

    @Override
    public Optional<BukkitHologram> getHologram(String id) {
        return Optional.ofNullable(holograms.get(id));
    }
}
