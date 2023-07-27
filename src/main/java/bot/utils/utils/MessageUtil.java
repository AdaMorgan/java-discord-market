package bot.utils.utils;

import bot.utils.entity.AuctionEntity;
import bot.utils.type.ChannelType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
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

    public static MessageCreateData messageCreate(MessageEditData data) {
        return MessageCreateData.fromEditData(data);
    }

    @NotNull
    private static MessageEditData messageWinAuction() {
        return MessageEditData.fromContent("lot");
    }

    @NotNull
    private static MessageEmbed embedWinAuction(AuctionEntity entity, Date date) {
        return new EmbedBuilder()
                .setAuthor(entity.item)
                .setTitle(embedWinAuctionTitle(3, entity.bid))
                .setFooter(embedWinAuctionFooter(entity.message.getIdLong(), date))
                .build();
    }

    private static String embedWinAuctionFooter(long id, Date date) {
        return String.format("ID: %s | ● %s", id, 3);
    }

    private static String embedWinAuctionTitle(int price, int bid) {
        return String.format("Ended @%s w/%s bid(s)", price, bid);
    }

    @NotNull
    private static MessageEditData embedWinAuctionData(AuctionEntity entity, Date expired) {
        return MessageEditBuilder.from(messageWinAuction()).setEmbeds(embedWinAuction(entity, expired)).build();
    }

    @NotNull
    public static MessageCreateData embedWinAuctionBuild(AuctionEntity entity, Date expired) {
        return MessageCreateData.fromEditData(embedWinAuctionData(entity, expired));
    }

    @NotNull
    public static List<ActionRow> getType(@NotNull ChannelType type) {
        return switch (type) {
            case AUCTION -> getAuctionButtons();
            case MARKET -> getMarketButtons();
        };
    }

    private static List<ActionRow> getMarketButtons() {
        return List.of(
                ActionRow.of(
                        Button.primary("bid:bay", "bay")
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
}
