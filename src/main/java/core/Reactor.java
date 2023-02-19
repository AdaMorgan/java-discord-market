package core;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class Reactor {
    public static void main(String[] args) {
        Flux.generate(synchronous -> synchronous.next("Work!"))
                .delayElements(Duration.ofSeconds(5))
                .subscribe(System.out::println);
    }
}
