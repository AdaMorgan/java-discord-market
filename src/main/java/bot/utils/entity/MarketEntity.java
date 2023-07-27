package bot.utils.entity;

import bot.utils.Controller;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.NotNull;

public class MarketEntity extends Entity {
    public MarketEntity(Controller controller, Message message, String item, int price, int time, User author) {
        super(controller, message, item, price, time, author);
    }

    public void bay(User user) {
        stop();
        stopAfter(user);
    }

    @Override
    protected MessageCreateData setFinalMessageByUser() {
        return MessageCreateData.fromContent("");
    }

    public void stopAfter(User user) {
        user.openPrivateChannel().queue(channel -> {
            channel.sendMessage("You have successfully requested a trade from " + user.getName()).queue();
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
