package net.c0ffee1.platforms.bukkit.protocol.wrappers.meta;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityMetaWrapper implements WrappedEntityMeta {
    protected final WrappedDataWatcher watcher = new WrappedDataWatcher();

    public static WrappedDataWatcher.Serializer BYTE_SERIALIZER = WrappedDataWatcher.Registry.get(Byte.class);
    public static WrappedDataWatcher.Serializer INT_SERIALIZER = WrappedDataWatcher.Registry.get(Integer.class);
    public static WrappedDataWatcher.Serializer FLOAT_SERIALIZER = WrappedDataWatcher.Registry.get(Float.class);
    public static WrappedDataWatcher.Serializer BOOLEAN_SERIALIZER = WrappedDataWatcher.Registry.get(Boolean.class);
    public static WrappedDataWatcher.Serializer CHAT_SERIALIZER = WrappedDataWatcher.Registry.getChatComponentSerializer(true);

    public final WrappedDataWatcher.WrappedDataWatcherObject entityState = new WrappedDataWatcher.WrappedDataWatcherObject(0, BYTE_SERIALIZER);
    public final WrappedDataWatcher.WrappedDataWatcherObject airTicks = new WrappedDataWatcher.WrappedDataWatcherObject(1, INT_SERIALIZER);
    public final WrappedDataWatcher.WrappedDataWatcherObject customNameObj = new WrappedDataWatcher.WrappedDataWatcherObject(2, CHAT_SERIALIZER);
    public final WrappedDataWatcher.WrappedDataWatcherObject nameVisible = new WrappedDataWatcher.WrappedDataWatcherObject(3, BOOLEAN_SERIALIZER);
    public final WrappedDataWatcher.WrappedDataWatcherObject isSilent = new WrappedDataWatcher.WrappedDataWatcherObject(4, BOOLEAN_SERIALIZER);
    public final WrappedDataWatcher.WrappedDataWatcherObject hasNoGravity = new WrappedDataWatcher.WrappedDataWatcherObject(5, BOOLEAN_SERIALIZER);
    public final WrappedDataWatcher.WrappedDataWatcherObject pose = new WrappedDataWatcher.WrappedDataWatcherObject(6, INT_SERIALIZER);
    public final WrappedDataWatcher.WrappedDataWatcherObject frozenTicks = new WrappedDataWatcher.WrappedDataWatcherObject(7, INT_SERIALIZER);

    public void setPose(Pose pose) {
        watcher.setObject(this.pose, pose.ordinal());
    }

    public void linkEntity(Entity entity){
        this.watcher.setEntity(entity);
    }

    public PacketContainer getPacket(int entityId){
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entityId);

        List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();

        for(final WrappedWatchableObject entry : getWatchableObjects()) {
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
        packet.getDataValueCollectionModifier().write(0, wrappedDataValueList);
//
//        packet.getWatchableCollectionModifier().write(0, getWatchableObjects());
        return packet;
    }

    public void setCustomName(Component customName){
        String json = GsonComponentSerializer.gson().serialize(customName);
        var wrapped = WrappedChatComponent
                .fromJson(json);
        Optional<?> opt = Optional
                .of(wrapped.getHandle());

        this.watcher.setObject(this.customNameObj, opt);
    }

    public Component getCustomName(){
        Optional<WrappedChatComponent> c = (Optional<WrappedChatComponent>) this.watcher.getObject(this.customNameObj);
        if(c.isPresent()){
            return GsonComponentSerializer.gson().deserialize(c.get().getJson());
        }
        return null;
    }

    public Object getObject(WrappedDataWatcher.WrappedDataWatcherObject obj){
        return this.watcher.getObject(obj);
    }

    public void setCustomNameVisible(boolean visible){
        this.watcher.setObject(this.nameVisible, visible);
    }

    public void setAirTicks(int ticks){
        this.watcher.setObject(airTicks, ticks);
    }

    public void setSilent(boolean isSilent){
        this.watcher.setObject(this.isSilent, isSilent);
    }

    public void setHasNoGravity(boolean gravity){
        this.watcher.setObject(hasNoGravity, gravity);
    }

    public void setFrozenTicks(int ticks){
        this.watcher.setObject(frozenTicks, ticks);
    }

    public void setEntityState(EntityState... entityStates){
        byte mask = EntityState.createMask(entityStates);
        this.watcher.setObject(entityState, mask);
    }



    public List<WrappedWatchableObject> getWatchableObjects() {
        return watcher.getWatchableObjects();
    }

    public enum EntityState{
        ON_FIRE(0x01),
        CROUCHING(0x02),
        SPRINTING(0x08),
        SWIMMING(0x10),
        INVISIBLE(0x20),
        GLOWING(0x40),
        ELYTRA_FLIGHT(0x80);

        byte bitMask;

        EntityState(int bitMask){
            this.bitMask = (byte) bitMask;
        }

        public static byte createMask(EntityState... states) {
            int mask = 0;
            for(EntityState state : states) {
                mask |= state.getBitMask();
            }
            return (byte) mask;
        }

        public byte getBitMask() {
            return bitMask;
        }

        public static EntityState[] fromMask(int mask) {
            List<EntityState> list = new ArrayList<>();
            for(EntityState handStatus : values()) {
                if((handStatus.getBitMask() & mask) == handStatus.getBitMask()) {
                    list.add(handStatus);
                }
            }
            return list.toArray(new EntityState[0]);
        }
    }
}
