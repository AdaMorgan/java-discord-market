package bot.utils.entity;

import bot.utils.Controller;
import bot.utils.type.StatusType;
import bot.utils.timer.TimerTask;
import bot.utils.utils.MessageUtil;
import bot.utils.utils.TimerUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuctionEntity extends Entity {
    private final Controller controller;

    public final String item;
    public final int start, time;
    private StatusType status;
    public Message message;
    public int current, bid;

    public final User author;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public final Map<User, Integer> users = new ConcurrentHashMap<>();
    public User leader;

    public AuctionEntity(Controller controller, Message message, String item, int start, int time, User author) {
        this.status = StatusType.BEGIN;
        this.controller = controller;
        this.message = message;
        this.item = item;
        this.start = start;
        this.leader = null;
        this.current = 0;
        this.time = time;
        this.author = author;
        this.bid = 0;

        scheduler.scheduleAtFixedRate(new TimerTask(this, time), 0, 1, TimeUnit.SECONDS);
    }

    private MessageChannel getChannel() {
        return this.controller.channels.get(this.message.getIdLong());
    }

    public void recreate() {
        removeMessage();
        this.status = StatusType.ENDING;
        getChannel().sendMessage("")
                .setComponents(MessageUtil.getAuctionButtons())
                .queue(newMessage -> {
                    controller.entity.remove(this.message.getIdLong());
                    this.message = newMessage;
                    this.update();
                    controller.entity.put(newMessage.getIdLong(), this);
                });
    }

    public void update() {
        this.current = getLeader() != null ? this.users.get(getLeader()) : 0;
        this.leader = getLeader();
        this.message.editMessageEmbeds(message()).queue(null, new ErrorHandler().ignore(ErrorResponse.UNKNOWN_MESSAGE));
    }

    public void removeMessage() {
        this.message.delete().queue();
    }

    public void stop() {
        removeMessage();
        this.controller.entity.remove(this.message.getIdLong());
        this.author.openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(state().build()).queue());
        this.scheduler.shutdown();
    }

    @NotNull
    private EmbedBuilder state() {
        if (this.leader != null)
            return new EmbedBuilder().setTitle("Winner: " + this.leader.getAsTag() + "/" + this.bid);
        else
            return new EmbedBuilder().setTitle("No winner");
    }

    public void bid(@NotNull IReplyCallback event, int value) {
        if (event.getUser().equals(this.author)) {
            intercepted(event.getUser());
            this.users.put(event.getUser(), getMaxValue() + value + getStartValue());
            this.update();
            this.bid++;
        } else {
            event.reply("You author!").setEphemeral(true).queue();
        }
    }

    private void intercepted(User user) {
        if (!users.isEmpty() && this.leader != null && !this.leader.equals(user))
            this.leader.openPrivateChannel().queue(player -> player.sendMessageEmbeds(MessageUtil.intercepted(1000).build()).queue());
    }

    private int getStartValue() {
        return this.users.isEmpty() ? this.start : 0;
    }

    private int getMaxValue() {
        return this.users.isEmpty() ? 0 : this.users.get(getLeader());
    }

    private User getLeader() {
        return users.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public void leave(User user) {
        this.users.remove(user);
        this.update();
    }

    @NotNull
    private String getTitle() {
        return getLeader() != null ? getLeader().getName() : "null";
    }

    public synchronized MessageEmbed message() {
        return new EmbedBuilder()
                .setAuthor(setTitle())
                .setTitle(getTitle())
                .setColor(this.status.getColor())
                .addField("**Starting** @\n", String.valueOf(this.start), true)
                .addField("**Current Bid:**\n", String.valueOf(this.current), true)
                .setFooter("ID: " + this.message.getIdLong())
                .build();
    }

    private String setTitle() {
        return String.format("%s %s", this.status.getText(), TimerUtil.getTime(this.time));
    }
}
