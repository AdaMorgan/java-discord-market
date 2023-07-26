package bot.utils;

import bot.main.Application;
import bot.utils.entity.AuctionEntity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Controller {
    public final Map<Long, MessageChannel> channels = new ConcurrentHashMap<>();
    public final Map<Long, Controller> controllers = new ConcurrentHashMap<>();
    public Map<Long, AuctionEntity> entity = new ConcurrentHashMap<>();

    public final Application app;

    public Controller(Application app) {
        this.app = app;
    }

    public long getCountItemByUser(User user) {
        return this.entity.values().stream()
                .filter(entity -> entity.author == user)
                .count();
    }

    public boolean isItemLimit(User user) {
        return this.app.config.getItemLimit() >= getCountItemByUser(user);
    }
}
