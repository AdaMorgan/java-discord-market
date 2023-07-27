package bot.utils.entity;

import bot.utils.Controller;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class MarketEntity extends Entity {
    public MarketEntity(Controller controller, Message message, String item, int price, int time, User author) {
        super(controller, message, item, price, time, author);
    }

    public void bay() {
        stop();
    }

    @Override
    protected MessageEmbed embed() {
        return new EmbedBuilder().setTitle("You want to buy a lot").build();
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
