package bot.utils;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Timer {
    private static final Timer TIMER = new Timer();

    private String getText(int count) {
        return count < 1 ? "Expires:" : "ENDING SOON!";
    }

    public Date getDateStart() {
        return new Date();
    }

    public Date getDateStop(int count) {
        return Date.from(getDateStart().toInstant().plus(count, ChronoUnit.HOURS));
    }

    public static String getTimerText(int count) {
        return String.format("%s %s %s", TIMER.getText(count), TIMER.getPattern(count), TIMER.getTimeToHours(count));
    }

    private String getPattern(int count) {
        return new SimpleDateFormat("MM/dd/yyyy H:mm 'PM ET'").format(getDateStop(count));
    }

    private long getPeriod(int count) {
        return getDateStop(count).getTime() - getDateStart().getTime();
    }

    private String getTimeToDay(int count) {
        return String.format("(in < %s days)", TimeUnit.MILLISECONDS.toDays(getPeriod(count)));
    }

    private String getTimeToHours(int count) {
        return String.format("(in < %s hours)", TimeUnit.MILLISECONDS.toHours(getPeriod(count)));
    }

    private String getTimeToMinutes(int count) {
        return String.format("(in < %s minutes)", TimeUnit.MILLISECONDS.toMinutes(getPeriod(count)));
    }
}
