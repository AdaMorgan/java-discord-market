package core.config;

import com.moandjiezana.toml.Toml;

import java.io.File;

public class Config {
    private static Toml getFile() {
        return new Toml().read(new File("config.toml"));
    }

    public static String getToken() {
        return getFile().getString("discord.token");
    }

    public String getDriver() {
        return getFile().getString("database.postgres.driver");
    }

    public String getName() {
        return getFile().getString("database.postgres.name");
    }

    public String getHost() {
        return getFile().getString("database.postgres.host");
    }

    public String getUser() {
        return getFile().getString("database.postgres.user");
    }

    public String getPassword() {
        return getFile().getString("database.postgres.password");
    }
}
