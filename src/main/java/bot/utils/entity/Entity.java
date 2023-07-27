package bot.utils.entity;

import bot.utils.Controller;
import bot.utils.timer.TimerTask;
import bot.utils.type.StatusType;
import bot.utils.utils.MessageUtil;
import bot.utils.utils.TimerUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Entity {
    public StatusType status;
    public Controller controller;
    public Message message;
    public String item;
    public int price, time;
    public User author;

    public final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Entity(Controller controller, Message message, String item, int price, int time, User author) {
        this.status = StatusType.BEGIN;
        this.controller = controller;
        this.message = message;
        this.item = item;
        this.price = price;
        this.time = time;
        this.author = author;

        scheduler.scheduleAtFixedRate(new TimerTask(this, time), 0, 1, TimeUnit.SECONDS);
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

    protected String setTitle() {
        return String.format("%s %s", this.status.getText(), TimerUtil.getTime(this.time));
    }

    public void stopAfter() {
        //...
    }

    protected void update() {
        //...
    }

    public MessageEmbed message() {
        return null;
    }

    protected MessageCreateData setFinalMessageByUser() {
        return null;
    }

}
