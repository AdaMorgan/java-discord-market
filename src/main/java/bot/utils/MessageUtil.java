package bot.utils;

import net.dv8tion.jda.api.EmbedBuilder;

public class MessageUtil {
    public static EmbedBuilder intercepted(int value) {
        return new EmbedBuilder()
                .setDescription("Your deal has been hijacked with a big offer " + value);
    }
}
