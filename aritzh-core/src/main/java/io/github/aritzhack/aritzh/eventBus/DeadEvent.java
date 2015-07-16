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
 * An event wrapper to wrap events that were not handled.
 *
 * @author Aritz Lopez
 */
public class DeadEvent {

	private final Object event;

	public DeadEvent(Object event) {
		this.event = event;
	}

	public Object getEvent() {
		return event;
	}
}
