package net.c0ffee1.quartz.platforms.bukkit.command;

import com.google.inject.Inject;
import net.c0ffee1.quartz.core.Quartz;
import net.c0ffee1.quartz.core.annotations.PostRegister;
import net.c0ffee1.quartz.core.commands.QuartzCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuartzBukkitCommand extends BukkitCommand implements QuartzCommand<CommandSender> {
    protected boolean enabled = true;

    @Inject
    protected JavaPlugin bukkitPlugin;

    protected QuartzBukkitCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull String permission) {
        super(name, description, usageMessage, Collections.emptyList());
        setPermission(permission);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void removeCommand(String name){
        if(Bukkit.getCommandMap().getKnownCommands().containsKey(name)) {
            Bukkit.getCommandMap().getCommand(name).unregister(Bukkit.getCommandMap());
            Bukkit.getCommandMap().getKnownCommands().remove(name);
        }
    }

    @PostRegister
    @Override
    public void register() {
        for(String alias : new ArrayList<>(getAliases())){
            Bukkit.getCommandMap().getKnownCommands().remove(alias);
        }
        removeCommand(getName());
        bukkitPlugin.getSLF4JLogger().debug("Registered command " + getName());

        boolean registered = Bukkit.getCommandMap().register(getName(), bukkitPlugin.getName(), this);
    }

    @Override
    public void unregister() {
        Bukkit.getCommandMap().getKnownCommands().remove(getName());
        getAliases().forEach(alias -> {
            Bukkit.getCommandMap().getKnownCommands().remove(alias);
        });
    }

    @Override
    public List<? extends QuartzCommand<CommandSender>> getSubCommands() {
        return null;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        return false;
    }
}
