package bot.listener;

import bot.main.Application;
import bot.market.Controller;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class StartupListener extends ListenerAdapter {
    public final Application app;

    public StartupListener(Application app) {
        this.app = app;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().getGuilds().forEach(this::addController);
    }

    private void addController(@NotNull Guild guild) {
        this.app.manager.controllers.put(guild.getIdLong(), new Controller());
    }
}
