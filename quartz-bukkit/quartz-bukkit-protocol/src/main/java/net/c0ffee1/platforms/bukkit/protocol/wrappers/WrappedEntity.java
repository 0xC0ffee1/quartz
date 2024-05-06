package net.c0ffee1.platforms.bukkit.protocol.wrappers;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface WrappedEntity {

    PacketContainer getSpawnPacket();
    void spawn(Player player, Location location);
}
