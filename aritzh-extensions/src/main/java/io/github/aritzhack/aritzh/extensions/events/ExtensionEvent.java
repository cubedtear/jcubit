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

package io.github.aritzhack.aritzh.extensions.events;

import io.github.aritzhack.aritzh.extensions.ExtensibleApp;
import io.github.aritzhack.aritzh.extensions.Extension;

/**
 * Root for all {@link io.github.aritzhack.aritzh.eventBus.EventBus} events related to extensions
 *
 * @author Aritz Lopez
 */
public abstract class ExtensionEvent {

	protected final ExtensibleApp app;
	protected final Extension e;

	public ExtensionEvent(ExtensibleApp app, Extension e) {
		super();
		this.app = app;
		this.e = e;
	}

	public ExtensibleApp getApp() {
		return app;
	}

	public Extension getExtension() {
		return e;
	}

	public static class ExtensionLoadEvent extends ExtensionEvent {

		public ExtensionLoadEvent(ExtensibleApp app, Extension e) {
			super(app, e);
		}
	}

	public static class ExtensionUnloadEvent extends ExtensionEvent {

		public ExtensionUnloadEvent(ExtensibleApp app, Extension e) {
			super(app, e);
		}
	}
}
