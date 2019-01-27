package com.bhochhi.spring.reactwithwebflux;

import org.springframework.context.ApplicationEvent;

public class ProductCreatedEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ProductCreatedEvent(Object source) {
        super(source);
    }
}
