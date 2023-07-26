package bot.utils;

import bot.main.Application;
import bot.utils.entity.AuctionEntity;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Controller {
    public final Map<Long, MessageChannel> channels = new ConcurrentHashMap<>();
    public final Map<Long, Controller> controllers = new ConcurrentHashMap<>();
    public Map<Long, AuctionEntity> entity = new ConcurrentHashMap<>();
    public Map<Long, Long> auctions, markets, trades = new ConcurrentHashMap<>();

    public final Application app;

    public Controller(Application app) {
        this.app = app;
    }
}
