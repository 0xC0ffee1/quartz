package net.coffee1.quartz.platforms.bukkit.holograms;

import net.c0ffee1.quartz.api.hologram.Hologram;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BukkitHologramBuilderImpl implements Hologram.Builder<BukkitHologram> {
    private final List<Component> lines = new CopyOnWriteArrayList<>();
    private Location location;
    private String tag;
    private float gap;

    @Override
    public @NotNull BukkitHologram build() {
        return new BukkitHologramImpl(lines, location, tag, gap);
    }

    @Override
    public Hologram.@NotNull Builder<BukkitHologram> line(@NotNull Component... lines) {
        this.lines.addAll(Arrays.asList(lines));
        return this;
    }

    @Override
    public <L> Hologram.@NotNull Builder<BukkitHologram> location(@NotNull L location) {
        this.location = (Location) location;
        return this;
    }

    @Override
    public Hologram.@NotNull Builder<BukkitHologram> lineGap(float gap) {
        this.gap = gap;
        return this;
    }

    @Override
    public Hologram.@NotNull Builder<BukkitHologram> tag(@NotNull String tag) {
        this.tag = tag;
        return this;
    }
}
