package bukkit;

import net.c0ffee1.quartz.platforms.bukkit.QuartzBukkitPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

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
    public @NotNull Logger getSLF4JLogger() {
        return super.getSLF4JLogger();
    }
}
