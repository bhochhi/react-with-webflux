package com.bhochhi.spring.reactwithwebflux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
class ProductCreatedEventPublisher implements
        ApplicationListener<ProductCreatedEvent>,
        Consumer<FluxSink<ProductCreatedEvent>> {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private final Executor executor;
    private final BlockingQueue<ProductCreatedEvent> queue =
            new LinkedBlockingQueue<>();

    ProductCreatedEventPublisher(Executor executor) {
        this.executor = executor;
    }


    @Override
    public void onApplicationEvent(ProductCreatedEvent event) {
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<ProductCreatedEvent> sink) {

        this.productRepository.findAll()
                .doOnSubscribe(products -> this.eventPublisher.publishEvent((new ProductCreatedEvent(products))));




        this.executor.execute(() -> {
            while (true)
                try {
                    ProductCreatedEvent event = queue.take();
                    sink.next(event);
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }
}