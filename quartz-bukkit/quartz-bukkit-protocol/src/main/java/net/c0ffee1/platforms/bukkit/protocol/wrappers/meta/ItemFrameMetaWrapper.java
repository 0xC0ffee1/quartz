package net.c0ffee1.platforms.bukkit.protocol.wrappers.meta;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.inventory.ItemStack;

public class ItemFrameMetaWrapper extends EntityMetaWrapper {
    private ItemStack itemStack;
    private int rotation;

    public int getRotation() {
        return rotation;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        var obj = new WrappedDataWatcher.WrappedDataWatcherObject(8, WrappedDataWatcher.Registry.getItemStackSerializer(false));
        this.watcher.setObject(obj, itemStack);
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
        var obj = new WrappedDataWatcher.WrappedDataWatcherObject(9, WrappedDataWatcher.Registry.get(Integer.class));
        this.watcher.setObject(obj, rotation);
    }
}
