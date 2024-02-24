package bukkit.tests;

import net.c0ffee1.mock.BukkitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TestBukkitModule extends BukkitTest {

    @Test
    public void test(){
        Assertions.assertNotNull(getPlugin().getTestData("bukkit_service_discovery"), "Bukkit service discovery not working");
        Assertions.assertNotNull(getPlugin().getTestData("bukkit_inject"), "Bukkit plugin injection is not working");
        Assertions.assertEquals("hello world", getPlugin().getTestData("config_test_string"), "Config loading is not working");
    }
}
