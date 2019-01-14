package com.bhochhi.spring.reactwithwebflux;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

interface ProductRepository extends ReactiveCrudRepository<Product, String> {

}