package com.bhochhi.spring.reactwithwebflux;


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
class DataService{

    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;
    private final WebClient client;


    DataService(ProductRepository productRepository, TransactionRepository transactionRepository, WebClient client){
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
        this.client = client;
    }


    Flux<Product> getBankProducts(){
        return productRepository.findAll().filter(m->m.getType().equalsIgnoreCase("bank"));
    }

    List<Product> getInsuranceProducts(){
        return null;
    }

    List<Transaction> getRecentTransactions(){

        return null;
    }
}