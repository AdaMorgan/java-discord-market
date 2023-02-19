package core.command;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildManager extends ListenerAdapter {
    private final long UNDEFINED = 0L;

    private final String CATEGORY = "TRADE";
    private final String CHANNEL = "cap-auctions";

    //ROLE
    private final String NEW = "New Auction";
    private final String END = "Ending Soon";

    public String NEW_ID, END_ID;

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
//        if (getRoleID(event, NEW) == UNDEFINED)
//            event.getGuild().createRole().setName(NEW).setColor(BLUE).queue(role -> NEW_ID = role.getId());
//
//        if (getRoleID(event, END) == UNDEFINED)
//            event.getGuild().createRole().setName(END).setColor(RED).queue(role -> END_ID = role.getId());

        if (getCategoryID(event) == UNDEFINED)
            event.getGuild().createCategory(CATEGORY).complete();

        if (getChannelID(event) == UNDEFINED)
            event.getGuild().getCategoryById(getCategoryID(event)).createTextChannel(CHANNEL).complete();
    }

    private long getCategoryID(GuildReadyEvent event) {
        for (Category category : event.getGuild().getCategories())
            if (category.getName().equals(CATEGORY))
                return category.getIdLong();

        return UNDEFINED;
    }

    private long getChannelID(GuildReadyEvent event) {
        for (Channel channel : event.getGuild().getChannels())
            if (!channel.getName().equals(CHANNEL))
                return channel.getIdLong();

        return UNDEFINED;
    }

    public long getRoleID(GuildReadyEvent event, String name) {
        for (Role role : event.getGuild().getRoles())
            if (role.getName().equals(name))
                return role.getIdLong();

        return UNDEFINED;
    }
}
