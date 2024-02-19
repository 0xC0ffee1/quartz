package bukkit.tests;

import bukkit.BukkitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TestBukkitModule extends BukkitTest {
    @Override
    public void onSetUp() {

    }

    @Test
    public void test(){
        Assertions.assertNotNull(getPlugin().getTestData("bukkit_service_discovery"), "Bukkit service discovery not working");
        Assertions.assertNotNull(getPlugin().getTestData("bukkit_inject"), "Bukkit plugin injection is not working");
    }
}
