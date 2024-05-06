package net.c0ffee1.db.core.api;

public interface HikariDatabaseConfig extends DatabaseConfig{
    int getMaxPoolSize();
    boolean isAutoCommit();
}
