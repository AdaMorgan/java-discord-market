package bot.listener;

import bot.utils.entity.AuctionEntity;
import bot.utils.Controller;
import bot.main.Application;
import bot.utils.type.ChannelType;
import bot.utils.utils.MessageUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ControlListener extends ListenerAdapter {
    private final Map<Long, Controller> controllers;

    public ControlListener(@NotNull Application app) {
        this.controllers = app.controller.controllers;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String[] id = event.getComponentId().split(":");

        if (!id[0].equals("bid")) return;

        requireController(event, event.getMessage(), auction -> {
            if (auction != null) {
                switch (id[1]) {
                    case "1" -> auction.bid(event, 1);
                    case "10" -> auction.bid(event, 10);
                    case "100" -> auction.bid(event, 100);
                    case "leave" -> auction.leave(event.getUser());

                    case "auction" -> add(getController(event.getGuild()), event.getGuild(), event.getMember().getUser(), ChannelType.AUCTION);
                }
            }
        });

        if (!event.isAcknowledged()) event.deferEdit().queue();
    }

    private MessageChannel getChannel(Controller controller, Guild guild, @NotNull ChannelType type) {
        return switch (type) {
            case AUCTION -> guild.getTextChannelById(controller.auctions.get(guild.getIdLong()));
            case MARKET -> null;
            case TRADE -> null;
        };
    }

    private void add(Controller controller, Guild guild, User user, ChannelType type) {
        getChannel(controller, guild, type).sendMessage("")
                .setComponents(MessageUtil.getButtons())
                .queue(message -> {
                    controller.channels.put(message.getIdLong(), getChannel(controller, guild, type));
                    createAuction(controller, message, new AuctionEntity(controller, message, "item", 100, 10, user));
                });
    }

    private void createAuction(@NotNull Controller controller, @NotNull Message message, @NotNull AuctionEntity item) {
        message.editMessageEmbeds(item.message()).queue();
        controller.entity.put(message.getIdLong(), item);
    }

    private Controller getController(@NotNull Guild guild) {
        return this.controllers.get(guild.getIdLong());
    }

    @NotNull
    private AuctionEntity getAuction(@NotNull Guild guild, @NotNull Message message) {
        return getController(guild).entity.get(message.getIdLong());
    }

    private void requireController(@NotNull IReplyCallback event, Message message, @NotNull Consumer<AuctionEntity> handler) {
        handler.accept(getAuction(event.getGuild(), message));
    }
}
