package bot.utils.entity;

import bot.utils.Controller;
import bot.utils.utils.MessageUtil;
import bot.utils.utils.TimerUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class AuctionEntity extends Entity {
    public int current, bid;

    public final Map<User, Integer> users = new ConcurrentHashMap<>();
    public User leader;

    public AuctionEntity(Controller controller, Message message, String item, int start, int time, User author) {
        super(controller, message, item, start, time, author);
        this.price = start;
        this.leader = null;
        this.current = 0;
        this.bid = 0;
    }

    @Override
    public void update() {
        this.current = getLeader() != null ? this.users.get(getLeader()) : 0;
        this.leader = getLeader();
        this.message.editMessageEmbeds(message()).queue(null, new ErrorHandler().ignore(ErrorResponse.UNKNOWN_MESSAGE));
    }

    @Override
    public MessageEmbed embed() {
        if (this.leader != null)
            return new EmbedBuilder().setTitle("Winner: " + this.leader.getAsTag() + "/" + this.bid).build();
        else
            return new EmbedBuilder().setTitle("No winner").build();
    }

    public void bid(@NotNull IReplyCallback event, int value) {
        if (!event.getUser().equals(this.author))
            this.bidAfter(event, value);
        else
            event.reply("You author!").setEphemeral(true).queue();
    }

    private void bidAfter(IReplyCallback event, int value) {
        intercepted(event.getUser());
        this.users.put(event.getUser(), getMaxPrice(value));
        this.update();
        this.bid++;
    }

    private void intercepted(User user) {
        if (!users.isEmpty() && this.leader != null && !this.leader.equals(user))
            this.leader.openPrivateChannel().queue(player -> player.sendMessageEmbeds(MessageUtil.intercepted(getMaxValueMap()).build()).queue());
    }

    private int getStartPrice() {
        return this.users.isEmpty() ? this.price : 0;
    }

    private int getMaxPrice(int value) {
        return (this.users.isEmpty() ? 0 : this.users.get(getLeader())) + value + getStartPrice();
    }

    private int getMaxValueMap() {
        return users.values().stream().max(Comparator.naturalOrder()).get();
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

    @Override
    public synchronized MessageEmbed message() {
        return new EmbedBuilder()
                .setAuthor(setTitle())
                .setTitle(getTitle())
                .setColor(this.status.getColor())
                .addField("**Starting** @\n", String.valueOf(this.price), true)
                .addField("**Current Bid:**\n", String.valueOf(this.current), true)
                .setFooter("ID: " + this.message.getIdLong())
                .build();
    }
}
