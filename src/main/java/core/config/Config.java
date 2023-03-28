package core.config;

import com.moandjiezana.toml.Toml;

import java.io.File;
import java.util.function.Function;

public interface Config {
    Function<String, String> getKey = key -> new Toml().read(new File("config.toml")).getString(key);

    static String getToken() {
        return getKey.apply("discord.token");
    }

    static String getDriver() {
        return getKey.apply("database.postgres.driver");
    }

    static String getName() {
        return getKey.apply("database.postgres.name");
    }

    static String getHost() {
        return getKey.apply("database.postgres.host");
    }

    static String getUser() {
        return getKey.apply("database.postgres.user");
    }

    static String getPassword() {
        return getKey.apply("database.postgres.password");
    }
}
