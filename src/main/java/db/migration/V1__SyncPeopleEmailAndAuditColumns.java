package db.migration;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
 * 既存の {@code people} に {@code email} / {@code created_at} / {@code updated_at} が無い場合だけ補う。
 * 空 DB でテーブル未作成のときは何もしない（この後 Hibernate が作成する）。
 * MySQL 以外では no-op（H2 テスト等は {@code spring.flyway.enabled=false} 推奨）。
 */
public class V1__SyncPeopleEmailAndAuditColumns extends BaseJavaMigration {

    private static final String TABLE_PEOPLE = "people";
    private static final String COL_EMAIL = "email";
    /** 移行前スキーマでの列名のみ。新規 DB では存在しない。 */
    private static final String COL_EMAIL_BEFORE_RENAME = "mail";

    @Override
    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        String product = connection.getMetaData().getDatabaseProductName();
        if (product == null || !product.toLowerCase().contains("mysql")) {
            return;
        }
        if (!tableExists(connection, TABLE_PEOPLE)) {
            return;
        }

        try (Statement st = connection.createStatement()) {
            if (columnExists(connection, TABLE_PEOPLE, COL_EMAIL_BEFORE_RENAME)
                    && !columnExists(connection, TABLE_PEOPLE, COL_EMAIL)) {
                st.execute(
                        "ALTER TABLE "
                                + TABLE_PEOPLE
                                + " CHANGE COLUMN "
                                + COL_EMAIL_BEFORE_RENAME
                                + " "
                                + COL_EMAIL
                                + " VARCHAR(200) NOT NULL");
            }

            if (!columnExists(connection, TABLE_PEOPLE, "created_at")) {
                st.execute("ALTER TABLE " + TABLE_PEOPLE + " ADD COLUMN created_at DATETIME(6) NULL");
            }
            if (!columnExists(connection, TABLE_PEOPLE, "updated_at")) {
                st.execute("ALTER TABLE " + TABLE_PEOPLE + " ADD COLUMN updated_at DATETIME(6) NULL");
            }

            st.execute(
                    "UPDATE "
                            + TABLE_PEOPLE
                            + " SET created_at = COALESCE(created_at, CURRENT_TIMESTAMP(6)), "
                            + "updated_at = COALESCE(updated_at, CURRENT_TIMESTAMP(6))");

            if (columnExists(connection, TABLE_PEOPLE, "created_at")) {
                st.execute("ALTER TABLE " + TABLE_PEOPLE + " MODIFY COLUMN created_at DATETIME(6) NOT NULL");
            }
            if (columnExists(connection, TABLE_PEOPLE, "updated_at")) {
                st.execute("ALTER TABLE " + TABLE_PEOPLE + " MODIFY COLUMN updated_at DATETIME(6) NOT NULL");
            }
        }
    }

    private static boolean tableExists(Connection c, String table) throws Exception {
        DatabaseMetaData md = c.getMetaData();
        String catalog = c.getCatalog();
        try (ResultSet rs = md.getTables(catalog, null, table, new String[] {"TABLE"})) {
            return rs.next();
        }
    }

    private static boolean columnExists(Connection c, String table, String column) throws Exception {
        DatabaseMetaData md = c.getMetaData();
        String catalog = c.getCatalog();
        try (ResultSet rs = md.getColumns(catalog, null, table, column)) {
            return rs.next();
        }
    }
}
