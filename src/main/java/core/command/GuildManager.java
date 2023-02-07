package core.command;

import jdk.jfr.MetadataDefinition;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.channel.GenericChannelEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.awt.*;


// TODO: автосоздание роли
public class GuildManager extends ListenerAdapter {
    private static long CATEGORY_ID, CHANNEL_ID = 0L;
    private final long UNDEFINED = 0L;

    private final String CATEGORY = "TRADE";
    private final String CHANNEL = "cap-auctions";

    //ROLE
    private final String NEW = "New Auction";
    private final String END = "Ending Soon";

    public String NEW_ID, END_ID;

    private final Color BLUE = new Color(123, 227, 221);
    private final Color RED = new Color(205, 18, 71);

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
                return CATEGORY_ID = Long.parseLong(category.getId());

        return UNDEFINED;
    }

    private long getChannelID(GuildReadyEvent event) {
        for (Channel channel : event.getGuild().getCategoryById(getCategoryID(event)).getChannels())
            if (!channel.getName().equals(CHANNEL))
                return CHANNEL_ID = Long.parseLong(channel.getId());

        return UNDEFINED;
    }
//
//    public long getRoleID(GuildReadyEvent event, String name) {
//        for (Role role : event.getGuild().getRoles())
//            if (role.getName().equals(name))
//                return Long.parseLong(role.getId());
//
//        return UNDEFINED;
//    }

    public static long getChannelId() {
        return CHANNEL_ID;
    }

    public static long getCategoryId() {
        return CATEGORY_ID;
    }
}
