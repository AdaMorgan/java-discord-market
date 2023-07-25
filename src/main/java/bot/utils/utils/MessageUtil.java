package bot.utils.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageUtil {

    @NotNull
    @Contract("_ -> new")
    public static EmbedBuilder intercepted(int value) {
        return new EmbedBuilder()
                .setDescription("Your deal has been hijacked with a big offer " + value);
    }

    @NotNull
    public static List<ActionRow> getButtons() {
        return List.of(
                ActionRow.of(
                        Button.primary("bid:1", "+1"),
                        Button.primary("bid:10", "+10"),
                        Button.primary("bid:100", "+100"),
                        Button.primary("bid:leave", "leave")
                )
        );
    }
}
