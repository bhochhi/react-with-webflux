package com.bhochhi.spring.reactwithwebflux;

import org.reactivestreams.Publisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.net.URI;

@RestController
@RequestMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@CrossOrigin(origins = "*")
public class TransactionController {


    private TransactionRepository   transactionRepository;
    private ApplicationEventPublisher publisher;

    TransactionController( TransactionRepository transactionRepository, ApplicationEventPublisher publisher) {
        this.transactionRepository = transactionRepository;
        this.publisher = publisher;
    }


    @PostMapping
    Publisher<ResponseEntity<Transaction>> create(@RequestBody Transaction transaction) {
        return this.transactionRepository
                .save(transaction)
                .doOnSuccess(trans -> {
                    System.out.println(trans);
                    this.publisher.publishEvent(trans);
                })
                .map(t -> ResponseEntity.created(URI.create("/transactions/" + t.getId()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .build());
    }


    @GetMapping
    Flux<Transaction> getAll() {
        return this.transactionRepository.findAll();

    }




}

