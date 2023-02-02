package core;

import core.command.ChannelManager;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        JDABuilder.create("OTY4MDEwMTk0Nzc4OTkyNjcw.GuC79j.OTRo-BXU81gjAAcwdkUkq1LNXG6SXDdsoJCI_w", Arrays.asList(INTENTS))
                .setActivity(Activity.playing("server"))
                .setStatus(OnlineStatus.ONLINE)
                .setMemberCachePolicy(MemberCachePolicy.NONE)

                .addEventListeners(new ChannelManager())
                .addEventListeners(new CommandManager())
                .build();
    }

    private static final GatewayIntent[] INTENTS = {
            GatewayIntent.MESSAGE_CONTENT,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_MESSAGE_REACTIONS,
            GatewayIntent.GUILD_PRESENCES,
    };
}