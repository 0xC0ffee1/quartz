package net.c0ffee1.mock.bukkit;

import com.google.inject.Inject;
import net.c0ffee1.quartz.core.annotations.PostRegister;
import net.c0ffee1.quartz.core.annotations.Service;

@Service
public class TestService {

    @Inject
    private TestBukkitPlugin plugin;

    @Inject
    private TestConfig testConfig;

    @Inject
    private TestConfig testConfig2;


    @PostRegister
    public void onRegister(){
        if(plugin == null) return;
        plugin.setTestData("bukkit_inject", "ok");
        plugin.setTestData("bukkit_service_discovery", "ok");
        plugin.setTestData("config_test_string", testConfig.getTestString());
        assert testConfig.id.equals(testConfig2.id);
    }
}
