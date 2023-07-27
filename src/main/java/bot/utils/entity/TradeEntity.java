package bot.utils.entity;

import bot.utils.Controller;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class TradeEntity extends Entity {
    public TradeEntity(Controller controller, Message message, String item, int price, int time, User author) {
        super(controller, message, item, price, time, author);
    }
}
