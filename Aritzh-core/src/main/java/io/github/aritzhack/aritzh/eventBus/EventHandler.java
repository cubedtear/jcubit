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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper for an object an a method of that object, used to ease the handling of events.
 *
 * @author Aritz Lopez
 */
public class EventHandler {
    public final Object listener;
    public final Method handler;

    /**
     * Creates an EventHandler.
     *
     * @param listener The object that will handle the events, and which contains the method {@code handler}.
     * @param handler  The method of the object {@code listener} which will be called to handle the method.
     */
    public EventHandler(Object listener, Method handler) {
        this.listener = listener;
        this.handler = handler;
    }

    /**
     * Handles the event, calling the handler method in the listener object.
     *
     * @param event The event to handle.
     * @throws EventException Thrown if the handling threw a Throwable, other than an {@link Error}.
     * @throws Error          If the method is no longer accessible, or if the arguments don't match, or if the handling method threw it.
     */
    public void handle(Object event) throws EventException {
        try {
            this.handler.invoke(listener, event);
        } catch (IllegalAccessException e) {
            throw new Error("Access level changed, method " + handler + " is no longer accessible!", e);
        } catch (IllegalArgumentException e) {
            throw new Error("Method " + handler + " rejected event " + event, e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Error) throw (Error) e.getCause();
            throw new EventException(this, event, e);
        }
    }
}
