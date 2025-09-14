package tech.zeta.restaurant.project.util;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.RestaurantManagement;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class DBConnection {
    // Thread-Safe Singleton
    private static DBConnection instance;
    private static Properties properties = new Properties();

    private final String db_class_name;
    private final String db_url;
    private final String db_user_name;
    private final String db_password;

    private DBConnection() throws IOException {
        try (InputStream inputStream = RestaurantManagement.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream == null) {
                log.error("Failed to fetch data from application.properties");
                throw new IOException("application.properties not found");
            }
            properties.load(inputStream);

            this.db_class_name = properties.getProperty("db_class_name");
            this.db_url = properties.getProperty("db_url");
            this.db_user_name = properties.getProperty("db_user_name");
            this.db_password = properties.getProperty("db_password");

            try {
                Class.forName(db_class_name); // load driver class
            } catch (ClassNotFoundException e) {
                log.error("Failed to load DB driver: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            log.error("Got an exception during DBConnection init: " + e.getMessage());
            throw new IOException("Database connection initialization failed.", e);
        }
    }

    // thread-safe singleton accessor
    public static DBConnection getInstance() throws IOException {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    // return a fresh connection every time
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(db_url, db_user_name, db_password);
    }
}
