package bot.listener;

import bot.main.Application;
import bot.market.Controller;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StartupListener extends ListenerAdapter {
    public final Application app;

    public StartupListener(Application app) {
        this.app = app;
    }

    @Override
    public void onReady(ReadyEvent event) {
        event.getJDA().getGuilds().forEach(
                guild -> this.app.manager.controllers.put(guild.getIdLong(), new Controller())
        );
    }
}
