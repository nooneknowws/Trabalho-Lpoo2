package com.br.ufpr.lpoo2.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Wrapper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class ConnectionFactory {
    
    private static final HikariDataSource dataSource;
    static {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://localhost:5432/lpoo2final");
        config.setUsername("postgres");
        config.setPassword("admin");
        config.setDriverClassName("org.postgresql.Driver");
        dataSource = new HikariDataSource(config);

    }
    private ConnectionFactory() {}

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();

    }
}
