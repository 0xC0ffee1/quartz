package net.coffee1.quartz.platforms.bukkit.holograms;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import net.c0ffee1.quartz.api.hologram.Hologram;
import net.c0ffee1.quartz.api.hologram.HologramPool;

public class HologramModule extends AbstractModule {
    @Override
    protected void configure() {
        super.configure();
        bind(new TypeLiteral<HologramPool<String, BukkitHologram>>() {})
                .to(HologramService.class);
    }
}
