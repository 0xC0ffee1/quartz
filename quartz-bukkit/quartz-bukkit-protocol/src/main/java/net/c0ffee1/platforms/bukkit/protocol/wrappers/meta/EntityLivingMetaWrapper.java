package net.c0ffee1.platforms.bukkit.protocol.wrappers.meta;

import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EntityLivingMetaWrapper extends EntityMetaWrapper implements WrappedEntityMeta{

    public final WrappedDataWatcher.WrappedDataWatcherObject handState = new WrappedDataWatcher.WrappedDataWatcherObject(8, BYTE_SERIALIZER);
    public final WrappedDataWatcher.WrappedDataWatcherObject health = new WrappedDataWatcher.WrappedDataWatcherObject(9, FLOAT_SERIALIZER);
    public final WrappedDataWatcher.WrappedDataWatcherObject potionEffect = new WrappedDataWatcher.WrappedDataWatcherObject(10, FLOAT_SERIALIZER);
    public final WrappedDataWatcher.WrappedDataWatcherObject potionAmbient = new WrappedDataWatcher.WrappedDataWatcherObject(11, BOOLEAN_SERIALIZER);
    public final WrappedDataWatcher.WrappedDataWatcherObject arrowsInBody = new WrappedDataWatcher.WrappedDataWatcherObject(12, INT_SERIALIZER);
    public final WrappedDataWatcher.WrappedDataWatcherObject beeStings = new WrappedDataWatcher.WrappedDataWatcherObject(13, INT_SERIALIZER);
    public final WrappedDataWatcher.WrappedDataWatcherObject bedPos = new WrappedDataWatcher.WrappedDataWatcherObject(14, WrappedDataWatcher.Registry.getBlockPositionSerializer(true));

    public void setHealth(float health){
        this.watcher.setObject(this.health, health);
    }

    public void setPotionEffectColor(int color){
        this.watcher.setObject(potionEffect, color);
    }

    public void setPotionAmbient(boolean ambient){
        this.watcher.setObject(potionAmbient, ambient);
    }

    public void setAmountOfArrowsInBody(int amount){
        this.watcher.setObject(arrowsInBody, amount);
    }

    public void setAmountOfBeeStings(int amount){
        this.watcher.setObject(beeStings, amount);
    }

    public void setCurrentBedPosition(Location loc){
        Optional<?> opt = Optional
                .of(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));

        this.watcher.setObject(bedPos, opt);
    }

    public void setHandState(HandStatus... handStatuses){
        byte mask = HandStatus.createMask(handStatuses);
        this.watcher.setObject(handState, mask);
    }

    public List<HandStatus> getHandStates(){
        byte bitMask = (byte) getObject(handState);
        var states = HandStatus.fromMask(bitMask);
        return Arrays.asList(states);
    }


    public enum HandStatus {

        MAIN_HAND(0x00),
        HAND_ACTIVE(0x01),
        OFF_HAND(0x02),
        RIPTIDE_SPIN_ATTACK(0x04),
        ALL(0x07);

        private final int mask;

        HandStatus(int mask) {
            this.mask = mask;
        }

        public int getMask() {
            return mask;
        }

        public static byte createMask(HandStatus... handStatuses) {
            int mask = 0;
            for(HandStatus handStatus : handStatuses) {
                mask |= handStatus.mask;
            }
            return (byte) mask;
        }

        public static HandStatus[] fromMask(int mask) {
            List<HandStatus> list = new ArrayList<>();
            for(HandStatus handStatus : values()) {
                if((handStatus.mask & mask) == handStatus.mask) {
                    list.add(handStatus);
                }
            }
            return list.toArray(new HandStatus[list.size()]);
        }
    }

}
