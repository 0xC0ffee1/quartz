package net.c0ffee1.mock.bukkit;

import net.c0ffee1.quartz.platforms.bukkit.QuartzBukkitPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.HashMap;

public class TestBukkitPlugin extends JavaPlugin implements QuartzBukkitPlugin {
    private HashMap<String, String> testData = new HashMap<>();

    @Override
    public void onEnable() {
        initQuartz(TestBukkitPlugin.class);
    }

    public void setTestData(String key, String value){
        testData.put(key, value);
    }
    public String getTestData(String key){
        return testData.get(key);
    }

    @Override
    public Path getDataFolderPath() {
        return Path.of("");
    }

    @Override
    public Logger getSlf4jLogger() {
        return getSLF4JLogger();
    }
}
