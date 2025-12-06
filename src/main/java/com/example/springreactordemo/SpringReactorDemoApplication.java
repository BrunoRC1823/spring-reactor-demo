package com.example.springreactordemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


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

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        dishes.addAll(List.of("Ceviche", "Papa rellena", "Sopa"));
        createMono();
        createFlux();
        m1doOnNext();
        m2map();
        m3flatMap();
        m4range();
    }
}
