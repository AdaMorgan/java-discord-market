package bot.utils.timer;

import bot.utils.entity.AuctionEntity;

public class TimerTask implements Runnable {

    private final AuctionEntity auction;
    private final int start;
    private int current;

    public TimerTask(AuctionEntity auction, int time) {
        this.auction = auction;
        this.start = time * 60 * 60;
        this.current = time;
    }

    @Override
    public void run() {
        this.current--;
        recreate();
        stop();
    }

    public void stop() {
        if (this.current <= 0) this.auction.stop();
    }

    public void recreate() {
        if (this.current == 5) this.auction.recreate();
    }

    public String getCurrentTime() {
        return String.valueOf(this.current);
    }
}
