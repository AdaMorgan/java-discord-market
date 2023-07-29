package bot.utils.utils;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimerUtil {

    @NotNull
    public static String getExpiredTime(int second) {
        return LocalDateTime.now().plusSeconds(second)
                .atZone(ZoneId.of("America/New_York"))
                .format(DateTimeFormatter.ofPattern("MM/dd/yy h:mm")) + " PM ET";
    }

    @NotNull
    public static String getElementTime(long second) {
        if (second > TimeUnit.DAYS.toSeconds(2))
            return "(in < " + TimeUnit.SECONDS.toDays(second) + " day)";
        if (second > TimeUnit.DAYS.toSeconds(1) && second <= TimeUnit.DAYS.toSeconds(2))
            return "(in < a day)";
        if (second > TimeUnit.HOURS.toSeconds(2) && second <= TimeUnit.DAYS.toSeconds(1))
            return "(in < " + TimeUnit.SECONDS.toHours(second) + " hours)";
        if (second > TimeUnit.HOURS.toSeconds(1) && second <= TimeUnit.HOURS.toSeconds(2))
            return "(in < a hour)";
        if (second > TimeUnit.MINUTES.toSeconds(5) && second <= TimeUnit.HOURS.toSeconds(1))
            return "(in < " + TimeUnit.SECONDS.toMinutes(second) + " minutes)";
        if (second <= TimeUnit.MINUTES.toSeconds(5))
            return "(in < 5 minutes)";

        return "null";
    }

    @NotNull
    public static String getElementTime(Date date) {
        return String.format("", 3);
    }
}
