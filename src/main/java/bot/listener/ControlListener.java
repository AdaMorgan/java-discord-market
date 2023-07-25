package bot.listener;

import bot.main.Application;
import bot.market.Auction;
import bot.market.Controller;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ControlListener extends ListenerAdapter {
    private final Application app;
    private final Map<Long, Controller> controllers;

    public ControlListener(Application app) {
        this.app = app;
        this.controllers = this.app.controller.controllers;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String[] id = event.getComponentId().split(":");

        if (!id[0].equals("bid")) return;

        if (getController(event.getGuild()).lots.containsKey(event.getMessage().getIdLong())) {
            switch (id[1]) {
                case "1" -> requireController(event, event.getMessage(), auction -> auction.bid(event, 1));
                case "10" -> requireController(event, event.getMessage(), auction -> auction.bid(event, 10));
                case "100" -> requireController(event, event.getMessage(), auction -> auction.bid(event, 100));
                case "leave" -> requireController(event, event.getMessage(), auction -> auction.leave(event.getUser()));
            }
        }

        if (!event.isAcknowledged()) event.deferEdit().queue();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        if (event.getMessage().getContentRaw().equals("!auction")) {
            add(getController(event.getGuild()), event.getChannel(), event.getAuthor());
        }
    }

    private void add(Controller controller, @NotNull MessageChannel channel, User user) {
        channel.sendMessage("")
                .setComponents(buttons())
                .queue(message -> createAuction(controller, message, new Auction(controller, message, "item", 100, 5, user)));
    }

    private void createAuction(Controller controller, Message message, Auction item) {
        message.editMessageEmbeds(item.message()).queue();
        controller.lots.put(message.getIdLong(), item);
    }

    private List<ActionRow> buttons() {
        return List.of(
                ActionRow.of(
                        Button.primary("bid:1", "+1"),
                        Button.primary("bid:10", "+10"),
                        Button.primary("bid:100", "+100"),
                        Button.primary("bid:leave", "leave")
                )
        );
    }

    private Controller getController(@NotNull Guild guild) {
        return this.controllers.get(guild.getIdLong());
    }

    @NotNull
    private Auction getAuction(@NotNull Guild guild, Message message) {
        return getController(guild).lots.get(message.getIdLong());
    }

    private void requireController(@NotNull IReplyCallback event, Message message, Consumer<Auction> handler) {
        handler.accept(getAuction(event.getGuild(), message));
    }
}
