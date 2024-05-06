package net.c0ffee1.platforms.bukkit.protocol.wrappers;


import net.c0ffee1.platforms.bukkit.protocol.wrappers.meta.ArmorstandMetaWrapper;
import org.bukkit.entity.EntityType;

public class WrappedArmorstand extends EntityLivingWrapper{
    public WrappedArmorstand() {
        super(EntityType.ARMOR_STAND, new ArmorstandMetaWrapper());
    }

    @Override
    public ArmorstandMetaWrapper getEntityMeta() {
        return (ArmorstandMetaWrapper) super.getEntityMeta();
    }
}
