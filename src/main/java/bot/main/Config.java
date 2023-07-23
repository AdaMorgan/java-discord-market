package bot.main;

import com.moandjiezana.toml.Toml;

import java.io.FileReader;

public class Config {
    private final Toml content;

    public Config(Toml toml) {
        this.content = toml;
    }

    public String getToken() {
        return content.getString("discord.token");
    }

    public static Config readFromFile(String path) {
        try(FileReader reader = new FileReader(path)) {
            return new Config(new Toml().read(reader));
        } catch(Exception exception) {
            throw new RuntimeException("Failed to read config", exception);
        }
    }
}
