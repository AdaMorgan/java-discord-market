package core.database;

import core.Timer;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public interface Query {
    static String getFile(String name) throws IOException {
        return Files.readString(Paths.get(name));
    }

    private static String getPattern(MessageReactionAddEvent event, String name) throws IOException {
        return getFile(name)
                .replaceAll(":id", "333")
                .replaceAll(":message_id", event.getMessageId())
                .replaceAll(":item", "sword")
                .replaceAll(":author", event.getUser().getName())
                .replaceAll(":author_id", event.getUserId())
                .replaceAll(":status", "NEW")
                .replaceAll(":initial", "0")
                .replaceAll(":start", String.valueOf(new Timer().getDateStart()))
                .replaceAll(":end", String.valueOf(new Timer().getDateStop(6)));
    }

    static void createTable(MessageReactionAddEvent event, String name) throws SQLException, IOException {
        Connect.getConnect().createStatement().executeQuery(getPattern(event, name)).close();
    }
}
