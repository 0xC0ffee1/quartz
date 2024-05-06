package net.c0ffee1.platforms.bukkit.protocol.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

import net.c0ffee1.platforms.bukkit.protocol.wrappers.meta.PlayerMetaWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerWrapper extends EntityLivingWrapper{
    private WrappedGameProfile profile;
    public PlayerWrapper(String displayName) {
        super(EntityType.PLAYER, new PlayerMetaWrapper());
        this.profile = new WrappedGameProfile(UUID.randomUUID(), displayName);
    }

    public PlayerWrapper(Player player) {
        super(EntityType.PLAYER, new PlayerMetaWrapper());
        getPlayerMeta().linkEntity(player);
        this.entityId = player.getEntityId();
        this.profile = new WrappedGameProfile(player.getUniqueId(), player.getName());
    }

    public PlayerMetaWrapper getPlayerMeta(){
        return (PlayerMetaWrapper) getLivingMeta();
    }

    @Override
    public PacketContainer getSpawnPacket() {
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        packet.getIntegers().write(0, this.entityId);
        packet.getUUIDs().write(0, this.profile.getUUID());
        packet.getDoubles().write(0, location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());
        packet.getBytes().write(0, (byte)((int)(location.getYaw() * 256.0F / 360.0F)));
        packet.getBytes().write(1, (byte)((int)(location.getPitch() * 256.0F / 360.0F)));
        return packet;
    }
}
