package net.c0ffee1.mock.bukkit;

import com.google.inject.Inject;
import net.c0ffee1.quartz.core.annotations.Command;
import net.c0ffee1.quartz.core.annotations.PostRegister;
import net.c0ffee1.quartz.core.config.ConfigManager;
import net.c0ffee1.quartz.platforms.bukkit.QuartzBukkitCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@Command
public class TestCommand extends QuartzBukkitCommand {

    @Inject
    private TestBukkitPlugin plugin;

    protected TestCommand() {
        super("test", "Test command", "/test", "test.test");
    }

    @PostRegister
    public void emulateExecution(){
        execute(Bukkit.getConsoleSender(), "", new String[]{});
    }
    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        plugin.setTestData("cmd", "1");
        return true;
    }
}
