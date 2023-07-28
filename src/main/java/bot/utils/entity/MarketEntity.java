package bot.utils.entity;

import bot.utils.Controller;
import bot.utils.type.ChannelType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public class MarketEntity extends Entity {
    private User buyer;

    public MarketEntity(Controller controller, Message message, String item, int price, int time, User author) {
        super(controller, message, item, price, time, author);
        this.buyer = null;
    }

    public void bay(User user) {
        stopAfter(user);
        stop();
    }

    @Override
    protected MessageCreateData setFinalMessageByUser() {
        if (this.buyer != null)
            return MessageCreateData.fromContent("You have been requested to exchange " + this.buyer.getName());
        else
            return MessageCreateData.fromContent("Nobody bought your item");
    }

    @Override
    protected ChannelType setType() {
        return ChannelType.MARKET;
    }

    @Override
    protected void update() {
        this.message.editMessageEmbeds(message()).queue(null, new ErrorHandler().ignore(ErrorResponse.UNKNOWN_MESSAGE));
    }

    private void stopAfter(@NotNull User user) {
        this.buyer = user;
        user.openPrivateChannel().queue(channel -> {
            channel.sendMessage("You have successfully requested a trade from " + this.author.getName()).queue();
        });
    }

    @Override
    public MessageEmbed message() {
        return new EmbedBuilder()
                .setAuthor(setTitle())
                .setTitle(this.item)
                .setColor(this.status.getColor())
                .addField("**Price** @\n", String.valueOf(this.price), true)
                .setFooter("ID: " + this.message.getIdLong())
                .build();
    }
}
