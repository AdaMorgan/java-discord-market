package bot.listener;

import bot.main.Application;
import bot.utils.Controller;
import bot.utils.entity.AuctionEntity;
import bot.utils.entity.Entity;
import bot.utils.entity.MarketEntity;
import bot.utils.entity.TradeEntity;
import bot.utils.type.ChannelType;
import bot.utils.utils.InputUtil;
import bot.utils.utils.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartupListener extends ListenerAdapter {
    public final Application app;
    private final ControlListener control;
    public Map<Long, Long> auctions, markets, trades;

    public StartupListener(Application app) {
        this.app = app;
        this.control = this.app.control;
        this.auctions = new HashMap<>();
        this.markets = new HashMap<>();
        this.trades = new HashMap<>();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().getGuilds().forEach(this::addController);
    }

    private void addController(@NotNull Guild guild) {
        this.app.controller.controllers.put(guild.getIdLong(), new Controller(this.app));

        createRole(guild);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String[] id = event.getComponentId().split(":");

        if (!id[0].equals("bid")) return;

        switch (id[1]) {
            case "auction" -> event.replyModal(InputUtil.auction()).queue();
            case "market" -> event.replyModal(InputUtil.market()).queue();
            case "trade" -> event.replyModal(InputUtil.market()).queue();
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        String[] id = event.getModalId().split(":");

        if (!id[0].equals("bot")) return;

        switch (id[1]) {
            case "auction" -> add(event, ChannelType.AUCTION);
            case "market" -> add(event, ChannelType.MARKET);
            case "trade" -> add(event, ChannelType.TRADE);
        }

        if (!event.isAcknowledged()) event.deferEdit().queue();
    }

    private void createRole(Guild guild) {
        createCategory(guild);
    }

    private void createCategory(@NotNull Guild guild) {
        guild.getCategories().stream()
                .filter(category -> category.getName().equalsIgnoreCase("trade"))
                .findFirst()
                .ifPresentOrElse(category -> getCategory(guild),
                        () -> guild.createCategory("trade").queue(category -> createChannel(guild)));

        guild.getChannels().forEach(channel -> {
            if (channel.getName().equalsIgnoreCase("auction"))
                this.app.startup.auctions.put(guild.getIdLong(), channel.getIdLong());

            if (channel.getName().equalsIgnoreCase("market"))
                this.app.startup.markets.put(guild.getIdLong(), channel.getIdLong());

            if (channel.getName().equalsIgnoreCase("trade"))
                this.app.startup.trades.put(guild.getIdLong(), channel.getIdLong());
        });
    }

    private void getCategory(@NotNull Guild guild) {
        guild.getCategoriesByName("trade", true).forEach(category -> createChannel(guild));
    }

    private final List<String> names = Arrays.asList("info", "auction", "market", "trade");

    private void sendMessage(@NotNull TextChannel channel) {
        if (channel.getName().equalsIgnoreCase("info"))
            channel.sendMessageEmbeds(info().build())
                    .setComponents(MessageUtil.getInfoButtons())
                    .queue();
    }

    private void createChannel(@NotNull Guild guild) {
        for (String name : names) {
            guild.getCategories().forEach(category -> {
                List<TextChannel> existingChannels = category.getTextChannels().stream()
                        .filter(channel -> channel.getName().equalsIgnoreCase(name))
                        .toList();

                if (existingChannels.isEmpty()) {
                    guild.createTextChannel(name)
                            .setParent(category)
                            .queue(this::sendMessage);
                }
            });
        }
    }

    private void add(@NotNull IReplyCallback event, ChannelType type) {
        Controller controller = control.getController(event.getGuild());

        if (controller.isItemLimit(event.getUser())) {
            getChannel(event.getGuild(), type).sendMessage("@New Item")
                    .setComponents(MessageUtil.getType(type))
                    .queue(message -> {
                        controller.channels.put(message.getIdLong(), getChannel(event.getGuild(), type));
                        create(controller, message, getType(event, controller, message, type));
                    });
        } else {
            event.reply("Limit!").setEphemeral(true).queue();
        }
    }

    private MessageChannel getChannel(Guild guild, @NotNull ChannelType type) {
        return switch (type) {
            case AUCTION -> guild.getTextChannelById(this.app.startup.auctions.get(guild.getIdLong()));
            case MARKET ->  guild.getTextChannelById(this.app.startup.markets.get(guild.getIdLong()));
            case TRADE ->  guild.getTextChannelById(this.app.startup.trades.get(guild.getIdLong()));
        };
    }

    private Entity getType(IReplyCallback event, Controller controller, Message message, ChannelType type) {
        return switch (type) {
            case AUCTION -> new AuctionEntity(controller, message, "item", 100, 10, event.getUser());
            case MARKET ->  new MarketEntity(controller, message, "item", 100, 10, event.getUser());
            case TRADE ->  new TradeEntity(controller, message, "item", 100, 10, event.getUser());
        };
    }

    private void create(@NotNull Controller controller, @NotNull Message message, @NotNull Entity entity) {
        message.editMessageEmbeds(entity.message()).queue();
        controller.entity.put(message.getIdLong(), entity);
    }

    private void setPermissionCategory(@NotNull Guild guild, @NotNull Category category, String name) {
        category.createTextChannel(name).addPermissionOverride(guild.getPublicRole(), 3, 3).queue();
    }

    @NotNull
    private EmbedBuilder info() {
        return new EmbedBuilder()
                .setTitle("INFO");
    }
}
