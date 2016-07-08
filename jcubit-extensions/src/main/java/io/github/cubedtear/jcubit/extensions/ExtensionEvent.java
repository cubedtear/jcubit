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

package io.github.cubedtear.jcubit.extensions;

import io.github.cubedtear.jcubit.eventBus.EventBus;
import io.github.cubedtear.jcubit.util.API;

/**
 * Root for all {@link EventBus} events related to extensions
 *
 * @author Aritz Lopez
 * @see EventBus
 */
public abstract class ExtensionEvent {

	protected final ExtensibleApp app;
	protected final Extension e;

	/**
	 * Creates an ExtensionEvent from the given App and Extension.
	 * @param app The application that can be extended.
	 * @param e The extension that generated or is the target of the event.
     */
	public ExtensionEvent(ExtensibleApp app, Extension e) {
		this.app = app;
		this.e = e;
	}

	/**
	 * @return The app.
	 */
	@API
	public ExtensibleApp getApp() {
		return app;
	}

	/**
	 * @return The extension.
     */
	@API
	public Extension getExtension() {
		return e;
	}

	/**
	 * Event posted when an extension is getting loaded.
	 */
	@API
	public static class ExtensionLoadEvent extends ExtensionEvent {

		protected ExtensionLoadEvent(ExtensibleApp app, Extension e) {
			super(app, e);
		}
	}

	/**
	 * Event posted when an extension is getting unloaded.
	 */
	@API
	public static class ExtensionUnloadEvent extends ExtensionEvent {

		protected ExtensionUnloadEvent(ExtensibleApp app, Extension e) {
			super(app, e);
		}
	}
}
