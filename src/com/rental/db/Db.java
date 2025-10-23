package com.rental.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Db {
    private static Connection connection;

    public static synchronized Connection get() {
        if (connection == null) {
            try {
                Properties props = new Properties();
                try (FileInputStream fis = new FileInputStream("config.properties")) {
                    props.load(fis);
                }
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String pass = props.getProperty("db.password", "");
                connection = DriverManager.getConnection(url, user, pass);
                connection.setAutoCommit(true);
                ensureSchema(connection);
            } catch (Exception e) {
                throw new RuntimeException("DB connection failed: " + e.getMessage(), e);
            }
        }
        return connection;
    }

    private static void ensureSchema(Connection conn) throws SQLException, IOException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "username VARCHAR(100) PRIMARY KEY," +
                    "password_hash VARCHAR(128) NOT NULL," +
                    "salt VARCHAR(64) NOT NULL," +
                    "role VARCHAR(20) NOT NULL" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS vehicles (" +
                    "id VARCHAR(100) PRIMARY KEY," +
                    "type VARCHAR(20) NOT NULL," +
                    "brand VARCHAR(100)," +
                    "model VARCHAR(100)," +
                    "year INT," +
                    "base_rate DOUBLE," +
                    "status VARCHAR(20)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS customers (" +
                    "id VARCHAR(100) PRIMARY KEY," +
                    "name VARCHAR(200)," +
                    "phone VARCHAR(50)," +
                    "email VARCHAR(200)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS contracts (" +
                    "id VARCHAR(100) PRIMARY KEY," +
                    "customer_id VARCHAR(100) NOT NULL," +
                    "vehicle_id VARCHAR(100) NOT NULL," +
                    "start_ts TIMESTAMP NOT NULL," +
                    "end_ts TIMESTAMP NULL," +
                    "status VARCHAR(20) NOT NULL," +
                    "total DOUBLE NOT NULL," +
                    "FOREIGN KEY (customer_id) REFERENCES customers(id)," +
                    "FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
        }
    }
}
