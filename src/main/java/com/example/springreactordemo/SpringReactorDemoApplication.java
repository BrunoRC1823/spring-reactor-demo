package com.example.springreactordemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.cert.X509Certificate;
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
            throw new ArithmeticException("Arithmetic Exception custom");
        }).onErrorReturn("Error!!").subscribe(log::info);
    }

    private void m13Threads() {
        final Mono<String> mono = Mono.just("Hi! ");
        Thread t1 = new Thread(() -> mono.map(msg -> msg + "thread: ")
                .subscribe(v -> log.info("{}{}", v, Thread.currentThread().getName())));
        log.info(Thread.currentThread().getName());
        t1.start();
    }

    private void m14PublishOn() {
        Flux.range(0, 2)
                .mapNotNull(x -> {
                    log.info("Valor: ".concat(String.valueOf(x + 1)).concat(" | Thread: ".concat(Thread.currentThread().getName())));
                    return x;
                })
                .publishOn(Schedulers.newSingle("mito-thread"))
                .mapNotNull(x -> {
                    log.info("Valor: ".concat(String.valueOf(x + 1)).concat(" | Thread: ".concat(Thread.currentThread().getName())));
                    return x;
                })
                .publishOn(Schedulers.boundedElastic())
                .mapNotNull(x -> {
                    log.info("Valor: ".concat(String.valueOf(x + 1)).concat(" | Thread: ".concat(Thread.currentThread().getName())));
                    return x;
                })
                .subscribe();
    }

    private void m15SubscribeOn() {
        Flux.range(0, 2)
                .mapNotNull(x -> {
                    log.info("Valor: ".concat(String.valueOf(x + 1)).concat(" | Thread: ".concat(Thread.currentThread().getName())));
                    return x;
                })
                .subscribeOn(Schedulers.immediate())
                .mapNotNull(x -> {
                    log.info("Valor: ".concat(String.valueOf(x + 1)).concat(" | Thread: ".concat(Thread.currentThread().getName())));
                    return x;
                })
                .subscribe();
    }

    private void m16PublishSubscribeOn() {
        Flux.range(0, 2)
                .publishOn(Schedulers.single())
                .mapNotNull(x -> {
                    log.info("Valor: ".concat(String.valueOf(x + 1)).concat(" | Thread: ".concat(Thread.currentThread().getName())));
                    return x;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .mapNotNull(x -> {
                    log.info("Valor: ".concat(String.valueOf(x + 1)).concat(" | Thread: ".concat(Thread.currentThread().getName())));
                    return x;
                })
                .subscribe();
    }

    private void m17runOn() {
        Flux.range(1, 16)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(x -> {
                    log.info("Valor: ".concat(String.valueOf(x)).concat(" | Thread: ".concat(Thread.currentThread().getName())));
                    return x;
                })
                .subscribe();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        dishes.addAll(List.of("Ceviche", "Papa rellena", "Sopa"));

        log.info("METHOD: 17");
        m17runOn();
    }
}
