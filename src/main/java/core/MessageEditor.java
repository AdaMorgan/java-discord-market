package core;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class MessageEditor extends ListenerAdapter {
    private long AUTHOR_ID, LEADER_ID, PASSED_ID;

    private long MESSAGE_ID = 0L;
    private Integer STARTING = 10;
    private Integer CURRENT = STARTING;

    private Date DATE;
    private int HOUR, MINUTE, SECOND;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equalsIgnoreCase("test")) {
            this.AUTHOR_ID = event.getAuthor().getIdLong();
            event.getChannel().sendMessageEmbeds(messageEmbed(this.CURRENT, "NaN")).queue(message -> {
                this.MESSAGE_ID = message.getIdLong();
                message.addReaction(Emoji.fromUnicode("\uD83D\uDFE9")).queue();
                message.addReaction(Emoji.fromUnicode("\uD83D\uDFE8")).queue();
                message.addReaction(Emoji.fromUnicode("\uD83D\uDFE5")).queue();
            });
        }
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (!event.getUser().isBot()) {
            event.getReaction().removeReaction(event.getUser()).queue();
            if (event.getUserIdLong() == this.AUTHOR_ID)
                event.getUser().openPrivateChannel().complete().sendMessage("AUTHOR!").queue();

            if (event.getUserIdLong() == this.LEADER_ID)
                event.getUser().openPrivateChannel().complete().sendMessage("LEADER!").queue();

            if (event.getUserIdLong() != this.AUTHOR_ID && event.getUserIdLong() != this.LEADER_ID) {
                this.LEADER_ID = event.getUserIdLong();
                System.out.println(LEADER_ID);
                System.out.println(PASSED_ID);
                editMessageEmbedsAuthor(event, event.getUser().getName());
                switch (event.getEmoji().getAsReactionCode()) {
                    case "\uD83D\uDFE9" -> editMessageEmbedsCount(event, this.CURRENT += 1);
                    case "\uD83D\uDFE8" -> editMessageEmbedsCount(event, this.CURRENT += 10);
                    case "\uD83D\uDFE5" -> editMessageEmbedsCount(event, this.CURRENT += 100);
                }
            }
        }
    }

    private void editMessageEmbedsAuthor(MessageReactionAddEvent event, String username) {
        event.getChannel().editMessageEmbedsById(this.MESSAGE_ID, messageEmbed(this.CURRENT, username)).queue();
    }

    private void editMessageEmbedsCount(MessageReactionAddEvent event, int count) {
        event.getChannel().editMessageEmbedsById(this.MESSAGE_ID, messageEmbed(count, event.getUser().getName())).queue();
    }

    private MessageEmbed messageEmbed(int count, String username) {
        String value = "NaN";
        if (STARTING != count) value = String.valueOf(count);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(username);
        embedBuilder.setDescription("Starting: " + this.STARTING + "\n" + "Current bid: " + value);
        embedBuilder.setTimestamp(new Date().toInstant());
        return embedBuilder.build();
    }
}
