package bot.market.type;

import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.awt.*;

public enum StatusType {

    BEGIN("Expires: ", new Color(88, 101, 242), EmojiType.BEGIN.fromUnicode()),
    ENDING("ENDING SOON! ", new Color(237, 66, 69), EmojiType.ENDING.fromUnicode());


    private final String text;
    private final Emoji emoji;
    private final Color color;

    StatusType(String text, Color color, Emoji emoji) {
        this.text = text;
        this.color = color;
        this.emoji = emoji;
    }

    public String getText() {
        return this.text;
    }

    public Color getColor() {
        return this.color;
    }

    public Emoji getEmoji() {
        return this.emoji;
    }
}
