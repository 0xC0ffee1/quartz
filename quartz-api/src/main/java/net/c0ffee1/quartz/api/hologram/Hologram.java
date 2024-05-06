package net.c0ffee1.quartz.api.hologram;

import net.c0ffee1.quartz.api.entity.Positioned;
import net.c0ffee1.quartz.api.entity.Spawnable;
import net.c0ffee1.quartz.api.entity.Tagged;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a hologram that can be positioned within a specific type of location.
 * @param <L> the type of location where the hologram can be positioned
 * @param <P> the type of receiver for the hologram
 */
public interface Hologram<L, P> extends Positioned<L>, Spawnable<P>, Tagged {

    void updateLine(int index, Component line);
    void deleteLine(int index);
    void updateLineFor(P recipient, int index, Component line);

    interface Builder<H extends Hologram<?,?>> extends AbstractBuilder<H> {
        @Contract("_ -> this")
        @NotNull Builder<H> line(final @NotNull Component... lines);

        @Contract("_ -> this")
        @NotNull <L> Builder<H> location(final @NotNull L location);

        @Contract("_ -> this")
        @NotNull Builder<H> lineGap(final float gap);

        @Contract("_ -> this")
        @NotNull Builder<H> tag(final @NotNull String tag);
    }
}
