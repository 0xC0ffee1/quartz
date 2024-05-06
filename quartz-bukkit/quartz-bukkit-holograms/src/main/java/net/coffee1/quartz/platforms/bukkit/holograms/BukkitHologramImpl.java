package net.coffee1.quartz.platforms.bukkit.holograms;

import lombok.Data;
import me.clip.placeholderapi.PlaceholderAPI;
import net.c0ffee1.platforms.bukkit.protocol.wrappers.ArmorstandWrapper;
import net.c0ffee1.quartz.api.hologram.Hologram;
import net.c0ffee1.quartz.platforms.bukkit.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class BukkitHologramImpl implements BukkitHologram {
    private final ConcurrentHashMap<UUID, ArrayList<ArmorstandWrapper>> ids = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, PlayerData> playerData = new ConcurrentHashMap<>();
    private final List<Component> lines;
    private final Location location;
    private final String tag;
    private final float gap;

    BukkitHologramImpl(List<Component> lines, Location location, String tag, float gap){
        this.lines = lines;
        this.tag = tag;
        this.gap = gap;
        this.location = location;
    }

    public void setPlayerVisibility(boolean visible, UUID player) {
        PlayerData pData = playerData.computeIfAbsent(player, (k)-> new PlayerData());
        pData.setVisible(visible);
    }

    public boolean isVisibleForPlayer(UUID player){
        PlayerData pData = playerData.computeIfAbsent(player, (k)-> new PlayerData());
        return pData.isVisible();
    }

    @Override
    public void spawn(Player receiver) {
        setPlayerVisibility(true, receiver.getUniqueId());
        double y = this.location.getY() + (lines.size() * gap) + 1;
        for(Component text : lines){
            createHologramForPlayer(receiver, text, y-=gap);
        }
    }

    @Override
    public void destroy(Player receiver) {
        if(!ids.containsKey(receiver.getUniqueId())) return;
        playerData.remove(receiver.getUniqueId());
        List<ArmorstandWrapper> stands = ids.remove(receiver.getUniqueId());
        for (var armorStand : stands) {
            armorStand.destroy(receiver);
        }
    }

    @Override
    public void destroy() {
        for(var entry : ids.entrySet()){
            Player player = Bukkit.getPlayer(entry.getKey());
            if(player == null) continue;
            entry.getValue().forEach(w -> w.destroy(player));
        }
        ids.clear();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String tag() {
        return tag;
    }

    private ArmorstandWrapper createHologramForPlayer(Player player, Component text, double ypos){
        ArmorstandWrapper armorStand = new ArmorstandWrapper();

        armorStand.setInvisible(true);
        armorStand.setCustomName(ComponentUtils.parseLegacyText(PlaceholderAPI.setPlaceholders(player,
                ComponentUtils.toLegacyText(text))));
        armorStand.setCustomNameVisible(true);
        armorStand.setSmall(true);
        armorStand.setMarker(true);

        armorStand.spawn(player, location.clone().set(location.getX(), ypos, location.getZ()));

        if(!ids.containsKey(player.getUniqueId())){
            ids.put(player.getUniqueId(), new ArrayList<>());
        }
        ids.get(player.getUniqueId()).add(armorStand);
        return armorStand;
    }

    @Override
    public void updateLine(int index, Component line) {
        if(index == -1) index = lines.size()-1;
        lines.set(index, line);
        for(UUID uuid : ids.keySet()){
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) continue;

            var armorStand = ids.get(player.getUniqueId()).get(index);
            armorStand.setCustomName(ComponentUtils.parseLegacyText(PlaceholderAPI.setPlaceholders(player,
                    ComponentUtils.toLegacyText(line))));
            armorStand.updateMeta(player);
        }
    }

    @Override
    public void deleteLine(int index) {
        lines.remove(index);
        for(var entry : ids.entrySet()){
            Player player = Bukkit.getPlayer(entry.getKey());
            if(player == null) continue;
            entry.getValue().get(index).destroy(player);
        }
    }

    @Override
    public void updateLineFor(Player recipient, int index, Component line) {
        throw new NotImplementedException();
//        if(index == -1) index = lines.size()-1;
//        if(ids.get(recipient.getUniqueId()).size() < index+1){
//            addNewLocalLine(location,player, text, index,totalLines);
//            return;
//        }
//
//        var armorStand = ids.get(recipient.getUniqueId()).get(index);
//        armorStand.setCustomName(PlaceholderAPI.setPlaceholders(player, text));
//        armorStand.updateMeta(recipient);
    }

    @Data
    public static class PlayerData{
        private boolean visible = true;
    }
}
