package core.database;

import core.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    public static Connection getConnect() throws SQLException {
        return DriverManager.getConnection(getPattern(), Config.getUser(), Config.getPassword());
    }

    private static String getPattern() {
        return String.format("%s://%s/%s", Config.getDriver(), Config.getHost(), Config.getName());
    }
}
