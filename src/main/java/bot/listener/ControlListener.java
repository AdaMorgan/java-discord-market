package bot.listener;

import bot.main.Application;
import bot.market.Auction;
import bot.market.Controller;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class ControlListener extends ListenerAdapter {
    private final Application app;

    public ControlListener(Application app) {
        this.app = app;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String[] id = event.getComponentId().split(":");

        if (!id[0].equals("lot")) return;

        switch (id[1]) {
            case "1" -> requireController(event, event.getMessage(), auction -> auction.bid(event.getUser(), 1));
            case "10" -> requireController(event, event.getMessage(), auction -> auction.bid(event.getUser(), 10));
            case "100" -> requireController(event, event.getMessage(), auction -> auction.bid(event.getUser(), 100));
            case "leave" -> requireController(event, event.getMessage(), auction -> auction.leave(event.getUser()));
        }

        if (!event.isAcknowledged()) event.deferEdit().queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        if (event.getMessage().getContentRaw().equals("!auction")) {
            add(getController(event.getGuild()), new Auction(event.getMessage(), "item", 100, new Date(), event.getAuthor()), event.getMessage(), event.getChannel());
        }
    }

    private void add(Controller controller, Auction auction, Message message, MessageChannel channel) {
        controller.lots.put(message.getIdLong(), auction);
        channel.sendMessageEmbeds(message(auction))
                .setComponents(buttons())
                .queue();
    }

    private synchronized MessageEmbed message(Auction auction) {
        return new EmbedBuilder()
                .addField("**Starting** @\n", String.valueOf(auction.start), true)
                .addField("**Current Bid:**\n", String.valueOf(auction.current), true)
                .build();
    }

    private List<ActionRow> buttons() {
        return List.of(
                ActionRow.of(
                        Button.primary("1", "+1"),
                        Button.primary("10", "+10"),
                        Button.primary("100", "+100"),
                        Button.primary("leave", "leave")
                )
        );
    }

    private Controller getController(@NotNull Guild guild) {
        return this.app.manager.controllers.get(guild.getIdLong());
    }

    @NotNull
    private Auction getAuction(@NotNull Guild guild, Message message) {
        return getController(guild).lots.get(message.getIdLong());
    }

    private void requireController(@NotNull IReplyCallback event, Message message, Consumer<Auction> handler) {
        handler.accept(getAuction(event.getGuild(), message));
    }
}
