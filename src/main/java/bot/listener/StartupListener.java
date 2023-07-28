package bot.listener;

import bot.main.Application;
import bot.utils.Controller;
import bot.utils.entity.AuctionEntity;
import bot.utils.entity.Entity;
import bot.utils.entity.MarketEntity;
import bot.utils.type.ChannelType;
import bot.utils.utils.InputUtil;
import bot.utils.utils.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class StartupListener extends ListenerAdapter {
    public final Application app;
    private final ControlListener control;
    public Map<Long, Long> auctions, markets, trades;
    private final List<String> names = Arrays.asList("info", "auction", "market", "trade");

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
            case "remove" -> createSelectMenu(event, event.getUser());
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        String[] id = event.getModalId().split(":");

        if (!id[0].equals("bot")) return;

        switch (id[1]) {
            case "auction" -> add(event, ChannelType.AUCTION);
            case "market" -> add(event, ChannelType.MARKET);
        }

        if (!event.isAcknowledged()) event.deferEdit().queue();
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        listItems(event.getGuild(), event.getUser())
                .forEach(entity -> {
                    removeItem(event.getGuild(), event.getUser(), event.getValues().get(0));
                    event.reply("item removed").setEphemeral(true).closeResources().queue();
                });
    }

    private void createRole(Guild guild) {
        createCategory(guild);
    }

    @NotNull
    private Optional<Category> findTradeCategory(@NotNull Guild guild) {
        return guild.getCategories().stream()
                .filter(category -> category.getName().equalsIgnoreCase("trade"))
                .findFirst();
    }

    private void createTradeCategory(@NotNull Guild guild) {
        guild.createCategory("trade").queue(category -> createChannel(guild));
    }

    private void updateStartupChannels(@NotNull Guild guild) {
        guild.getChannels().forEach(channel -> {
            switch (channel.getName().toLowerCase()) {
                case "auction" -> this.app.startup.auctions.put(guild.getIdLong(), channel.getIdLong());
                case "market" -> this.app.startup.markets.put(guild.getIdLong(), channel.getIdLong());
            }
        });
    }

    //
    private void removeItem(Guild guild, User user, String id) {
        System.out.println(id);
        listItems(guild, user).stream()
                .filter(entity -> entity.message.getId().equals(id))
                .forEach(Entity::stop);
    }

    private void createSelectMenu(@NotNull IReplyCallback event, User user) {
        if (!listItems(event.getGuild(), user).isEmpty())
            event.reply("Select item to delete!").setEphemeral(true).addActionRow(createMenu(event.getGuild(), event.getUser())).queue();
        else
            event.reply("You don't have auctions").setEphemeral(true).queue();
    }

    @NotNull
    private SelectMenu createMenu(Guild guild, User user) {
        return StringSelectMenu.create("bot:items")
                .addOptions(optionsItems(guild, user))
                .build();
    }

    private List<Entity> listItems(Guild guild, User user) {
        return this.app.control.getController(guild).entity.values().stream()
                .filter(entity -> entity.author.equals(user))
                .collect(Collectors.toList());
    }

    @NotNull
    private SelectOption[] optionsItems(Guild guild, User user) {
        return listItems(guild, user).stream()
                .map(entity -> SelectOption.of(entity.item, entity.message.getId()))
                .toArray(SelectOption[]::new);
    }

    private void createCategory(@NotNull Guild guild) {
        findTradeCategory(guild).ifPresentOrElse(
                category -> getCategory(guild),
                () -> createTradeCategory(guild)
        );
        updateStartupChannels(guild);
    }

    private void getCategory(@NotNull Guild guild) {
        guild.getCategoriesByName("trade", true).forEach(category -> createChannel(guild));
    }

    private void sendMessage(@NotNull TextChannel channel) {
        if (channel.getName().equalsIgnoreCase("info"))
            channel.sendMessageEmbeds(info().build())
                    .setComponents(MessageUtil.getInfoButtons())
                    .queue();
    }

    private void createChannel(@NotNull Guild guild) {
        names.forEach(name -> guild.getCategories().forEach(category -> {
            if (getExistingChannels(category.getTextChannels(), name).isEmpty()) {
                createTextChannel(guild, name, category);
            }
        }));
    }

    private List<TextChannel> getExistingChannels(@NotNull List<TextChannel> channels, String name) {
        return channels.stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(name))
                .toList();
    }

    private void createTextChannel(@NotNull Guild guild, String name, Category category) {
        guild.createTextChannel(name)
                .setParent(category)
                .queue(this::sendMessage);
    }

    private void add(@NotNull IReplyCallback event, ChannelType type) {
        if (control.getController(event.getGuild()).isItemLimit(event.getUser()))
            createItem(event, control.getController(event.getGuild()), type);
        else
            event.reply("Limit!").setEphemeral(true).queue();
    }

    private void createItem(@NotNull IReplyCallback event, Controller controller, ChannelType type) {
        getChannel(event.getGuild(), type).sendMessage("@New Item")
                .setComponents(MessageUtil.getType(type))
                .queue(message -> {
                    controller.channels.put(message.getIdLong(), getChannel(event.getGuild(), type));
                    create(controller, message, getType(event, controller, message, type));
                });
    }

    private MessageChannel getChannel(Guild guild, @NotNull ChannelType type) {
        return switch (type) {
            case AUCTION -> guild.getTextChannelById(this.app.startup.auctions.get(guild.getIdLong()));
            case MARKET ->  guild.getTextChannelById(this.app.startup.markets.get(guild.getIdLong()));
        };
    }

    private Entity getType(IReplyCallback event, Controller controller, Message message, @NotNull ChannelType type) {
        return switch (type) {
            case AUCTION -> new AuctionEntity(controller, message, "item", 100, 30, event.getUser());
            case MARKET ->  new MarketEntity(controller, message, "item", 100, 30, event.getUser());
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
