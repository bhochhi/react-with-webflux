//package com.bhochhi.spring.reactwithwebflux;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.HandlerMapping;
//import org.springframework.web.reactive.config.EnableWebFlux;
//import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.WebSocketMessage;
//import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
//import reactor.core.publisher.Flux;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//@Configuration
//@EnableWebFlux
//public class WebSocketConfig {
//
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Bean
//    public HandlerMapping handlerMapping() {
//
//        Map<String, WebSocketHandler> map = new HashMap<>();
//        map.put("/ws", new MainWebSocketHandler(this.productRepository));
//
//        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
//        mapping.setUrlMap(map);
//        return mapping;
//    }
//
//    @Bean
//    WebSocketHandlerAdapter webSocketHandlerAdapter() {
//        return new WebSocketHandlerAdapter();
//    }
//
//
//
//}
