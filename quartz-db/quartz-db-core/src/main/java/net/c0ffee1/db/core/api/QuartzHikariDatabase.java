package net.c0ffee1.db.core.api;

import com.zaxxer.hikari.HikariDataSource;

public interface QuartzHikariDatabase extends QuartzDatabase{

    HikariDataSource getDataSource();
}
