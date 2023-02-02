package core;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class CommandManager extends ListenerAdapter {

    // Guild command -- instantly updated (max 100)

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        ArrayList<CommandData> commandData = new ArrayList<>();

        commandData.add(Commands.slash("end", "Ends your auction if it hasn't received any bids"));

        commandData.add(Commands.slash("clear", "clear message"));
//        commandData.add(Commands.slash("auctions", "Check your Active Auctions"));
//        commandData.add(Commands.slash("bids", "Check your Active Bids"));
//        commandData.add(Commands.slash("wins", "Shows all auctions you’ve won since your given time"));
//        commandData.add(Commands.slash("bidders", "Incrementally reveals all unique bidders"));
//        commandData.add(Commands.slash("help", "links to this page!"));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    // Global command -- up to an hour to update (unlimited)

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.appendDescription("Require message");
        embedBuilder.build();

        if (event.getUser().isBot()) return;
//        if (event.getName().equals("end")) {
//            event.getGuild().getChannelById().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
//                message.addReaction(Emoji.fromUnicode("\uD83D\uDFE9")).queue();
//                message.addReaction(Emoji.fromUnicode("\uD83D\uDFE8")).queue();
//                message.addReaction(Emoji.fromUnicode("\uD83D\uDFE5")).queue();
//                message.addReaction(Emoji.fromUnicode("\uD83D\uDDD1")).queue();
//            });
//        }

        if (event.getName().equals("clear")) {
            for (Message message : event.getChannel().getIterableHistory()) {
                event.getChannel().deleteMessageById(message.getId()).queue();
            }
        }
    }
}
