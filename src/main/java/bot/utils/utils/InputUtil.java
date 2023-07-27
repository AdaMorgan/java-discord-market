package bot.utils.utils;

import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

public class InputUtil {
    @NotNull
    public static TextInput.Builder create(String id, String label, String placeholder) {
        return TextInput.create(id, label, TextInputStyle.SHORT)
                .setMinLength(0)
                .setMaxLength(100)
                .setRequired(true)
                .setPlaceholder(placeholder);
    }

    @NotNull
    public static Modal auction() {
        return Modal.create("bot:auction", "Create Auction Item")
                .addActionRow(InputUtil.create("bot:auction", "Auction", "item").build())
                .build();
    }

    @NotNull
    public static Modal market() {
        return Modal.create("bot:market", "Create Market Item")
                .addActionRow(InputUtil.create("bot:market", "Market", "item").build())
                .build();
    }
}
