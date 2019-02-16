package com.bhochhi.spring.reactwithwebflux;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.net.URI;
import java.time.Duration;
import java.time.LocalTime;

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
    }


    //How the media type plays the role...
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Publisher<Product> getAll() {
        Flux<Product> banks = webClient.get().uri("/products/banking")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToFlux(Product.class).delayElements(Duration.ofSeconds(2));

        Flux<Product> insurance = webClient.get().uri("/products/insurance")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToFlux(Product.class).delaySequence(Duration.ofMillis(500L));

        return Flux.merge(banks, insurance);

    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Publisher<Product> getAllStream() {

        //make webclient call to services...
        Flux<Product> banks = webClient.get().uri("/products/banking")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Product.class).delayElements(Duration.ofSeconds(2));

        Flux<Product> insurance = webClient.get().uri("/products/insurance")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Product.class).delaySequence(Duration.ofMillis(500L));

        return Flux.merge(banks, insurance);

    }

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Publisher<ServerSentEvent<Product>> getProductsAsSSE() {

        Flux<ServerSentEvent<Product>> banks = webClient.get().uri("/products/banking")
                .retrieve()
                .bodyToFlux(Product.class).delayElements(Duration.ofSeconds(2))
                .map(product -> ServerSentEvent.<Product>builder()
                        .id(String.valueOf(product.hashCode()))
                        .event("bankProduct")
                        .data(product)
                        .build());


        Publisher<ServerSentEvent<Product>> insurance = webClient.get().uri("/products/insurance")
                .retrieve()
                .bodyToFlux(Product.class).delaySequence(Duration.ofMillis(500L))
                .map(product -> ServerSentEvent.<Product>builder()
                        .id(String.valueOf(product.hashCode()))
                        .event("message")
                        .data(product)
                        .build());

        return Flux.concat(
                Flux.merge(banks, insurance),

                Mono.just(ServerSentEvent.<Product>builder()
                        .id("project-to-be-removed_id")
                        .event("removeProduct")
                        .data(new Product() {{
                            setId("uuid-need-to-be-removed");
                        }})
                        .build()),

                Mono.just(ServerSentEvent.<Product>builder()
                        .id("disconnected_id")
                        .event("disconnectEvent") //not working yet!
                        .data(new Product(){{
                            setId("uuid-need-tofff-be-removed");
                        }})
                        .build()));

    }


    @GetMapping(value = "/banking")
    Publisher<Product> getAllBanking() {
        return this.productRepository.findAllByType("bank");
    }


    @GetMapping("/insurance")
    Publisher<Product> getAllInsurance() {
        return this.productRepository.findAllByType("pnc");

    }

}
