package com.rybak.andersenhw3.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    static {
        String url = System.getProperty("DB.URL");
        String username = System.getProperty("DB.USERNAME");
        String password = System.getProperty("DB.PASSWORD");

        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);

        dataSource = new HikariDataSource(config);

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();

        flyway.migrate();
    }

    private DataSource() {
        throw new IllegalStateException("Utility class");
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
