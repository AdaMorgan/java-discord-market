package bot.market;

import bot.main.Application;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Controller {
    public final Map<Long, Controller> controllers = new ConcurrentHashMap<>();
    public Map<Long, Auction> lots = new HashMap<>();

    public final Application app;

    public Controller(Application app) {
        this.app = app;
    }
}
