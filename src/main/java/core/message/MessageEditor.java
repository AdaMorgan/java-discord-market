package core.message;

import core.Timer;
import core.database.Connect;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

//DISCORD
public class MessageEditor extends ListenerAdapter {
    private String USERNAME;
    private long AUTHOR_ID, LEADER_ID;

    private long MESSAGE_ID = 0L;
    private Integer STARTING = 10;
    private Integer CURRENT = STARTING;
    private int TIME = 0;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equalsIgnoreCase("test")) {
            this.AUTHOR_ID = event.getAuthor().getIdLong();
            createMessage(event);
            Adapter.messageReceivedEvent = event;
        }
    }

    private void createMessage(MessageReceivedEvent event) {
        event.getChannel().sendMessage("<@&1072447833153736784>").setEmbeds(messageEmbed(this.TIME = 5, 0, "NaN")).queue(message -> {
            this.MESSAGE_ID = message.getIdLong();
            message.addReaction(Emoji.fromUnicode("\uD83D\uDFE9")).queue();
            message.addReaction(Emoji.fromUnicode("\uD83D\uDFE8")).queue();
            message.addReaction(Emoji.fromUnicode("\uD83D\uDFE5")).queue();
            message.addReaction(Emoji.fromUnicode("\uD83D\uDFE1")).queue();
        });
    }

    private ResultSet createTable() throws SQLException {
        return Connect.getConnect().createStatement().executeQuery("CREATE TABLE " + this.MESSAGE_ID);
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
                getEmojiReaction(event);
            }
        }
    }

    private void getEmojiReaction(MessageReactionAddEvent event) {
        if (event.getUser() != null) {
            switch (event.getEmoji().getAsReactionCode()) {
                case "\uD83D\uDFE9" -> editMessageEmbeds(event, this.CURRENT += 1, event.getUser().getName());
                case "\uD83D\uDFE8" -> editMessageEmbeds(event, this.CURRENT += 10, event.getUser().getName());
                case "\uD83D\uDFE5" -> editMessageEmbeds(event, this.CURRENT += 100, event.getUser().getName());
                case "\uD83D\uDFE1" -> onAuctionLeave();
            }
        }
    }

    private void onAuctionLeave() {
        System.out.println("Work!!!");
    }

    private void editMessageEmbeds(MessageReactionAddEvent event, int count, String name) {
        event.getChannel().editMessageEmbedsById(this.MESSAGE_ID, messageEmbed(this.TIME, count, name)).queue();
    }

    private void editMessageEmbeds(MessageReceivedEvent event, int time) {
        event.getChannel().editMessageEmbedsById(this.MESSAGE_ID, messageEmbed(time, this.CURRENT, this.USERNAME)).queue();
    }

    private MessageEmbed messageEmbed(int timer, int count, String username) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(Timer.getTimer(timer));
        embedBuilder.addField("Starting @", this.STARTING.toString(), true);
        embedBuilder.addField("Current Bid:", String.valueOf(count), true);
        embedBuilder.setFooter(username);
        embedBuilder.setTimestamp(new Date().toInstant());
        return embedBuilder.build();
    }
}
