package core;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class Reactor {
    public static void main(String[] args) throws InterruptedException {
        Flux.generate(synchronous -> synchronous.next("Ping!"))
                .delayElements(Duration.ofSeconds(5))
                .subscribe(System.out::println);

        new CountDownLatch(1).await();
    }
}
