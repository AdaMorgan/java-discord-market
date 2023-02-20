package core;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class Reactor {
    public static final double POSITIVE_INFINITY = 1.0 / 0.0;

    public static void main(String[] args) throws InterruptedException {
        Flux.generate(synchronous -> synchronous.next("Ping!"))
                .delayElements(Duration.ofSeconds(5))
                .subscribe(System.out::println);

        Thread.sleep((long) POSITIVE_INFINITY);
    }
}
