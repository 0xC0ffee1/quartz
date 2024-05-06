package net.c0ffee1.platforms.bukkit.protocol.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.wrappers.*;
import lombok.Getter;
import lombok.Setter;

import net.c0ffee1.quartz.platforms.bukkit.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//Dogshit legacy code, too lazy to rewrite
//@TODO rewrite
public class ArmorstandWrapper {
    private static final AtomicInteger CURRENT_ID = new AtomicInteger(-10000);
    private final int id;
    private final UUID uuid;

    @Getter
    private Location location;

    @Setter
    private Component customName = Component.empty();

    @Getter @Setter
    private boolean isSmall, hasArms, hasPlate, marker, invisible,customNameVisible;

    private Vector3F headPose = new Vector3F();

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArmorstandWrapper(){
        this.id = CURRENT_ID.incrementAndGet();
        this.uuid = UUID.randomUUID();
    }

    public void setHeadPose(float x, float y, float z) {
        this.headPose.setX(x);
        this.headPose.setY(y);
        this.headPose.setZ(z);
    }

    public void setHeadItem(Player player, ItemStack itemStack){
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0, this.id);
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
        list.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, itemStack));
        packet.getSlotStackPairLists().write(0, list);
        ProtocolUtils.sendPacket(player, packet);
    }

    public void rotateHead(double degrees){
        EulerAngle eulerAngle = new EulerAngle(Math.toRadians(0), Math.toRadians(degrees), Math.toRadians(0));
        this.headPose = new Vector3F(0, (float)Math.toDegrees(eulerAngle.getY()), 0);
    }

    public void moveTo(Player player, Location location){
        this.location = location;
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
        packet.getIntegers().write(0, this.id);
        packet.getDoubles().write(0, location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());
        packet.getBytes().write(0, (byte) 0);
        packet.getBytes().write(1, (byte) 0);
        packet.getBooleans().write(0, true);
        ProtocolUtils.sendPacket(player, packet);
    }

    public void updateLocation(Player player){
        moveTo(player, this.location);
    }

    public void updateMeta(Player player){
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        WrappedDataWatcher watcher = new WrappedDataWatcher();

        var byteSerializer = WrappedDataWatcher.Registry.get(Byte.class);
        var booleanSerializer = WrappedDataWatcher.Registry.get(Boolean.class);



        //OptChat must be wrapped in this, it doesn't even give an error msg if you do it otherwise
        Optional<?> opt = Optional
                .of(ProtocolUtils.getChatComponent(customName).getHandle());


        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);

        WrappedDataWatcher.WrappedDataWatcherObject nameVisible = new WrappedDataWatcher.WrappedDataWatcherObject(3, booleanSerializer);

        watcher.setObject(nameVisible, customNameVisible);


        WrappedDataWatcher.WrappedDataWatcherObject entityBitMask = new WrappedDataWatcher.WrappedDataWatcherObject(0, byteSerializer);

        WrappedDataWatcher.WrappedDataWatcherObject headPose = new WrappedDataWatcher.WrappedDataWatcherObject(16, WrappedDataWatcher.Registry.getVectorSerializer());

        watcher.setObject(headPose, this.headPose);

        byte small = (byte) (isSmall ? 0x01 : 0x00);
        byte arms = (byte) (hasArms ? 0x04 : 0x00);
        byte plate = (byte) (hasPlate ? 0x08 : 0x00);
        byte marker = (byte) (this.marker ? 0x10 : 0x00);

        byte bitMask = (byte) (small | arms | plate | marker);

        WrappedDataWatcher.WrappedDataWatcherObject bytes = new WrappedDataWatcher.WrappedDataWatcherObject(15, byteSerializer);
        watcher.setObject(bytes, bitMask);
        watcher.setObject(entityBitMask, (byte) (this.invisible ? 0x20 : 0x00));

        packet.getIntegers().write(0, this.id);

        List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();

        for(final WrappedWatchableObject entry : watcher.getWatchableObjects()) {
            if(entry == null) continue;

            WrappedDataWatcher.WrappedDataWatcherObject watcherObject = entry.getWatcherObject();
            wrappedDataValueList.add(
                    new WrappedDataValue(
                            watcherObject.getIndex(),
                            watcherObject.getSerializer(),
                            entry.getRawValue()
                    )
            );
        }
//
//        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        packet.getDataValueCollectionModifier().write(0, wrappedDataValueList);

        ProtocolUtils.sendPacket(player, packet);
    }

    public String getCustomNameText() {
        return ComponentUtils.toLegacyText(customName);
    }

    public void spawn(Player player){
        spawn(player, location);
    }

    public void spawn(Player player, Location location){
        this.location = location;
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY);

//        player.sendMessage("bytes: " + packet.getBytes().size());
//        player.sendMessage("ints: " + packet.getIntegers().size());
//        player.sendMessage("doubles: " + packet.getDoubles().size());

        packet.getIntegers().write(0, this.id); //entityID
        packet.getUUIDs().write(0, this.uuid); //UUID
        packet.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND); //Type
        packet.getDoubles().write(0, location.getX()); //pos X
        packet.getDoubles().write(1, location.getY()); //pos Y
        packet.getDoubles().write(2, location.getZ()); //pos Z

        //1.19
//        packet.getIntegers().write(1,0); //data
//        packet.getIntegers().write(2, 0); //vel x
//        packet.getIntegers().write(3, 0); //vel y
//        packet.getIntegers().write(4, 0); //vel z
//
//
//        packet.getBytes().write(0, (byte) 0); //pitch
//        packet.getBytes().write(1, (byte) 0); //yaw
//        packet.getBytes().write(2, (byte) 0); //head yaw?

        ProtocolUtils.sendPacket(player, packet);
        updateMeta(player);
    }

    public void destroy(Player player){
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);

        packet.getIntLists().write(0, Collections.singletonList(this.id));
        ProtocolUtils.sendPacket(player, packet);
    }
}
