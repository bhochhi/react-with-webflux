package com.bhochhi.spring.reactwithwebflux;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

interface ProductRepository extends ReactiveCrudRepository<Product, String> {
        Flux<Product> findAllByType(String value);



}