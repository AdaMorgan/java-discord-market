package bot.listener;

import bot.main.Application;
import bot.utils.Controller;
import bot.utils.entity.AuctionEntity;
import bot.utils.entity.Entity;
import bot.utils.entity.MarketEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
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

        requireController(event, event.getMessage(), entity -> {
            if (entity != null) {
                if (entity instanceof AuctionEntity auctionEntity) {
                    switch (id[1]) {
                        case "1" -> auctionEntity.bid(event, 1);
                        case "10" -> auctionEntity.bid(event, 10);
                        case "100" -> auctionEntity.bid(event, 100);
                        case "leave" -> auctionEntity.leave(event.getUser());
                    }
                }
                if (entity instanceof MarketEntity marketEntity) {
                    switch (id[1]) {
                        case "bay" -> marketEntity.bay();
                    }
                }
            }
        });
    }

    public Controller getController(@NotNull Guild guild) {
        return this.controllers.get(guild.getIdLong());
    }

    public void requireController(@NotNull IReplyCallback event, @NotNull Message message, @NotNull Consumer<Entity> handler) {
        handler.accept(getController(event.getGuild()).entity.get(message.getIdLong()));
    }
}
