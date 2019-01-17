package com.bhochhi.spring.reactwithwebflux;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {



//    @GetMapping(value = "/products", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<Product> getAllProducts() {
//        return dataService.getBankProducts();
//    }

//    @GetMapping(value = "/xproducts", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<Product> xProducts() {
//        return client.get()
//                .uri("/coffees")
//                .retrieve()
//                .bodyToFlux(Product.class);
//    }


}