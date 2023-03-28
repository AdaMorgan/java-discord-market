package core.database;

import java.sql.Date;
import java.sql.SQLException;

public record Table(
        long id,
        long messageID,
        long itemID, String item,
        long authorID, String authorName,
        String status,
        int initial, int current, int final1,
        Date start, Date end) {

}
