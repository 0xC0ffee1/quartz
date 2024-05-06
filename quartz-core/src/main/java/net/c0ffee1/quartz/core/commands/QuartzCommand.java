package net.c0ffee1.quartz.core.commands;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface QuartzCommand<S> extends Registrable{

    void setEnabled(boolean enabled);

    boolean isEnabled();

    String getPermission();
    String getName();
    String getUsage();
    String getDescription();
    List<String> getAliases();

    List<? extends QuartzCommand<S>> getSubCommands();


    boolean execute(@NotNull S commandSender, @NotNull String alias, @NotNull String[] args) throws Exception;
}
