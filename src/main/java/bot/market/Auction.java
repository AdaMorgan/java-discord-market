package bot.market;

import bot.market.type.StatusType;
import bot.utils.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Auction {
    private final Controller controller;

    public final String item;
    public final int start, time;
    public Message message;
    public int current, bid;

    public final User author;
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    public final Map<User, Integer> users = new ConcurrentHashMap<>();
    public User leader;

    public Auction(Controller controller, Message message, String item, int start, int time, User author) {
        this.controller = controller;
        this.message = message;
        this.item = item;
        this.start = start;
        this.leader = null;
        this.current = 0;
        this.time = time;
        this.author = author;
        this.bid = 0;

        service.schedule(this::stop, this.time, TimeUnit.SECONDS);
    }

    public void stop() {
        this.message.delete().queue();
        this.controller.lots.remove(this.message.getIdLong());
        this.author.openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(state().build()).queue());
    }

    @NotNull
    private EmbedBuilder state() {
        if (this.leader != null)
            return new EmbedBuilder().setTitle("Winner: " + this.leader.getAsTag() + "/" + this.bid);
        else
            return new EmbedBuilder().setTitle("No winner");
    }

    public void update() {
        this.current = getLeader() != null ? this.users.get(getLeader()) : 0;
        this.leader = getLeader();
        this.message.editMessageEmbeds(message()).queue();
    }

    public void bid(@NotNull IReplyCallback event, int value) {
        intercepted(event.getUser());
        this.users.put(event.getUser(), getMaxValue() + value + getStartValue());
        this.update();
        this.bid++;
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
                .setAuthor(MessageUtil.getStatus(false).getText())
                .setTitle(getTitle())
                .setColor(MessageUtil.getStatus(false).getColor())
                .addField("**Starting** @\n", String.valueOf(this.start), true)
                .addField("**Current Bid:**\n", String.valueOf(this.current), true)
                .setFooter("ID:" + this.message.getIdLong())
                .build();
    }
}
