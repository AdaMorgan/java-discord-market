package core.config;

import com.moandjiezana.toml.Toml;

import java.awt.*;
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

    public Color getColorBlue() {
        return new Color(123, 227, 221);
    }

    public Color getColorRed() {
        return new Color(205, 18, 71);
    }
}
