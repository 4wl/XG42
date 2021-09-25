package com.unknown.xg42.event.events.api;

/**
 * Handles events.
 *
 * @param <T>
 */
public interface Invoker<T>
{

    /**
     * Called when an event is posted on the bus.
     *
     * @param event the event.
     */
    void invoke(T event);

}
