package bot.market.type;

import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.awt.*;

public enum StatusType {

    BEGIN("#5865F2", EmojiType.BEGIN.fromUnicode()),
    ENDING("#ED4245", EmojiType.ENDING.fromUnicode());

    private final Emoji emoji;
    private final Color color;

    StatusType(String color, Emoji emoji) {
        this.color = Color.getColor(color);
        this.emoji = emoji;
    }

    public Color getColor() {
        return this.color;
    }

    public Emoji getEmoji() {
        return this.emoji;
    }
}
