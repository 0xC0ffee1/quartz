package net.c0ffee1.platforms.bukkit.protocol.wrappers.meta;

import com.comphenix.protocol.wrappers.Vector3F;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.util.EulerAngle;

public class ArmorstandMetaWrapper extends EntityLivingMetaWrapper {
    private final WrappedDataWatcher.WrappedDataWatcherObject modifiers = new WrappedDataWatcher.WrappedDataWatcherObject(15, BYTE_SERIALIZER);

    private final WrappedDataWatcher.WrappedDataWatcherObject headRotation = new WrappedDataWatcher.WrappedDataWatcherObject(16, WrappedDataWatcher.Registry.getVectorSerializer());
    private final WrappedDataWatcher.WrappedDataWatcherObject bodyRotation = new WrappedDataWatcher.WrappedDataWatcherObject(17, WrappedDataWatcher.Registry.getVectorSerializer());
    private final WrappedDataWatcher.WrappedDataWatcherObject leftArmRotation = new WrappedDataWatcher.WrappedDataWatcherObject(18, WrappedDataWatcher.Registry.getVectorSerializer());
    private final WrappedDataWatcher.WrappedDataWatcherObject rightArmRotation = new WrappedDataWatcher.WrappedDataWatcherObject(19, WrappedDataWatcher.Registry.getVectorSerializer());
    private final WrappedDataWatcher.WrappedDataWatcherObject leftLegRotation = new WrappedDataWatcher.WrappedDataWatcherObject(20, WrappedDataWatcher.Registry.getVectorSerializer());
    private final WrappedDataWatcher.WrappedDataWatcherObject rightLegRotation = new WrappedDataWatcher.WrappedDataWatcherObject(21, WrappedDataWatcher.Registry.getVectorSerializer());

    public void setModifiers(ArmorstandModifier... modifiers){
        byte mask = ArmorstandModifier.createMask(modifiers);
        watcher.setObject(this.modifiers, mask);
    }

    private void setRotations(WrappedDataWatcher.WrappedDataWatcherObject object, float rotX, float rotY, float rotZ){
        EulerAngle eulerAngle = new EulerAngle(Math.toRadians(rotX), Math.toRadians(rotY), Math.toRadians(rotZ));
        var vector = new Vector3F((float)Math.toDegrees(eulerAngle.getX()), (float)Math.toDegrees(eulerAngle.getY()), (float)Math.toDegrees(eulerAngle.getZ()));
        this.watcher.setObject(object, vector);
    }

    public void setHeadRotations(float rotX, float rotY, float rotZ){
        setRotations(headRotation, rotX, rotY, rotZ);
    }
    public void setBodyRotations(float rotX, float rotY, float rotZ){
        setRotations(bodyRotation, rotX, rotY, rotZ);
    }
    public void setLeftArmRotations(float rotX, float rotY, float rotZ){
        setRotations(leftArmRotation, rotX, rotY, rotZ);
    }
    public void setRightArmRotations(float rotX, float rotY, float rotZ){
        setRotations(rightArmRotation, rotX, rotY, rotZ);
    }
    public void setLeftLegRotations(float rotX, float rotY, float rotZ){
        setRotations(leftLegRotation, rotX, rotY, rotZ);
    }
    public void setRightLegRotations(float rotX, float rotY, float rotZ){
        setRotations(rightLegRotation, rotX, rotY, rotZ);
    }

    public enum ArmorstandModifier {

        SMALL(0x01),
        ARMS(0x04),
        BASE_PLATE(0x08),
        MARKER(0x10);

        private final int mask;

        ArmorstandModifier(int mask) {
            this.mask = mask;
        }

        public int getMask() {
            return mask;
        }

        public static byte createMask(ArmorstandModifier... handStatuses) {
            int mask = 0;
            for(var status : handStatuses) {
                mask |= status.mask;
            }
            return (byte) mask;
        }
    }
}
