package net.c0ffee1.quartz.core.commands;

import java.util.HashMap;

public interface CommandManager <T extends QuartzCommand<?>> {
    void register(T command);

    void unregister(T command);
}
