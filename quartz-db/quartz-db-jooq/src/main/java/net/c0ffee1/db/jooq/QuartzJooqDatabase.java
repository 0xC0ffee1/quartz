package net.c0ffee1.db.jooq;

import net.c0ffee1.db.core.impl.CommonHikariDatabase;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.JDBCUtils;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;


public class QuartzJooqDatabase extends CommonHikariDatabase {
    protected DSLContext context;

    @Override
    public boolean initDatabase() {
        boolean result = super.initDatabase();
        context = DSL.using(getDataSource(), JDBCUtils.dialect(config.getType().getDriverName()));
        return result;
    }
}
