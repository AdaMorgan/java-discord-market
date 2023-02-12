package core;

import reactor.core.publisher.Flux;

import java.time.Duration;

//Controller
public class Reactor {
    public static void main(String[] args) throws InterruptedException {
        Flux.generate(synchronousSink -> {

        }).subscribe(System.out::println);

        Thread.sleep(40000);
    }
}
