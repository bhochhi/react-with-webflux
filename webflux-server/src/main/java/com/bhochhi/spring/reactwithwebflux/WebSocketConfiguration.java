package com.bhochhi.spring.reactwithwebflux;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Log4j2
@Configuration
class WebSocketConfiguration {

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebClient webClient;


    @Bean
    WebClient webClient() {
        return WebClient.create("http://localhost:8080");
    }

    @Bean
    Executor executor() {
        return Executors.newSingleThreadExecutor();
    }


    @Bean
    HandlerMapping handlerMapping() {

        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws/allinone", new MainWebSocketHandler(objectMapper,productRepository,webClient));
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(1);
        handlerMapping.setUrlMap(map);
        return handlerMapping;
    }




    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

}