package net.c0ffee1.db.jooq.utils;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Meta;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JooqTableUtils {
    private static final Logger logger = LoggerFactory.getLogger(JooqTableUtils.class);
    // Check if a table exists
    public static boolean tableExists(DSLContext create, String tableName) {
        Meta meta = create.meta();
        return !meta.getTables(tableName).isEmpty();
    }

    // Ensure table exists, create if not
    public static void ensureTableExists(DSLContext create, String tableName) {
        Table<?> table = DSL.table(DSL.name(tableName));
        if (!tableExists(create, tableName)) {
            logger.info("Table does not exist, creating: " + tableName);
            create.createTable(table).columns(table.fields()).execute();
        }
        ensureColumnsExist(create, table);
    }

    // Ensure all columns exist, alter table if necessary
    public static void ensureColumnsExist(DSLContext create, Table<?> table) {
        List<String> existingColumns = getExistingColumns(create, table);
        List<Field<?>> missingFields = getMissingFields(table, existingColumns);

        for (Field<?> field : missingFields) {
            logger.info("Altering table " + table.getName() + " to add column " + field.getName());
            create.alterTable(table).addColumn(field).execute();
        }
    }

    // Get list of existing columns from a table
    private static List<String> getExistingColumns(DSLContext create, Table<?> table) {
        Meta meta = create.meta();
        return meta.getTables(table.getName()).stream()
                .flatMap(t -> Arrays.stream(t.fields()))
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    // Identify missing fields from the expected schema
    private static List<Field<?>> getMissingFields(Table<?> table, List<String> existingColumns) {
        return Stream.of(table.fields())
                .filter(field -> !existingColumns.contains(field.getName()))
                .collect(Collectors.toList());
    }
}