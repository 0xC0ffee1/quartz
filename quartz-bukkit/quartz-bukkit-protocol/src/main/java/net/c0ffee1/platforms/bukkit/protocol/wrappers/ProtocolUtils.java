package net.c0ffee1.platforms.bukkit.protocol.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ProtocolUtils {
    private static AtomicInteger UTIL_ENTITY_ID = new AtomicInteger(50000);

    public static boolean sendPacket(Player player, PacketContainer packetContainer){
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer);
        return true;
    }

    public static WrappedChatComponent getChatComponent(Component component){
        String json = GsonComponentSerializer.gson().serialize(component);
        return WrappedChatComponent.fromJson(json);
    }


    public static void spawnEntity(Player player, EntityType type, Location location, int data){
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getIntegers().write(0, UTIL_ENTITY_ID.getAndIncrement());
        packet.getUUIDs().write(0, UUID.randomUUID());
        packet.getDoubles().write(0, location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());

        packet.getEntityTypeModifier().write(0, type);

        packet.getIntegers().write(1, 0); //vel x
        packet.getIntegers().write(2, 0); //vel y
        packet.getIntegers().write(3, 0); //vel z
        packet.getIntegers().write(4, data); //data


        packet.getBytes().write(0, (byte) 0); //pitch
        packet.getBytes().write(1, (byte) 0); //yaw
        packet.getBytes().write(2, (byte) 0); //head yaw?

//        packet.getIntegers().write(2, 0); //vel x
//        packet.getIntegers().write(3, 0); //vel y
//        packet.getIntegers().write(4, 0); //vel z
//
//

//        packet.getBytes().write(2, (byte) 0); //head yaw?


//        packet.getIntegers().write(1,0); //vel x
//        packet.getIntegers().write(2, 0); //vel y
//        packet.getIntegers().write(3, 0); //vel z
//        packet.getIntegers().write(4, ((int)(location.getYaw() * 256.0F / 360.0F)));
//        packet.getIntegers().write(5, ((int)(location.getPitch() * 256.0F / 360.0F)));
//        packet.getIntegers().write(6, data); //data
        sendPacket(player, packet);
    }

    public static void updateMeta(Player player, Entity entity){
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entity.getEntityId());
        packet.getWatchableCollectionModifier().write(0, WrappedDataWatcher.getEntityWatcher(entity).getWatchableObjects());
        ProtocolUtils.sendPacket(player, packet);
    }

    public static void destroyEntity(Player player, Integer... id){
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        packet.getIntLists().write(0, Arrays.asList(id));
        sendPacket(player, packet);
    }
}
