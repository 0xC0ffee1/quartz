package net.c0ffee1.platforms.bukkit.protocol.wrappers;


import net.c0ffee1.platforms.bukkit.protocol.wrappers.meta.ItemFrameMetaWrapper;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;

public class ItemFrameWrapper extends EntityWrapper {
    private BlockFace facing;

    public ItemFrameWrapper() {
        super(EntityType.ITEM_FRAME, new ItemFrameMetaWrapper());
    }

    public BlockFace getFacing() {
        return facing;
    }

    @Override
    public ItemFrameMetaWrapper getEntityMeta() {
        return (ItemFrameMetaWrapper) super.getEntityMeta();
    }

    public void setFacing(BlockFace facing) {
        this.facing = facing;
        int orientation = 1;
        switch (facing){
            case NORTH -> orientation = 2;
            case SOUTH -> orientation = 3;
            case WEST -> orientation = 4;
            case EAST -> orientation = 5;
        }
        setSpawnData(orientation);
    }
}
