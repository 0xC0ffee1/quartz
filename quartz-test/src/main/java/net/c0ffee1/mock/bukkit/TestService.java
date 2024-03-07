package net.c0ffee1.mock.bukkit;

import com.google.inject.Inject;
import net.c0ffee1.quartz.core.annotations.OnConfigReload;
import net.c0ffee1.quartz.core.annotations.PostRegister;
import net.c0ffee1.quartz.core.annotations.Service;
import net.c0ffee1.quartz.core.annotations.UsesConfig;
import net.c0ffee1.quartz.core.config.ConfigManager;

@UsesConfig({TestConfig.class})
@Service
public class TestService {

    @Inject
    private TestBukkitPlugin plugin;

    @Inject
    private TestConfig testConfig;

    @Inject
    private TestConfig testConfig2;

    @Inject
    private ConfigManager configManager;


    @PostRegister
    public void onRegister(){
        if(plugin == null) return;
        plugin.setTestData("bukkit_inject", "ok");
        plugin.setTestData("bukkit_service_discovery", "ok");
        plugin.setTestData("config_test_string", testConfig.getTestString());
        configManager.reloadAllConfigs();
        assert testConfig.id.equals(testConfig2.id);
    }

    @OnConfigReload
    public void configLoad(){
        System.out.println("Configs reloaded!");
    }
}
