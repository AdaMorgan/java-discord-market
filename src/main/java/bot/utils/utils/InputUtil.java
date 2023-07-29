package bot.utils.utils;

import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

public class InputUtil {
    @NotNull
    private static TextInput.Builder title(String id) {
        return TextInput.create(id, "item", TextInputStyle.SHORT)
                .setMinLength(0)
                .setMaxLength(100)
                .setRequired(true)
                .setPlaceholder("item");
    }

    @NotNull
    private static TextInput.Builder description(String id) {
        return TextInput.create(id, "description", TextInputStyle.SHORT)
                .setMinLength(0)
                .setMaxLength(800)
                .setRequired(true)
                .setPlaceholder("description");
    }

    @NotNull
    private static TextInput.Builder price(String id, String label) {
        return TextInput.create(id, label, TextInputStyle.SHORT)
                .setMinLength(0)
                .setMaxLength(15)
                .setRequired(true)
                .setPlaceholder(label);
    }

    @NotNull
    private static TextInput.Builder time(String id) {
        return TextInput.create(id, "hours", TextInputStyle.SHORT)
                .setMinLength(0)
                .setMaxLength(15)
                .setRequired(true)
                .setPlaceholder("hours");
    }

    @NotNull
    public static Modal auction() {
        return Modal.create("bot:auction", "Create Auction Item")
                .addActionRow(title("bot:auction.title").build())
                .addActionRow(description("bot:auction.description").build())
                .addActionRow(price("bot:auction.price", "Start price").build())
                .addActionRow(time("bot:auction.time").build())
                .build();
    }

    @NotNull
    public static Modal market() {
        return Modal.create("bot:market", "Create Market Item")
                .addActionRow(title("bot:market.title").build())
                .addActionRow(description("bot:market.description").build())
                .addActionRow(price("bot:market.price", "price").build())
                .addActionRow(time("bot:market.time").build())
                .build();
    }
}
