package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Factory to build connections to a database.
 */
public class ConnectionFactory {
    private final String url;
    private final String username;
    private final String password;

    public ConnectionFactory(String username, String password) {
        this.username = username;
        this.password = password;
        this.url = "jdbc:mysql://cse.unl.edu/" + username + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    }

    /**
     * Creates a connection.
     * @return Connection
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
}
