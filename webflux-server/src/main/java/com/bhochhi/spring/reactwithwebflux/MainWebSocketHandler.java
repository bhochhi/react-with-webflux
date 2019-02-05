package com.bhochhi.spring.reactwithwebflux;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

        //This implement combines the inbound and outbound streams:

        //3. send


        Flux<WebSocketMessage> productResponse = webClient.get().uri("/products")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToFlux(Product.class)
                .map(product -> {
                    try {
                        return session.textMessage(objectMapper.writeValueAsString(product));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return session.textMessage("PROD-ERROR");
                    }
                });
        Flux<WebSocketMessage> transResponse = webClient.get().uri("/transactions")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToFlux(Transaction.class)
                .map(transaction -> {
                    try {
                        session.getAttributes().put("MessageType","SERVER_DATA");
                        return session.textMessage(objectMapper.writeValueAsString(transaction));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        session.getAttributes().put("MessageType","ERROR");
                        return session.textMessage("TRANS-ERROR");
                    }
                });


        return session.send(Flux.merge(productResponse,transResponse));

        //2. receive==>send

//        Flux<WebSocketMessage> output = session.receive()
////                .doOnNext(message -> {
////                    log.info("From Client: {}",message.getPayloadAsText());
////
////                })
//                .concatMap(message -> {
//                    String requestType = message.getPayloadAsText();
//                    if(requestType.equalsIgnoreCase("GET_PRODUCTS")){
//                        Flux<Product>  productFlux = webClient.get().uri("/products")
//                                .accept(MediaType.APPLICATION_JSON_UTF8)
//                                .retrieve()
//
//                                .bodyToFlux(Product.class);
//                        return productFlux.map(product -> {
//                            try {
//                                return session.textMessage(objectMapper.writeValueAsString(product));
//                            } catch (JsonProcessingException e) {
//                                e.printStackTrace();
//                            }
//                            return session.textMessage("Unable to send product :" +product.getName() );
//                        });
//                    }
//
//                    return Mono.just(session.textMessage("Unable process your request for "+requestType));
//                });

//                .map(value -> {
//                    log.info("value under map==>{}",value.getPayloadAsText());
//
//
//
//                    return session.textMessage("rupee " + value.getPayloadAsText());
//                });

//        log.info("Client request is==>{}",output.);




        //1. ==> send ==> receive==> send

//        return session.send(Flux.just(session.textMessage("Server is ready"))
////                        Flux.interval(Duration.ofSeconds(1))
////                        .map(n -> n.toString())
////                        .map(session::textMessage)
//        ).and(session.receive()
//                .map(WebSocketMessage::getPayloadAsText)
//                .doOnNext(msg -> {
//                    //TODO: make service calls.
//                    log.info("Client request: " + msg);
//                })
//                .doOnSubscribe(sub -> {
//                    log.info("Session Started." + session.getId());
//                    sub.cancel();
//                })
//                .doOnTerminate(() -> log.info("connection terminated"))
//                .doFinally(sig -> log.info("Session Complete." + session.getId()))
//
//        ).and(session.send(Flux.just(session.textMessage("Here I am again for you!!! Send your request..."))));


//        return session.send(output);

    }


}
