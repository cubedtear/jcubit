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

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.github.cubedtear.jcubit.extensions.events.ExtensionEvent;
import io.github.cubedtear.jcubit.logging.core.ILogger;
import io.github.cubedtear.jcubit.logging.core.NullLogger;
import io.github.cubedtear.jcubit.util.API;
import io.github.cubedtear.jcubit.util.NotNull;
import io.github.cubedtear.jcubit.util.Nullable;
import io.github.cubedtear.jcubit.util.ReflectionUtil;
import org.reflections.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

import static org.reflections.ReflectionUtils.*;

/**
 * Loads the extensions for a {@link ExtensibleApp}
 * @author Aritz Lopez
 */
public class Extensions {

	private final List<Extension> extensions = Lists.newArrayList();

	@NotNull
	private final ExtensibleApp app;
	@NotNull
	private final ILogger logger;

	/**
	 * Creates an extension loader for the given {@link ExtensibleApp}.
	 * Nothing will be logged.
	 * @param app The app.
     */
	@API
	public Extensions(@NotNull ExtensibleApp app) {
		this(app, null);
	}

	/**
	 * Creates an extension loader for the given {@link ExtensibleApp}.
	 * Uses the given logger to log some messages.
	 * @param app The app.
	 * @param logger The logger to use. If null, nothing will be logged.
	 */
	@API
	public Extensions(@NotNull ExtensibleApp app, @Nullable ILogger logger) {
		this.app = app;
		this.logger = NullLogger.getLogger(logger);
	}

	/**
	 * Loads extensions from the given folder. Careful! This will load all the jars and zips of the given folder
	 * to the classpath.
	 * @param extensionsFolder The folder from which extensions should be loaded.
	 * @throws ReflectiveOperationException If an error occurs loading the extensions, or adding the folder to
	 * classpath.
	 * @throws IOException If an error occurs when loading the folder into classpath.
     */
	public void loadExtensionsFromFolder(File extensionsFolder) throws ReflectiveOperationException, IOException {
		try {
			Preconditions.checkArgument(extensionsFolder.isDirectory(), "Extensions folder must be a directory");
			if (!extensionsFolder.exists() && !extensionsFolder.mkdirs())
				throw new IOException("Extensions' folder did not exist an could not be created");

			ReflectionUtil.addFolderToClasspath(extensionsFolder);

			loadExtensionsFromClasspath();

		} catch (IOException e) {
			logger.e("Error loading extensions", e);
		}
	}

	/**
	 * Loads extensions from classpath.
	 * @throws ReflectiveOperationException If an error occurs loading the extensions.
	 */
	public void loadExtensionsFromClasspath() throws ReflectiveOperationException {
		for (Class c : app.getReflections().getTypesAnnotatedWith(ExtensionData.class)) {
			logger.d("Found extension class: {}", c.getName());
			Extension e = this.register(c);
			this.app.getExtensionsEventBus().post(new ExtensionEvent.ExtensionLoadEvent(app, e));
		}
	}

	/**
	 * Register a particular class as an extension.
	 * @param extensionClass The class to register.
	 * @return The loaded extension, with its data parsed.
	 * @throws IllegalAccessException If the field annotated with {@link ExtensionInstance} is not public.
	 * @throws InstantiationException If there is no field annotated with {@link ExtensionInstance}, and the
	 * extension class does not provide a public default (no argument) constructor.
     */
	@SuppressWarnings("unchecked")
	public Extension register(Class extensionClass) throws IllegalAccessException, InstantiationException {
		Preconditions.checkNotNull(extensionClass);
		Preconditions.checkArgument(ReflectionUtil.classHasAnnotation(extensionClass, ExtensionData.class), "Tried to register non-extension class " + extensionClass + "!");
		Set<Field> instanceFields = ReflectionUtils.getAllFields(extensionClass, withAnnotation(ExtensionInstance.class), ReflectionUtils.withModifier(Modifier.STATIC));

		Object instance = null;

		for (Field f : instanceFields) {
			try {
				instance = f.get(null);
			} catch (NullPointerException e) {
				instance = null;
				logger.w("Field {}.{} annotated with @ExtensionInstance, but is not static!", f.getDeclaringClass(), f.getName());
			}
		}

		if (instance == null) {
			try {
				instance = extensionClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				logger.w("Exception when instantiating extension class {}, and @ExtensionInstance static field not present", e, extensionClass.getSimpleName());
				throw e;
			}
		}

		ExtensionData data = (ExtensionData) extensionClass.getAnnotation(ExtensionData.class);

		Extension e = new Extension(data.extensionName(), data.version(), data.appVersion(), instance);
		this.extensions.add(e);
		logger.d("Extension \"{}\" successfully loaded", e.name);
		return e;
	}

	/**
	 * @return the list of the loaded extensions.
     */
	public List<Extension> getExtensions() {
		return this.extensions;
	}
}
