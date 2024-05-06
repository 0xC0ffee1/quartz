package net.c0ffee1.platforms.bukkit.protocol.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;

import net.c0ffee1.platforms.bukkit.protocol.wrappers.meta.EntityLivingMetaWrapper;
import net.c0ffee1.platforms.bukkit.protocol.wrappers.meta.EntityMetaWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EntityLivingWrapper extends EntityWrapper{
    public static HashMap<EntityType, Integer> ID_MAP = new HashMap<>();

    public EntityLivingWrapper(LivingEntity entity){
        super(entity.getType(), new EntityLivingMetaWrapper());
        this.getEntityMeta().linkEntity(entity);
    }

    public EntityLivingWrapper(EntityType entityType) {
        super(entityType, new EntityLivingMetaWrapper());
    }

    public EntityLivingWrapper(EntityType entityType, EntityMetaWrapper meta) {
        super(entityType, meta);
    }

    public EntityLivingMetaWrapper getLivingMeta(){
        return (EntityLivingMetaWrapper) getWrappedEntityMeta();
    }

    private PacketContainer getEntityHeadRotatePacket() {
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        packet.getIntegers().write(0, this.entityId);
        packet.getBytes().write(0, (byte)((int)(this.location.getYaw() * 256.0F / 360.0F)));
        return packet;
    }

    private PacketContainer getEntityLookPacket() {
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_LOOK);
        packet.getIntegers().write(0, this.entityId);
        packet.getBytes().write(0, (byte)((int)(this.location.getYaw() * 256.0F / 360.0F)));
        packet.getBytes().write(1, (byte)((int)(this.location.getPitch() * 256.0F / 360.0F)));
        return packet;
    }

    public void setEquipment(Player player, Pair<EnumWrappers.ItemSlot, ItemStack>... equipment){
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0, this.entityId);
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>(Arrays.asList(equipment));
        packet.getSlotStackPairLists().write(0, list);
        sendPacket(player, packet);
    }

    public void rotateHead(Player player, float pitch, float yaw) {
        this.location.setPitch(pitch);
        this.location.setYaw(yaw);
        this.sendPacket(player, this.getEntityLookPacket());
        this.sendPacket(player, this.getEntityHeadRotatePacket());
    }

//    @Override
//    public PacketContainer getSpawnPacket() {
//        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
//        packet.getIntegers().write(0, this.entityId);
//        packet.getUUIDs().write(0, this.entityUUID);
//        packet.getIntegers().write(1, ID_MAP.get(entityType));
//        packet.getDoubles().write(0, this.location.getX());
//        packet.getDoubles().write(1, this.location.getY());
//        packet.getDoubles().write(2, this.location.getZ());
//        packet.getIntegers().write(2,0); //vel x
//        packet.getIntegers().write(3, 0); //vel y
//        packet.getIntegers().write(4, 0); //vel z
//        packet.getBytes().write(0, (byte)((int)(location.getYaw() * 256.0F / 360.0F)));
//        packet.getBytes().write(1, (byte)((int)(location.getPitch() * 256.0F / 360.0F)));
//        packet.getBytes().write(2, (byte)((int)(location.getYaw() * 256.0F / 360.0F)));
//        return packet;
//    }
}
