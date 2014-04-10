/*
 * Copyright (c) 2014 Aritzh (Aritz Lopez)
 *
 * This file is part of AritzhUtil
 *
 * AritzhUtil is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * AritzhUtil is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with AritzhUtil.
 * If not, see http://www.gnu.org/licenses/.
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
