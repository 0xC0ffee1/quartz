package net.c0ffee1.db.core.impl;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import net.c0ffee1.db.core.api.DatabaseConfig;
import net.c0ffee1.db.core.api.HikariDatabaseConfig;
import net.c0ffee1.db.core.api.QuartzDatabase;
import net.c0ffee1.db.core.api.QuartzHikariDatabase;
import org.slf4j.LoggerFactory;

public class CommonHikariDatabase implements QuartzHikariDatabase {

    @Inject
    protected DatabaseConfig config;

    @Getter
    protected HikariDataSource dataSource;

    @Override
    public boolean initDatabase() {
        HikariConfig dbConfig = new HikariConfig();
        dbConfig.setJdbcUrl(config.getType().getJdbcString(config.getHost(), String.valueOf(config.getPort()), config.getName()));
        dbConfig.setDriverClassName(config.getType().getDriverName());
        dbConfig.setUsername(config.getUsername());
        dbConfig.setPassword(config.getPassword());

        if(config instanceof HikariDatabaseConfig hikariDatabaseConfig){
            dbConfig.setMaximumPoolSize(hikariDatabaseConfig.getMaxPoolSize());
            dbConfig.setAutoCommit(hikariDatabaseConfig.isAutoCommit());
        }
        try {
            dbConfig.validate();
        }
        catch (Exception e){
            LoggerFactory.getLogger(getClass().getSimpleName()).error("Failed to initialize database! "
                    + config.getName() + ": " + e);
            return false;
        }

        dataSource = new HikariDataSource(dbConfig);
        return true;
    }
}
