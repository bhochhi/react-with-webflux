package com.bhochhi.spring.reactwithwebflux;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
public class RootController {


    private DataService dataService;

    RootController(DataService dataService){

        this.dataService = dataService;
    }


    @GetMapping(value = "/products", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> getAllProducts() {
        return dataService.getBankProducts();
    }

//    @GetMapping(value = "/xproducts", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<Product> xProducts() {
//        return client.get()
//                .uri("/coffees")
//                .retrieve()
//                .bodyToFlux(Product.class);
//    }


}