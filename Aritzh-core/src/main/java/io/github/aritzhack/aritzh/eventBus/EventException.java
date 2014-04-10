/*
 * Copyright 2014 Aritz Lopez
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.aritzhack.aritzh.eventBus;

/**
 * A RuntimeException that wraps an exception and saves what EventHandler threw what Throwable because of what event.
 *
 * @author Aritz Lopez
 */
public class EventException extends RuntimeException {


    private final EventHandler handler;
    private final Object event;

    /**
     * Creates a EventException. Supposedly the {@code handler} threw {@code throwable} when handling {@code event}.
     *
     * @param handler   The EventHandler that threw the Throwable.
     * @param event     The event that made the handler throw the Throwable.
     * @param throwable The Throwable that was thrown.
     */
    public EventException(EventHandler handler, Object event, Throwable throwable) {
        super(throwable);
        this.handler = handler;
        this.event = event;
    }

    /**
     * Returns the EventHandler that threw the Throwable.
     *
     * @return the EventHandler that threw the Throwable.
     */
    public EventHandler getThrower() {
        return handler;
    }

    /**
     * Returns the event that made the EventHandler throw the exception.
     *
     * @return the event that made the EventHandler throw the exception.
     */
    public Object getEvent() {
        return event;
    }
}
