package bot.utils.utils;

import bot.utils.entity.AuctionEntity;
import bot.utils.entity.Entity;
import bot.utils.entity.MarketEntity;
import bot.utils.type.ChannelType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class EmbedUtil {

    @NotNull
    private static MessageEditData messageWinAuction() {
        return MessageEditData.fromContent("lot");
    }

    @NotNull
    private static MessageEmbed embedWinAuction(AuctionEntity entity, Date date) {
        return new EmbedBuilder()
                .setAuthor(entity.item)
                .setTitle(embedWinAuctionTitle(entity.users.get(entity.getLeader()), entity.bid))
                .setFooter(setFooter(entity.message.getIdLong(), date))
                .build();
    }

    private static String setFooter(long id, Date date) {
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
    private static MessageCreateData embedWinAuctionBuild(AuctionEntity entity, Date expired) {
        return MessageCreateData.fromEditData(embedWinAuctionData(entity, expired));
    }

    private static MessageCreateData embedBayMarketBuild(MarketEntity entity, Date expired) {
        return MessageCreateData.fromContent("");
    }

    public static MessageCreateData sendMessageEmbed(Entity entity, Date expired, @NotNull ChannelType channel) {
        return switch (channel) {
            case AUCTION -> embedWinAuctionBuild((AuctionEntity) entity, expired);
            case MARKET -> embedBayMarketBuild((MarketEntity) entity, expired);
        };
    }
}
