package net.c0ffee1.quartz.api.entity;

import net.kyori.adventure.audience.Audience;

public interface Spawnable<P> {
    void spawn(P receiver);
    void destroy(P receiver);
    void destroy();
}
