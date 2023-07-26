package bot.main;

import com.moandjiezana.toml.Toml;

import java.io.FileReader;

public class Config {
    private final Toml content;

    public Config(Toml toml) {
        this.content = toml;
    }

    public String getToken() {
        return content.getString("bot.token");
    }

    public int getGuildLimit() {
        return content.getLong("bot.limit", 1000L).intValue();
    }

    public int getItemLimit() {
        return content.getLong("user.limit", 5L).intValue();
    }

    public static Config readFromFile(String path) {
        try (FileReader reader = new FileReader(path)) {
            return new Config(new Toml().read(reader));
        } catch(Exception exception) {
            throw new RuntimeException("Failed to read config", exception);
        }
    }
}
