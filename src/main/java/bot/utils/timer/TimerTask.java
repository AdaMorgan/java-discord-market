package bot.utils.timer;

import bot.utils.entity.Entity;
import bot.utils.type.StatusType;

public class TimerTask implements Runnable {

    private final Entity entity;
    private long current;

    public TimerTask(Entity auction, int minutes) {
        this.entity = auction;
        this.current = minutes;
    }

    @Override
    public void run() {
        this.current--;
        recreate();
        stop();
    }

    public void stop() {
        if (this.current <= 0) this.entity.stop();
    }

    public void recreate() {
        if (this.current == 5) this.entity.recreate(StatusType.ENDING);
    }

    public long getCurrentTime() {
        return this.current;
    }
}
