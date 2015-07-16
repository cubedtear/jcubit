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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * An EventBus implementation. Where some objects can be registered as event-handlers, and when events are post, they will handle them.
 *
 * @author Aritz Lopez
 */
public class EventBus {

	private final Multimap<Class, EventHandler> handlersByEventType = ArrayListMultimap.create();
	private final String name; // For now useless, will add use for this "shortly"
	private final LoadingCache<Class, Set<Class>> flattenHierarchy = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class, Set<Class>>() {
		@Override
		public Set<Class> load(Class concreteClass) throws Exception {
			Set<Class> ret = Sets.newHashSet();
			for (Class c = concreteClass; c != null; c = c.getSuperclass()) {
				ret.add(c);
				Collections.addAll(ret, c.getInterfaces());
			}
			return ret;
		}
	});

	/**
	 * Creates a default EventBus, with name "Main". Same as calling {@code new EventBus("Main")}.
	 */
	public EventBus() {
		this("Main");
	}

	/**
	 * Creates an EventBus with the specified name.
	 *
	 * @param name The name of the EventBus.
	 */
	public EventBus(String name) {
		this.name = name;
	}

	/**
	 * Registers the object as an event-handler for all methods of its class or superclasses that have the {@link Subscribe} annotation.
	 *
	 * @param listener The object to register as an event-handler.
	 */
	public void register(final Object listener) {
		for (Class clazz = listener.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
			Method[] methods = clazz.getMethods();
			for (Method m : methods) {
				if (!m.isAnnotationPresent(Subscribe.class)) continue;
				Class[] params = m.getParameterTypes();

				if (params.length != 1)
					throw new IllegalArgumentException("Method " + m + " has @Subscribe annotation" +
							"and requires " + params.length +
							" arguments, but event-handling methods must require just one argument");
				Class event = params[0];
				EventHandler handler = new EventHandler(listener, m);
				this.handlersByEventType.put(event, handler);
			}
		}
	}

	/**
	 * Unregisters the object from the event-handlers.
	 *
	 * @param listener The object to unregster.
	 */
	public void unregister(final Object listener) {
		for (Class clazz = listener.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
			this.handlersByEventType.removeAll(clazz);
		}
	}

	/**
	 * Posts an event to all event-handlers registered to handle it.
	 * If a event is not handled, it will be resent as a {@link DeadEvent}.
	 * If an exception is thrown in the handler, the exception will be post as an {@link EventException}.
	 * If handling that EventException more are thrown, they will be ignored.
	 *
	 * @param event The event to post
	 */
	public void post(final Object event) {
		Set<Class> eventTypes = flattenHierarchy(event.getClass());
		boolean handled = false;
		for (Class c : eventTypes) {
			for (EventHandler h : this.handlersByEventType.get(c)) {
				try {
					h.handle(event);
					handled = true;
				} catch (EventException e) {
					if (event instanceof EventException) continue;
					this.post(e); // If an exception is thrown, post the exception as an event, so that exception-handlers can deal with it
				}
			}
		}
		if (!handled && !(event instanceof DeadEvent)) {
			this.post(new DeadEvent(event));
		}
	}

	private Set<Class> flattenHierarchy(final Class c) {
		try {
			return flattenHierarchy.get(c);
		} catch (ExecutionException e) {
			return Sets.newHashSet(c);
		}
	}

	@Override
	public int hashCode() {
		int result = handlersByEventType.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		EventBus eventBus = (EventBus) o;

		return handlersByEventType.equals(eventBus.handlersByEventType) && name.equals(eventBus.name);
	}

	@Override
	public String toString() {
		return "[EventBus " + this.name + "]";
	}
}
