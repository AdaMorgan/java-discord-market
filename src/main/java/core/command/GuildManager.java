package core.command;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GuildManager extends ListenerAdapter {
    private final long UNDEFINED = 0L;

    private final String CATEGORY = "TRADE";
    private final String AUCTIONS = "auctions";
    private final String ARCHIVE = "archive";

    @SuppressWarnings("FieldCanBeLocal")
    private final String NEW = "New Auction", END = "Ending Soon";
    private final Color RED = Color.decode("#ec084e"), BLUE = Color.decode("#1ce7dd");

    public String NEW_ID, END_ID;

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        if (getRoleID(event, NEW) == UNDEFINED)
            event.getGuild().createRole().setName(NEW).setColor(BLUE).queue(role -> NEW_ID = role.getId());

        if (getRoleID(event, END) == UNDEFINED)
            event.getGuild().createRole().setName(END).setColor(RED).queue(role -> END_ID = role.getId());

        if (getCategoryID(event, CATEGORY) == UNDEFINED)
            event.getGuild().createCategory(CATEGORY).complete();

        if (getChannelID(event, AUCTIONS) == UNDEFINED)
            event.getGuild().getCategoryById(getCategoryID(event, CATEGORY)).createTextChannel(AUCTIONS)
                    .queue();

        if (getArchiveID(event, ARCHIVE) == UNDEFINED)
            event.getGuild().getCategoryById(getCategoryID(event, CATEGORY)).createTextChannel(ARCHIVE).queue();
    }

    public long getCategoryID(GuildReadyEvent event, String name) {
        return event.getGuild().getCategories().stream()
                .filter(category -> category.getName().equals(name))
                .findFirst()
                .map(Category::getIdLong).orElse(UNDEFINED);
    }

    public long getArchiveID(GuildReadyEvent event, String name) {
        return event.getGuild().getChannels().stream()
                .filter(channel -> channel.getName().equals(name))
                .findFirst()
                .map(Channel::getIdLong).orElse(UNDEFINED);
    }

    public long getChannelID(GuildReadyEvent event, String name) {
        return event.getGuild().getChannels().stream()
                .filter(channel -> channel.getName().equals(name))
                .findFirst()
                .map(Channel::getIdLong).orElse(UNDEFINED);
    }

    public long getRoleID(GuildReadyEvent event, String name) {
        return event.getGuild().getRoles().stream()
                .filter(role -> role.getName().equals(name))
                .findFirst()
                .map(Role::getIdLong).orElse(UNDEFINED);
    }
}
