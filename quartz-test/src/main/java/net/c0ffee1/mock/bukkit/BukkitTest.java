package net.c0ffee1.mock.bukkit;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BukkitTest {
    private ServerMock server;
    private TestBukkitPlugin plugin;

    @BeforeEach
    public void setUp()
    {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(TestBukkitPlugin.class);
        onSetUp();
    }

    public ServerMock getServer() {
        return server;
    }

    public TestBukkitPlugin getPlugin() {
        return plugin;
    }

    public void onSetUp(){

    }

    @AfterEach
    public void tearDown()
    {
        MockBukkit.unmock();
    }
}
