package core.message;

import core.Timer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

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
            event.getChannel().sendMessage("@root").setEmbeds(messageEmbed(this.TIME = 5, 0, "NaN")).queue(message -> {
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
//            if (event.getUserIdLong() == this.AUTHOR_ID)
//                event.getUser().openPrivateChannel().complete().sendMessage("AUTHOR!").queue();

            if (event.getUserIdLong() == this.LEADER_ID)
                event.getUser().openPrivateChannel().complete().sendMessage("LEADER!").queue();

            // event.getUserIdLong() != this.AUTHOR_ID &&
            if (event.getUserIdLong() != this.LEADER_ID) {
                this.LEADER_ID = event.getUserIdLong();

                switch (event.getEmoji().getAsReactionCode()) {
                    case "\uD83D\uDFE9" -> editMessageEmbeds(event, this.CURRENT += 1);
                    case "\uD83D\uDFE8" -> editMessageEmbeds(event, this.CURRENT += 10);
                    case "\uD83D\uDFE5" -> editMessageEmbeds(event, this.CURRENT += 100);
                    //case "" -> leaveAuction();
                }
            }
        }
    }

    private void editMessageEmbeds(MessageReactionAddEvent event, int count) {
        event.getChannel().editMessageEmbedsById(this.MESSAGE_ID, messageEmbed(this.TIME, count, this.USERNAME = event.getUser().getName())).queue();
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
