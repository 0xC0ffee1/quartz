package bukkit.tests;

import bukkit.TestBukkitPlugin;
import com.google.inject.Inject;
import net.c0ffee1.quartz.core.annotations.PostRegister;
import net.c0ffee1.quartz.core.annotations.Service;
import org.bukkit.plugin.java.JavaPlugin;

@Service
public class TestService {

    @Inject
    private TestBukkitPlugin plugin;

    @PostRegister
    public void onRegister(){
        if(plugin == null) return;
        plugin.setTestData("bukkit_inject", "ok");
        plugin.setTestData("bukkit_service_discovery", "ok");
    }
}
