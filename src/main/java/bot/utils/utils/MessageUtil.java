package bot.utils.utils;

import bot.utils.type.ChannelType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageUtil {

    @NotNull
    @Contract("_ -> new")
    public static EmbedBuilder intercepted(int value) {
        return new EmbedBuilder()
                .setDescription("Your deal has been hijacked with a big offer " + value);
    }

    @NotNull
    public static List<ActionRow> getAuctionButtons() {
        return List.of(
                ActionRow.of(
                        Button.primary("bid:1", "+1"),
                        Button.primary("bid:10", "+10"),
                        Button.primary("bid:100", "+100"),
                        Button.secondary("bid:leave", "leave")
                )
        );
    }

    @NotNull
    public static List<ActionRow> getInfoButtons() {
        return List.of(
                ActionRow.of(
                        Button.primary("bid:auction", "Auction"),
                        Button.primary("bid:market", "Market"),
                        Button.primary("bid:trade", "Trade"),
                        Button.primary("bid:remove", "Remove")
                )
        );
    }

    @NotNull
    public static List<ActionRow> getType(ChannelType type) {
        return switch (type) {
            case AUCTION -> getAuctionButtons();
            case MARKET -> getMarketButtons();
            case TRADE -> getTradeButtons();
        };
    }

    private static List<ActionRow> getTradeButtons() {
        return List.of(
                ActionRow.of(
                        Button.primary("bid:bay", "bay")
                )
        );
    }

    private static List<ActionRow> getMarketButtons() {
        return List.of(
                ActionRow.of(
                        Button.primary("bid:bay", "bay")
                )
        );
    }
}
