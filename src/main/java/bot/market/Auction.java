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
    public int current;

    public final User user;
    public final Map<User, Integer> users = new ConcurrentHashMap<>();
    public User leader;

    public final Date date;

    public Auction(Message message, String item, int start, Date date, User user) {
        this.id = message.getIdLong();
        this.item = item;
        this.start = start;
        this.current = 0;
        this.leader = null;
        this.date = date;
        this.user = user;
    }

    public void create() {

    }

    private Map.Entry<User, Integer> last() {
        return users.entrySet().stream()
                .reduce((user, bid) -> bid)
                .orElseThrow();
    }
}
