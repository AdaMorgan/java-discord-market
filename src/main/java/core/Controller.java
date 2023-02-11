package core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


//Controller
public class Controller {
    public void getController() {
        AtomicReference<ScheduledFuture<?>> reference = new AtomicReference<>();
        AtomicInteger integer = new AtomicInteger();
        reference.set(Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(() -> {
            System.out.println(integer.incrementAndGet());
        }, 0, 1, TimeUnit.MINUTES));
    }
}
