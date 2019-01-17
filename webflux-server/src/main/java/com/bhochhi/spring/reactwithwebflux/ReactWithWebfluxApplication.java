package com.bhochhi.spring.reactwithwebflux;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.UUID;

@SpringBootApplication
public class ReactWithWebfluxApplication {
    @Bean
    CommandLineRunner demoData(ProductRepository productRepository, TransactionRepository transactionRepository) {
        return args -> {
            productRepository.deleteAll().thenMany(
                    Flux.just("bankProduct1,bank", "bankProduct2,bank", "bankProduct3,bank", "insProduct1,pnc", "insProduct2,pnc", "insProduct3,pnc", "insProduct4,pnc")
                            .map(data -> {
                                String[] prod = data.split(",");
                                return new Product(UUID.randomUUID().toString(), prod[0], prod[1]);
                            })
                            .flatMap(productRepository::save))
                    .thenMany(productRepository.findAll())
                    .subscribe(System.out::println);


            transactionRepository.deleteAll().thenMany(
                    Flux.just("transaction 2,POSTED", "trans2,PENDING", "trans44555,PENDING", "transactions 400,PENDING", "despe2,POSTED", "trans,POSTED", "trand,CANCELLED")
                            .map(data -> {
                                String[] prod = data.split(",");
                                return new Transaction(UUID.randomUUID().toString(), prod[0], prod[1]);
                            })
                            .flatMap(transactionRepository::save))
                    .thenMany(transactionRepository.findAll())
                    .subscribe(System.out::println);

        };
    }


    @Bean
    WebClient client() {
        return WebClient.create("http://localhost:8080");
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactWithWebfluxApplication.class, args);
    }
}


