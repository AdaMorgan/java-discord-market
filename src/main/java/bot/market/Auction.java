package bot.market;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

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

    public Auction(String item, int start, Date date, User user) {
        this.item = item;
        this.start = start;
        this.leader = getLeader();
        this.current = 0;
        this.date = date;
        this.user = user;
    }

    public void setId(Message message) {
        this.message = message;
    }

    public void update() {
        this.current = getLeader() != null ? this.users.get(getLeader()) : 0;
        this.message.editMessageEmbeds(message()).queue();
    }

    public void bid(User user, int value) {
        this.users.put(user, getMaxValue() + value + getStartValue());
        this.update();
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
                .build();
    }
}
