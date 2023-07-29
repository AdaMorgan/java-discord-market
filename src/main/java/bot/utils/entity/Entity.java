package bot.utils.entity;

import bot.utils.Controller;
import bot.utils.timer.TimerTask;
import bot.utils.type.ChannelType;
import bot.utils.type.StatusType;
import bot.utils.utils.MessageUtil;
import bot.utils.utils.TimerUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Entity {
    private final ChannelType type;
    private final TimerTask timer;
    public StatusType status;
    public Controller controller;
    public Message message;
    public String item;
    public int price, time;
    public User author;

    public final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Entity(Controller controller, Message message, String item, int price, int time, User author) {
        this.status = StatusType.BEGIN;
        this.type = setType();
        this.controller = controller;
        this.message = message;
        this.item = item;
        this.price = price;
        this.time = time;
        this.author = author;
        this.timer = new TimerTask(this, time);

        scheduler.scheduleAtFixedRate(this.timer, 0, 1, TimeUnit.SECONDS);
    }

    private MessageChannel getChannel() {
        return this.controller.channels.get(this.message.getIdLong());
    }

    public void removeMessage() {
        this.message.delete().queue();
    }

    public void stop() {
        removeMessage();
        this.controller.entity.remove(this.message.getIdLong());
        this.author.openPrivateChannel().queue(channel -> channel.sendMessage(setFinalMessageByUser()).queue());
        stopAfter();
        this.scheduler.shutdown();
    }

    public void recreate(StatusType type) {
        removeMessage();
        this.status = type;
        sendMessage();
    }

    private void sendMessage() {
        getChannel().sendMessage(getStatusMessage()).setComponents(getType()).queue(this::updateMessage);
    }

    private String getStatusMessage() {
        return this.status == StatusType.BEGIN ? "" : StatusType.ENDING.getRole();
    }

    private void updateMessage(@NotNull Message newMessage) {
        controller.entity.remove(this.message.getIdLong());
        this.message = newMessage;
        this.update();
        controller.entity.put(newMessage.getIdLong(), this);
    }

    private List<ActionRow> getType() {
        if (this.type == ChannelType.AUCTION)
            return MessageUtil.getAuctionButtons();
        else
            return MessageUtil.getMarketButtons();
    }

    protected ChannelType setType() {
        return type;
    }

    protected String setTitle() {
        return String.format("%s %s %s", this.status.getText(), TimerUtil.getExpiredTime(this.time), TimerUtil.getElementTime(this.timer.getCurrentTime()));
    }

    public abstract void stopAfter();

    public abstract void update();

    public abstract MessageEmbed message();

    protected abstract MessageCreateData setFinalMessageByUser();
}
