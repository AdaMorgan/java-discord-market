package bot.utils.utils;

import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.jetbrains.annotations.NotNull;

public class InputUtil {
    @NotNull
    public static TextInput create(String id, String label, String placeholder) {
        return TextInput.create(id, label, TextInputStyle.SHORT)
                .setMinLength(0)
                .setMaxLength(100)
                .setRequired(true)
                .setPlaceholder(placeholder)
                .build();
    }
}
