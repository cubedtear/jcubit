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
