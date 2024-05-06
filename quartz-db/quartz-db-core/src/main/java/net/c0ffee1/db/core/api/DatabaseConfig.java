package net.c0ffee1.db.core.api;

import net.c0ffee1.db.core.DatabaseType;

public interface DatabaseConfig {
    String getHost();
    String getName();
    int getPort();
    String getPassword();
    String getUsername();
    DatabaseType getType();
}
