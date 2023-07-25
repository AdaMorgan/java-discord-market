package bot.utils;

import bot.market.type.StatusType;
import net.dv8tion.jda.api.EmbedBuilder;

public class MessageUtil {
    public static StatusType getStatus(boolean state) {
        return state ? StatusType.BEGIN : StatusType.ENDING;
    }

    public static EmbedBuilder intercepted(int value) {
        return new EmbedBuilder()
                .setDescription("Your deal has been hijacked with a big offer " + value);
    }
}
