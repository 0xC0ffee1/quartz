package net.c0ffee1.platforms.bukkit.protocol.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

import net.c0ffee1.platforms.bukkit.protocol.wrappers.meta.EntityMetaWrapper;
import net.c0ffee1.platforms.bukkit.protocol.wrappers.meta.WrappedEntityMeta;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityWrapper implements WrappedEntity{
    protected static AtomicInteger ENTITY_ID = new AtomicInteger(-20000);

    protected final EntityMetaWrapper wrappedEntityMeta;
    protected int entityId;
    protected UUID entityUUID;
    protected Location location;
    protected EntityType entityType;

    protected int spawnData;

    protected Set<UUID> watchers = new HashSet<>();

    protected HashMap<String, Object> metadata = new HashMap<>();

    public EntityWrapper(EntityType entityType){
        this(entityType, new EntityMetaWrapper());
    }

    public EntityWrapper(EntityType entityType, EntityMetaWrapper meta){
        this.wrappedEntityMeta = meta;
        this.entityType = entityType;
        this.entityUUID = UUID.randomUUID();
        this.entityId = ENTITY_ID.getAndIncrement();
    }

    public void setMetadata(String key, Object value){
        this.metadata.put(key, value);
    }

    public <T> T getMetadata(String key){
        return (T) metadata.get(key);
    }

    public <T> T getMetadata(String key, Object def){
        return (T) metadata.getOrDefault(key, def);
    }

    public boolean hasMetadata(String key){
        return metadata.containsKey(key);
    }

    public void setSpawnData(int spawnData) {
        this.spawnData = spawnData;
    }

    public int getId(){
        return entityId;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public WrappedEntityMeta getWrappedEntityMeta() {
        return wrappedEntityMeta;
    }

    public PacketContainer getMetaPacket(){
        return wrappedEntityMeta.getPacket(entityId);
    }

    public void updateMeta(Player player){
        sendPacket(player, getMetaPacket());
    }

    public void updateMetaForWatchers(){
        var packet = getMetaPacket();
        for(UUID watcher : watchers){
            Player player = Bukkit.getPlayer(watcher);
            if(player == null) continue;
            sendPacket(player, packet);
        }
    }

    public Component getCustomName(){
        return getEntityMeta().getCustomName();
    }

    public boolean customNameVisible(){
        return (boolean) getEntityMeta().getObject(getEntityMeta().nameVisible);
    }

    public int getAirTicks(){
        return (int) getEntityMeta().getObject(getEntityMeta().airTicks);
    }

    public int getFrozenTicks(){
        return (int) getEntityMeta().getObject(getEntityMeta().frozenTicks);
    }

    public List<EntityMetaWrapper.EntityState> getEntityStates(){
        byte bitMask = (byte) getEntityMeta().getObject(getEntityMeta().entityState);
        EntityMetaWrapper.EntityState[] states = EntityMetaWrapper.EntityState.fromMask(bitMask);
        return Arrays.asList(states);
    }

    public boolean isOnFire(){
        return getEntityStates().contains(EntityMetaWrapper.EntityState.ON_FIRE);
    }

    public boolean isCrouching(){
        return getEntityStates().contains(EntityMetaWrapper.EntityState.CROUCHING);
    }

    public boolean isSprinting(){
        return getEntityStates().contains(EntityMetaWrapper.EntityState.SPRINTING);
    }

    public boolean isSwimming(){
        return getEntityStates().contains(EntityMetaWrapper.EntityState.SWIMMING);
    }

    public boolean isInvisible(){
        return getEntityStates().contains(EntityMetaWrapper.EntityState.INVISIBLE);
    }

    public boolean isGlowing(){
        return getEntityStates().contains(EntityMetaWrapper.EntityState.GLOWING);
    }

    public boolean isFlyingWithElytra(){
        return getEntityStates().contains(EntityMetaWrapper.EntityState.ELYTRA_FLIGHT);
    }

    public void setEntityStates(EntityMetaWrapper.EntityState... states){
        getEntityMeta().setEntityState(states);
    }

    public void setAirTicks(int ticks){
        getEntityMeta().setAirTicks(ticks);
    }

    public void setCustomName(Component customName){
        getEntityMeta().setCustomName(customName);
    }

    public void setCustomNameVisible(boolean visible){
        getEntityMeta().setCustomNameVisible(visible);
    }

    public void setSilent(boolean silent){
        getEntityMeta().setSilent(silent);
    }

    public void setInvisible(boolean invisible){
        if(invisible){
            getEntityMeta().setEntityState(EntityMetaWrapper.EntityState.INVISIBLE);
        }
        else getEntityMeta().setEntityState(EntityMetaWrapper.EntityState.fromMask(0x00));
    }


    public void setPose(Pose pose){
        getEntityMeta().setPose(pose);
    }

    public void setFrozenTicks(int ticks){
        getEntityMeta().setFrozenTicks(ticks);
    }

    public void sendPacket(Player player, PacketContainer packet){
        ProtocolUtils.sendPacket(player, packet);
    }

    @Override
    public void spawn(Player player, Location location) {
        this.location = location;
        sendPacket(player, getSpawnPacket());
        updateMeta(player);
        watchers.add(player.getUniqueId());
    }

    public PacketContainer getDespawnPacket(){
        var packet =  ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        packet.getIntLists().write(0, Arrays.asList(entityId));
        return packet;
    }

    public void despawn(Player player){
        sendPacket(player, getDespawnPacket());
        watchers.remove(player.getUniqueId());
    }

    public boolean isWatcher(UUID uuid){
        return watchers.contains(uuid);
    }

    public void removeWatcher(UUID uuid){
        watchers.remove(uuid);
    }

    public void despawn(){
        var packet = getDespawnPacket();
        var iter = watchers.iterator();
        while (iter.hasNext()){
            Player player = Bukkit.getPlayer(iter.next());
            if(player == null) continue;
            sendPacket(player, packet);
            iter.remove();
        }
    }

    public EntityMetaWrapper getEntityMeta() {
        return (EntityMetaWrapper) wrappedEntityMeta;
    }

    public void moveTo(Player player, Location location){
        this.location = location;
        ProtocolUtils.sendPacket(player, getTeleportPacket(location));
    }

    public static AtomicInteger getEntityId() {
        return ENTITY_ID;
    }

    public UUID getEntityUUID() {
        return entityUUID;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    protected PacketContainer getTeleportPacket(Location location){
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
        packet.getIntegers().write(0, this.entityId);
        packet.getDoubles().write(0, location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());
        packet.getBytes().write(0, (byte) 0);
        packet.getBytes().write(1, (byte) 0);
        packet.getBooleans().write(0, true);
        return packet;
    }

    @Override
    public PacketContainer getSpawnPacket() {
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getIntegers().write(0, entityId);
        packet.getUUIDs().write(0, UUID.randomUUID());
        packet.getDoubles().write(0, location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());

        packet.getEntityTypeModifier().write(0, entityType);

        packet.getIntegers().write(1, 0); //vel x
        packet.getIntegers().write(2, 0); //vel y
        packet.getIntegers().write(3, 0); //vel z
        packet.getIntegers().write(4, spawnData); //data


        packet.getBytes().write(0, (byte) 0); //pitch
        packet.getBytes().write(1, (byte) 0); //yaw
        packet.getBytes().write(2, (byte) 0); //head yaw?
        return packet;
    }
}
