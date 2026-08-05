package by.tms.ecommerceprojectc41onl.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionManager {

    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_POOL_MAXIMUM_POOL_SIZE = "db.pool.maximumPoolSize";

    private static final HikariDataSource DATA_SOURCE = createDataSource();

    private ConnectionManager() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            throw new SQLException("Failed to get database connection from HikariCP pool. Check application.properties.", e);
        }
    }

    private static HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(ApplicationProperties.loadProperties(DB_URL));
        config.setUsername(ApplicationProperties.loadProperties(DB_USER));
        config.setPassword(ApplicationProperties.loadProperties(DB_PASSWORD));
        config.setMaximumPoolSize(getRequiredPositiveIntProperty(DB_POOL_MAXIMUM_POOL_SIZE));

        return new HikariDataSource(config);
    }

    private static int getRequiredPositiveIntProperty(String key) {
        String value = ApplicationProperties.loadProperties(key);
        int number = parseIntProperty(key, value);

        if (number <= 0) {
            throw new IllegalStateException("Property " + key + " must be positive");
        }

        return number;
    }

    private static int parseIntProperty(String key, String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Property " + key + " must be a number", e);
        }
    }
}
