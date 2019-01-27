package com.bhochhi.spring.reactwithwebflux;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.convert.JsonSchemaMapper;
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.WebSocketMessage;
//import org.springframework.web.reactive.socket.WebSocketSession;
//import reactor.core.publisher.Mono;
//
//import java.time.Duration;
//
//public class MainWebSocketHandler implements WebSocketHandler {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    public MainWebSocketHandler(ProductRepository productRepository){
//
//        this.productRepository = productRepository;
//    }
//    @Override
//    public Mono<Void> handle(WebSocketSession session) {
////        String protocol = session.getHandshakeInfo().getSubProtocol();
////
//////        WebSocketMessage message = session.textMessage("Hello world");
////        this.productRepository.findAll().collectList().subscribe(list->{
////            System.out.println("----"+list);
////            WebSocketMessage message = session.textMessage(list.toString());
////            session.send(Mono.delay(Duration.ofMillis(100)).thenMany(Mono.just(message)));
////        });
//////        session.send(new WebSocketMessage("products", DataBufferUtils.write()));
//////                this.productRepository.findAll().collectList().map(list-> JsonWriter.toJson(list)).subscribe(text->session.textMessage(text)));
////
////
////        return  session.send(Mono.delay(Duration.ofMillis(100)).thenMany(Mono.just(null)));
//    }
//}


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class MainWebSocketHandler implements WebSocketHandler{

    private final Flux<ProductCreatedEvent> publish;
    private ProductRepository productRepository;
    private ObjectMapper objectMapper;
    private ProductCreatedEventPublisher productCreatedEventPublisher;

    @Autowired
    private ApplicationEventPublisher eventPublisher;



    public MainWebSocketHandler(ObjectMapper objectMapper,
                                ProductCreatedEventPublisher productCreatedEventPublisher, ProductRepository productRepository){
        this.objectMapper = objectMapper;
        this.productCreatedEventPublisher = productCreatedEventPublisher;

        publish = Flux
                .create(productCreatedEventPublisher)
                .share();
        this.productRepository = productRepository;
    }


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        this.productRepository.findAll()
                .doOnSubscribe(products -> this.eventPublisher.publishEvent((new ProductCreatedEvent(products))));



        Flux<WebSocketMessage> messageFlux = publish
                    .map(evt -> {
                        try {

                            return objectMapper.writeValueAsString(evt.getSource());
                        }
                        catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(str -> {
                        log.info("sending " + str);
                        return session.textMessage(str);
                    });

            return session.send(messageFlux);
        }


}
