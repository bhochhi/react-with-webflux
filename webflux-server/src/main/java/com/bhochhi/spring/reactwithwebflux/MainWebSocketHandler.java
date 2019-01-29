package com.bhochhi.spring.reactwithwebflux;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class MainWebSocketHandler implements WebSocketHandler {

    private ProductRepository productRepository;
    private ObjectMapper objectMapper;


    public MainWebSocketHandler(ObjectMapper objectMapper, ProductRepository productRepository) {
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
    }


    @Override
    public Mono<Void> handle(WebSocketSession session) {

        log.info("client messages:  ");
        Flux<String>  inputs = session.receive().map(WebSocketMessage::getPayloadAsText);

        System.out.println("reading client messages "+inputs.count().then());
        inputs.doOnSubscribe(t-> System.out.println("------------"+t)).subscribe();

        return session.send(
                Flux.just("bank", "insurance").map(f ->
                        "Here is your " + f + " products")
                        .map(session::textMessage)
        ).then();

    }


}
