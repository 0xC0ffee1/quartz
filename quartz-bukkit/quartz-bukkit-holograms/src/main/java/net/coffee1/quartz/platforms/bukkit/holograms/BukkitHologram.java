package net.coffee1.quartz.platforms.bukkit.holograms;

import net.c0ffee1.quartz.api.hologram.Hologram;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public interface BukkitHologram extends Hologram<Location, Player> {
    static Builder<BukkitHologram> builder(){
        return new BukkitHologramBuilderImpl();
    }
}
