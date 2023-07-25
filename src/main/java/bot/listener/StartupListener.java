package bot.listener;

import bot.main.Application;
import bot.utils.Controller;
import bot.utils.type.StatusType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

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
        this.app.controller.controllers.put(guild.getIdLong(), new Controller(this.app));
    }

    public void createRole(Guild guild) {
        Arrays.stream(StatusType.values()).forEach(value ->
                guild.createRole().setColor(value.getColor()).setName(value.getRole()).queue()
        );
    }
}
