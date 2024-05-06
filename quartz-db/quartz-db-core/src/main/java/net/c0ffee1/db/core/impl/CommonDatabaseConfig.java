package net.c0ffee1.db.core.impl;

import lombok.Getter;
import net.c0ffee1.db.core.DatabaseType;
import net.c0ffee1.db.core.api.DatabaseConfig;
import net.c0ffee1.quartz.core.annotations.Config;

@Config(type = "toml", node = "database", file = "config")
@Getter
public class CommonDatabaseConfig implements DatabaseConfig {
    private String username, password;
    private String host;
    private int port;
    private DatabaseType type;
    private String name;
}
