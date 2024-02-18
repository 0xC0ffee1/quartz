package net.c0ffee1.quartz.core.commands;

import java.util.HashMap;

public class AbstractCommandManager <T extends QuartzCommand<?>> implements CommandManager<T> {

    protected HashMap<String, T> commands = new HashMap<>();

    @Override
    public void register(T command) {
        commands.put(command.getName(), command);
        command.register();
    }

    @Override
    public void unregister(T command) {
        commands.remove(command.getName());
        command.unregister();
    }
}
