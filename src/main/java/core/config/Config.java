package core.config;

import com.moandjiezana.toml.Toml;

import java.io.File;


//Toml
public class Config {
    private static final String FILE = "config.toml";

    private static Toml getFile() {
        return new Toml().read(new File(FILE));
    }

    public static String getToken() {
        return getFile().getString("discord.token");
    }

    public static String getHostname() {
        return getFile().getString("database.redis.host");
    }

    public static Long getPort() {
        return getFile().getLong("database.redis.port");
    }
}
