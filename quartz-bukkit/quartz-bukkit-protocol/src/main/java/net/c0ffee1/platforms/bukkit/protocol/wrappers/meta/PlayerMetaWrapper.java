package net.c0ffee1.platforms.bukkit.protocol.wrappers.meta;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;

public class PlayerMetaWrapper extends EntityLivingMetaWrapper implements WrappedEntityMeta{
    private final WrappedDataWatcher.WrappedDataWatcherObject additionalHearts = new WrappedDataWatcher.WrappedDataWatcherObject(15, FLOAT_SERIALIZER);
    private final WrappedDataWatcher.WrappedDataWatcherObject score = new WrappedDataWatcher.WrappedDataWatcherObject(16, INT_SERIALIZER);
    private final WrappedDataWatcher.WrappedDataWatcherObject skinState = new WrappedDataWatcher.WrappedDataWatcherObject(17, BYTE_SERIALIZER);
    private final WrappedDataWatcher.WrappedDataWatcherObject mainHand = new WrappedDataWatcher.WrappedDataWatcherObject(18, BYTE_SERIALIZER);

    public void setSkinState(SkinStatus... skinStatuses){
        byte mask = SkinStatus.createMask(skinStatuses);
        this.watcher.setObject(skinState, mask);
    }

    public void setAdditionalHearts(float amount){
        this.watcher.setObject(additionalHearts, amount);
    }

    public void setScore(int score){
        this.watcher.setObject(this.score, score);
    }

    public void setMainHand(byte value){
        this.watcher.setObject(this.mainHand, value);
    }

    public enum SkinStatus {

        CAPE_ENABLED(0x01),
        JACKET_ENABLED(0x02),
        LEFT_SLEEVE_ENABLED(0x04),
        RIGHT_SLEEVE_ENABLED(0x08),
        LEFT_PANTS_LEG_ENABLED(0x10),
        RIGHT_PANTS_LEG_ENABLED(0x20),
        HAT_ENABLED(0x40),
        ALL_ENABLED(0xFF);

        private final int mask;

        SkinStatus(int mask) {
            this.mask = mask;
        }

        public int getMask() {
            return mask;
        }

        public static byte createMask(SkinStatus... skinStatuses) {
            int mask = 0;
            for(SkinStatus handStatus : skinStatuses) {
                mask |= handStatus.mask;
            }
            return (byte) mask;
        }
    }
}
