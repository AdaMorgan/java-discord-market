package core.command;

import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


// TODO: автосоздание роли
public class GuildManager extends ListenerAdapter {

    public static long CATEGORY_ID, CHANNEL_ID = 0L;
    private final long UNDEFINED = 0L;

    public final String CATEGORY = "TRADE";
    public final String CHANNEL = "cap-auctions";

    //ROLE
    public final String NEW = "New Auction";
    public final String END = "Ending Soon";

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        if (getCategoryID(event) == UNDEFINED)
            event.getGuild().createCategory(CATEGORY).complete();

        if (getChannelID(event) == UNDEFINED)
            event.getGuild().getCategoryById(getCategoryID(event)).createTextChannel(CHANNEL).complete();
    }

    private long getCategoryID(GuildReadyEvent event) {
        for (Category category : event.getGuild().getCategories())
            if (category.getName().equals(CATEGORY))
                return CATEGORY_ID = Long.parseLong(category.getId());

        return UNDEFINED;
    }

    private long getChannelID(GuildReadyEvent event) {
        for (Channel channel : event.getGuild().getCategoryById(getCategoryID(event)).getChannels())
            if (channel.getName().equals(CHANNEL))
                return CHANNEL_ID = Long.parseLong(channel.getId());

        return UNDEFINED;
    }
}
