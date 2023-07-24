package bot.market;

import bot.main.Application;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Manager {
    public final Map<Long, Controller> controllers = new ConcurrentHashMap<>();
    public final Application app;

    public Manager(Application app) {
        this.app = app;
    }
}
