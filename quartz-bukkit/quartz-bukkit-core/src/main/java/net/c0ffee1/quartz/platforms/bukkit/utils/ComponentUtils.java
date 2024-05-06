package net.c0ffee1.quartz.platforms.bukkit.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ComponentUtils {

    public static TextComponent parseLegacyText(String text){
        if(text == null) return null;
        if(text.isEmpty()) return Component.text("");
        text = text.replace("§", "&");
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text).
                decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static String componentsToGson(Collection<Component> components){
        StringBuilder result = new StringBuilder();
        for(Component component : components){
            result.append(GsonComponentSerializer.gson().serialize(component));
            result.append("/n");
        }
        return result.toString();
    }

    public static List<Component> gsonToComponents(String gson){
        String[] splits = gson.split("/n");
        List<Component> components = new ArrayList<>();
        for(String split : splits){
            split = split.trim();
            if(split.isEmpty()) continue;
            Component component = GsonComponentSerializer.gson().deserialize(split);
            components.add(component);
        }
        return components;
    }

    public static String toLegacyText(Component component){
        return LegacyComponentSerializer.legacySection().serialize(component);
    }
}
