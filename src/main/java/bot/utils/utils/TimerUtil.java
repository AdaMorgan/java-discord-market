package bot.utils.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimerUtil {
    public static String getTime(int second) {
        return LocalDateTime.now().plusSeconds(second)
                .atZone(ZoneId.of("America/New_York"))
                .format(DateTimeFormatter.ofPattern("MM/dd/yy h:mm")) + "PM ET";
    }

    public static String endTime(int second) {
        if (second > 3600) return "(in < a day)";
        if (second > 1800) return "(in < a 12 hours)";
        if (second > 21600) return "(in < a 6 hours)";
        if (second > 10800) return "(in < a 3 hours)";
        if (second > 3600) return "(in < a hour)";

        return "Other";
    }
}
