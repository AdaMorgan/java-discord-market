package core;

import reactor.core.publisher.Flux;

import java.time.Duration;

class Reactor {
    public static void main(String[] args) throws InterruptedException {
        Flux.generate(synchronous -> synchronous.next("Ping!"))
                .delayElements(Duration.ofSeconds(5))
                .subscribe(System.out::println);

        Thread.sleep(Long.MAX_VALUE);
    }
}
