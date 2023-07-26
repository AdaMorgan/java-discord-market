package bot.main;

import bot.listener.ControlListener;
import bot.listener.StartupListener;
import bot.utils.Controller;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;


public class Application {
    public final JDA jda;
    public final Config config;
    public final Controller controller = new Controller(this);
    public final StartupListener startup;
    public final ControlListener control;

    public static void main(String[] args) {
        new Application(Config.readFromFile("config.toml"));
    }

    public Application(@NotNull Config config) {
        this.control = new ControlListener(this);
        this.startup = new StartupListener(this);
        this.config = config;

        jda = JDABuilder.createDefault(config.getToken())
                .setStatus(OnlineStatus.ONLINE)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(startup)
                .addEventListeners(control)
                .build();
    }
}
