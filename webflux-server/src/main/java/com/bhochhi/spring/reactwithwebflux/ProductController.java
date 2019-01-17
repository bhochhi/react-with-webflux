package com.bhochhi.spring.reactwithwebflux;

import org.reactivestreams.Publisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {


    private ProductRepository productRepository;
    private ApplicationEventPublisher publisher;

    ProductController(ProductRepository productRepository, ApplicationEventPublisher publisher) {
        this.productRepository = productRepository;
        this.publisher = publisher;
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


    @GetMapping(value = "/banking")
    Publisher<Product> getAllBanking() {
        return this.productRepository.findAllByType("bank");

    }


    @GetMapping("/insurance")
    Publisher<Product> getAllInsurance() {
        return this.productRepository.findAllByType("pnc");

    }

}
