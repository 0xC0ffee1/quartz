package net.c0ffee1.quartz.api.hologram;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * A pool of holograms
 * @param <K> The type of the key
 * @param <H> the type of value
 */
public interface HologramPool<K, H extends Hologram<?,?>> {
    /**
     * Adds a hologram to the pool with the specified identifier.
     * @param id the unique identifier of the hologram
     * @param hologram the hologram to add to the pool
     * @throws IllegalArgumentException if the hologram is null
     */
    void addHologram(K id, H hologram);
    /**
     * Destroys the hologram associated with the specified identifier.
     * @param id the identifier of the hologram to destroy
     * @throws NoSuchElementException if no hologram with the specified identifier exists
     */
    void destroyHologram(K id);

    /**
     * Retrieves the hologram associated with the specified identifier.
     * @param id the identifier of the hologram to retrieve
     * @return the hologram if found, or null if no hologram matches the identifier
     */
    Optional<H> getHologram(K id);
}
