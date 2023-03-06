package core.config;

import com.moandjiezana.toml.Toml;

import java.io.File;

public interface Config {
    private static Toml getFile() {
        return new Toml().read(new File("config.toml"));
    }

    static String getToken() {
        return getFile().getString("discord.token");
    }

    static String getDriver() {
        return getFile().getString("database.postgres.driver");
    }

    static String getName() {
        return getFile().getString("database.postgres.name");
    }

    static String getHost() {
        return getFile().getString("database.postgres.host");
    }

    static String getUser() {
        return getFile().getString("database.postgres.user");
    }

    static String getPassword() {
        return getFile().getString("database.postgres.password");
    }
}
