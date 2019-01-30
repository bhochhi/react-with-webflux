package com.bhochhi.spring.reactwithwebflux;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
public class MainWebSocketHandler implements WebSocketHandler {

    private ProductRepository productRepository;
    private WebClient webClient;
    private ObjectMapper objectMapper;


    public MainWebSocketHandler(ObjectMapper objectMapper, ProductRepository productRepository, WebClient webClient) {
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;

        this.webClient = webClient;
    }


    @Override
    public Mono<Void> handle(WebSocketSession session) {

        return session.send(Flux.just(session.textMessage("Server is ready"))
//                        Flux.interval(Duration.ofSeconds(1))
//                        .map(n -> n.toString())
//                        .map(session::textMessage)
        ).and(session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(msg -> log.info("Client request: " + msg))
                .doOnSubscribe(sub -> log.info("Session Started." + session.getId()))
                .doOnTerminate(()->log.info("connection terminated"))
                .doFinally(sig -> log.info("Session Complete." + session.getId()))

        ).and(session.send(Flux.just(session.textMessage("Here I am again for you!!! Send your request...")))).doOnSuccessOrError((s, e) -> {
            if (s != null) {
                log.info("Successfully completed " + s.toString());
            }
            if (e != null) {
                log.info("Error out " + e.toString());

            }
        });

    }


}
