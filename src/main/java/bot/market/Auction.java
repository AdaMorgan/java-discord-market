package bot.market;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Auction {
    public final long id;
    public final String item;
    public final int start;
    public Message message;
    public int current;

    public final User user;
    public final Map<User, Integer> users = new ConcurrentHashMap<>();
    public User leader;

    public final Date date;

    public Auction(Message message, String item, int start, Date date, User user) {
        this.message = message;
        this.id = message.getIdLong();
        this.item = item;
        this.start = start;
        this.leader = null;
        this.current = 0;
        this.date = date;
        this.user = user;
    }

    public void update() {
        this.message.editMessageEmbeds(this.message.getEmbeds()).queue();
    }

    public void bid(User user, int value) {
        this.users.put(user, value);
        this.current += value;
        this.leader = user;
        this.update();
    }

    public void leave(User user) {
        this.users.remove(user);
        this.update();
    }

    private Map.Entry<User, Integer> last() {
        return users.entrySet().stream()
                .reduce((user, bid) -> bid)
                .orElseThrow();
    }
}
