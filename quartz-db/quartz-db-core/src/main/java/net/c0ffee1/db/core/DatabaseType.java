package net.c0ffee1.db.core;

public enum DatabaseType {
    MYSQL("jdbc:mysql://{host}:{port}/{name}?characterEncoding=UTF-8&useUnicode=true", "com.mysql.cj.jdbc.Driver"),
    H2("jdbc:h2:./{name};MODE=MYSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE", "org.h2.Driver"),
    SQLITE("jdbc:sqlite:{name}.db", "org.sqlite.JDBC"),
    POSTGRESQL("jdbc:postgresql://{host}:{port}/{name}?characterEncoding=UTF-8", "org.postgresql.Driver");

    private final String jdbcString;
    private final String driverName;

    DatabaseType(String jdbcString, String driverName){
        this.jdbcString = jdbcString;
        this.driverName = driverName;
    }

    public String getJdbcString(String host, String port, String databaseName) {
        String copy = jdbcString;
        copy = copy.replace("{host}", host == null ? "" : host);
        copy = copy.replace("{port}", port == null ? "" : port);
        copy = copy.replace("{name}", databaseName == null ? "" : databaseName);

        return copy;
    }

    public String getDriverName() {
        return driverName;
    }
}
