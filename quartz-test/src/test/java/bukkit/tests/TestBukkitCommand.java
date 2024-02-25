package bukkit.tests;

import com.google.inject.Inject;
import net.c0ffee1.mock.bukkit.BukkitTest;
import net.c0ffee1.mock.bukkit.TestCommand;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

public class TestBukkitCommand extends BukkitTest {

    @Test
    public void test(){
        Assertions.assertNotNull(getPlugin().getTestData("cmd"));
    }
}
