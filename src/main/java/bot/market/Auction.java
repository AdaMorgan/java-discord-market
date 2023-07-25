package bot.market;

import bot.utils.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Auction {
    public final String item;
    public final int start;
    public Message message;
    public int current;

    public final User user;
    public final Map<User, Integer> users = new ConcurrentHashMap<>();
    public User leader;

    public final Date date;

    public Auction(Message message, String item, int start, Date date, User author) {
        this.message = message;
        this.item = item;
        this.start = start;
        this.leader = null;
        this.current = 0;
        this.date = date;
        this.user = author;
    }

    public void update() {
        this.current = getLeader() != null ? this.users.get(getLeader()) : 0;
        this.leader = getLeader();
        this.message.editMessageEmbeds(message()).queue();
    }

    public void bid(IReplyCallback event, int value) {
        intercepted(event.getUser());
        this.users.put(event.getUser(), getMaxValue() + value + getStartValue());
        this.update();
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

    private String getTitle() {
        return getLeader() != null ? getLeader().getName() : "null";
    }

    public synchronized MessageEmbed message() {
        return new EmbedBuilder()
                .setTitle(getTitle())
                .addField("**Starting** @\n", String.valueOf(this.start), true)
                .addField("**Current Bid:**\n", String.valueOf(this.current), true)
                .setFooter("ID:" + this.message.getIdLong())
                .build();
    }
}
