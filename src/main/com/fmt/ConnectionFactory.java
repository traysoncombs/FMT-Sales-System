package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Factory to build connections to a database.
 */
public final class ConnectionFactory {
    private static final String username = "";
    private static final String password = "";
    private static final String url = "";


    /**
     * Creates a connection.
     * @return Connection
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
}
