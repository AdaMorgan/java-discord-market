package core.database;

import core.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    public static Connection getConnect() throws SQLException {
        return DriverManager.getConnection(getPattern(), getConfig().getUser(), getConfig().getPassword());
    }

    private static String getPattern() {
        return String.format("%s://%s/%s", getConfig().getDriver(), getConfig().getHost(), getConfig().getName());
    }

    private static Config getConfig() {
        return new Config();
    }
}
