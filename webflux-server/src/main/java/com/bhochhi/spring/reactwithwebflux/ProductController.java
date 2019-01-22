package com.bhochhi.spring.reactwithwebflux;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.time.Duration;

@Slf4j
@RestController
@RequestMapping(value = "/products")
@CrossOrigin(origins = "*")
public class ProductController {


    private ProductRepository productRepository;
    private ApplicationEventPublisher publisher;
    private WebClient webClient;

    ProductController(ProductRepository productRepository, ApplicationEventPublisher publisher, WebClient webClient) {
        this.productRepository = productRepository;
        this.publisher = publisher;
        this.webClient = webClient;
    }


    @PostMapping
    Publisher<ResponseEntity<Product>> create(@RequestBody Product product) {
        return this.productRepository
                .save(product)
                .doOnSuccess(prod -> this.publisher.publishEvent(prod))
                .map(p -> ResponseEntity.created(URI.create("/products/" + p.getId()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .build());
    }


    @GetMapping
    Publisher<Product> getAll() {
        return this.productRepository.findAll();

    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Publisher<Product> getAllStream() {

        //make webclient call to three services...
        Flux<Product> banks  = webClient.get().uri("/products/banking")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Product.class).delayElements(Duration.ofMillis(100L));

        Flux<Product> insurance  = webClient.get().uri("/products/insurance")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Product.class).delayElements(Duration.ofMillis(50L));


        return Flux.merge(banks,insurance);


    }


    @GetMapping(value = "/banking")
    Publisher<Product> getAllBanking() {
        return this.productRepository.findAllByType("bank");
    }


    @GetMapping(value = "/banking/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Product> getAllBankingStream() {
        log.info("publishing stream");
        return this.productRepository.findAllByType("bank");

    }


    @GetMapping("/insurance")
    Publisher<Product> getAllInsurance() {
        return this.productRepository.findAllByType("pnc");

    }

}
