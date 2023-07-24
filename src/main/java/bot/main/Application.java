package bot.main;

import bot.listener.ControlListener;
import bot.listener.StartupListener;
import bot.market.Manager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;


public class Application {
    public final JDA jda;
    public final Config config;
    public final Manager manager = new Manager(this);

    public static void main(String[] args) {
        new Application(Config.readFromFile("config.toml"));
    }

    public Application(@NotNull Config config) {
        this.config = config;

        jda = JDABuilder.createDefault(config.getToken())
                .setStatus(OnlineStatus.ONLINE)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new ControlListener(this))
                .addEventListeners(new StartupListener(this))
                .build();
    }
}
