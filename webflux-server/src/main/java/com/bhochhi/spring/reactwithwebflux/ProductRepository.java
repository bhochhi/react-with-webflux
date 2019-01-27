package com.bhochhi.spring.reactwithwebflux;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
interface ProductRepository extends ReactiveCrudRepository<Product, String> {
        Flux<Product> findAllByType(String value);



}