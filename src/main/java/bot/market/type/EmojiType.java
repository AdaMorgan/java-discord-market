package bot.market.type;

import net.dv8tion.jda.api.entities.emoji.Emoji;

public enum EmojiType {

    BEGIN("begin", 1L),
    ENDING("ending", 1L);

    private final String name;
    private final long code;

    EmojiType(String name, long code) {
        this.name = name;
        this.code = code;
    }

    public Emoji fromUnicode() {
        return Emoji.fromCustom(this.name, this.code, false);
    }
}