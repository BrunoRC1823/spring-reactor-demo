package com.example.springreactordemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


@SpringBootApplication
public class SpringReactorDemoApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(SpringReactorDemoApplication.class);
    private List<String> dishes = new ArrayList<>();

    private void createMono() {
        Mono.just("Holi").subscribe(log::info);
    }

    private void createFlux() {
        Flux.fromIterable(dishes).subscribe(log::info);
    }

    private void m1doOnNext() {
        Flux<String> fx = Flux.fromIterable(dishes);
        fx.doOnNext(x -> log.info("Element: {}", x))
                .subscribe();
    }

    private void m2map() {
        Flux<String> fx1 = Flux.fromIterable(dishes);
        Flux<String> fx2 = fx1.map(String::toUpperCase);
        fx2.subscribe(log::info);
    }

    private void m3flatMap() {
        Mono.just("bruno").map(x -> 34).subscribe(e -> log.info("Data {}", e));
        Mono.just("bruno").map(x -> Mono.just(34)).subscribe(e -> log.info("Data {}", e));
        Mono.just("bruno").flatMap(x -> Mono.just(34)).subscribe(e -> log.info("Data {}", e));
    }

    private void m4range() {
        Flux<Integer> fx = Flux.range(0, 10);
        fx.map(x -> x * x)
                .subscribe(a -> log.info("Number: {}", a));
    }

    private void m5delayElements() throws InterruptedException {
        Flux<Integer> fx = Flux.range(0, 5);
        fx.delayElements(Duration.ofMillis(500)).doOnNext(a -> log.info("Number: {}", a)).subscribe();
        Thread.sleep(2700);
    }

    private void m6zipWith() throws InterruptedException {
        List<String> client = List.of("Client 1", "Client 2", "Client 3");
        Flux<String> fx1 = Flux.fromIterable(client);
        Flux<String> fx2 = Flux.fromIterable(dishes);
        fx1.zipWith(fx2, (a, b) -> a.concat(" - ").concat(b)).subscribe(log::info);
    }

    private void m7merge() {
        List<String> client = List.of("Client 1", "Client 2");
        Flux<String> fx1 = Flux.fromIterable(client);
        Flux<String> fx2 = Flux.fromIterable(dishes);
        Flux.merge(fx1, fx2).subscribe(log::info);
    }

    private void m8filter() {
        Predicate<String> filter = a -> a.startsWith("C");
        Flux.fromIterable(dishes).filter(filter)
                .subscribe(log::info);
    }

    private void m9Take() {
        Flux<String> fl1 = Flux.fromIterable(dishes);
        fl1.take(2).subscribe(log::info);
    }

    private void m10TakeLast() {
        Flux<String> fl1 = Flux.fromIterable(dishes);
        fl1.takeLast(2).subscribe(log::info);
    }

    private void m11DefaultIfEmpty() {
        Flux<String> fl1 = Flux.fromIterable(new ArrayList<>());
        fl1.map(String::toUpperCase).filter(a -> a.startsWith("C")).defaultIfEmpty("EMPTY FLUX").subscribe(log::info);
    }
    private void m12error() {
        Flux<String> fl1 = Flux.fromIterable(dishes);
        fl1.doOnNext(e -> {
            throw  new ArithmeticException("Arithmetic Exception custom");
        }).onErrorReturn("Error!!").subscribe(log::info);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        dishes.addAll(List.of("Ceviche", "Papa rellena", "Sopa"));
        log.info("CREATE MONO");
        createMono();
        log.info("CREATE FLUX");
        createFlux();
        log.info("METHOD: 1");
        m1doOnNext();
        log.info("METHOD: 2");
        m2map();
        log.info("METHOD: 3");
        m3flatMap();
        log.info("METHOD: 4");
        m4range();
        log.info("METHOD: 5");
        m5delayElements();
        log.info("METHOD: 6");
        m6zipWith();
        log.info("METHOD: 7");
        m7merge();
        log.info("METHOD: 8");
        m8filter();
        log.info("METHOD: 9");
        m9Take();
        log.info("METHOD: 10");
        m10TakeLast();
        log.info("METHOD: 11");
        m11DefaultIfEmpty();
        log.info("METHOD: 12");
        m12error();
    }
}
