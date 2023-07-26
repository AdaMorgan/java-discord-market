package bot.listener;

import bot.main.Application;
import bot.utils.Controller;
import bot.utils.type.StatusType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        createRole(guild);
        setupGuild(guild);

        this.app.controller.controllers.put(guild.getIdLong(), new Controller(this.app));
    }

    public void createRole(Guild guild) {
        Arrays.stream(StatusType.values()).forEach(element ->
                guild.createRole().setName(element.getRole()).setColor(element.getColor()).queue()
        );
    }

    private void setupGuild(Guild guild) {
        Optional.of(guild.createCategory("market").complete()).stream()
                .peek(category -> category.createTextChannel("info")
                        .queue(channel -> channel.sendMessageEmbeds(info().build()).setComponents(buttons()).queue()))
                .peek(category -> category.createTextChannel("auction")
                        .queue(channel -> this.app.controller.auction.put(guild.getIdLong(), channel.getIdLong())))
//                .peek(category -> category.createTextChannel("market").queue())
//                .peek(category -> category.createTextChannel("trade").queue())
                .close();
    }

    private void setPermissionCategory(Guild guild, Category category, String name) {
        category.createTextChannel(name).addPermissionOverride(guild.getPublicRole(), 3, 3).queue();
    }

    private List<ActionRow> buttons() {
        return List.of(
                ActionRow.of(
                        Button.primary("bid:auction", "Auction"),
                        Button.primary("bid:market", "Market"),
                        Button.primary("bid:trade", "Trade")
                )
        );
    }

    private EmbedBuilder info() {
        return new EmbedBuilder()
                .setTitle("INFO");
    }
}
