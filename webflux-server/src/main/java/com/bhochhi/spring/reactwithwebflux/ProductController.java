package com.bhochhi.spring.reactwithwebflux;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

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
                .map(p -> ResponseEntity.created(URI.create("/products/" + p.getId()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .build());

        //TODO: add Event Sourcing to publish created product.
    }


    //How the media type plays the role...
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Publisher<Product> getAll() {
        Flux<Product> banks  = webClient.get().uri("/products/banking")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToFlux(Product.class).delayElements(Duration.ofSeconds(2));

        Flux<Product> insurance  = webClient.get().uri("/products/insurance")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToFlux(Product.class).delaySequence(Duration.ofMillis(500L));

        return Flux.merge(banks,insurance);

    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Publisher<Product> getAllStream() {

        //make webclient call to three services...
        Flux<Product> banks  = webClient.get().uri("/products/banking")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Product.class).delayElements(Duration.ofSeconds(2));

        Flux<Product> insurance  = webClient.get().uri("/products/insurance")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Product.class).delaySequence(Duration.ofMillis(500L));

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


    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> sseProducts() {
        return Flux
                .zip(Flux.interval(Duration.ofSeconds(1)), this.productRepository.findAll())
                .map(Tuple2::getT2);
    }


}
