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

package io.github.aritzhack.aritzh.extensions;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.github.aritzhack.aritzh.extensions.events.ExtensionEvent;
import io.github.aritzhack.aritzh.logging.core.ILogger;
import io.github.aritzhack.aritzh.logging.core.NullLogger;
import io.github.aritzhack.aritzh.util.NotNull;
import io.github.aritzhack.aritzh.util.ReflectionUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * Loads the extensions for a {@link io.github.aritzhack.aritzh.extensions.ExtensibleApp}
 *
 * @author Aritz Lopez
 */
public class Extensions {

    private final List<Extension> extensions = Lists.newArrayList();

    @NotNull private final ExtensibleApp app;
    @NotNull private final ILogger logger;

    public Extensions(@NotNull ExtensibleApp app) {
        this(app, null);
    }

    public Extensions(@NotNull ExtensibleApp app, ILogger logger) {
        this.app = app;
        this.logger = NullLogger.getLogger(logger);
    }

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



    public Extension register(Class extensionClass) throws IllegalAccessException, InstantiationException {
        Preconditions.checkNotNull(extensionClass);
        Preconditions.checkArgument(ReflectionUtil.classHasAnnotation(extensionClass, ExtensionData.class), "Tried to register non-extension class " + extensionClass + "!");

        Set<Field> instanceFields = app.getReflections().getFieldsAnnotatedWith(ExtensionInstance.class);

        Object instance = null;

        for(Field f : instanceFields) {
            if(f.getDeclaringClass().equals(extensionClass)) {
                try {
                    instance = f.get(null);
                } catch (NullPointerException e) {
                    instance = null;
                    logger.w("Field {}.{} annotated with @ExtensionInstance, but is not static!", f.getDeclaringClass(), f.getName());
                }
            }
        }

        if (instance == null) {
            try {
                instance = extensionClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                logger.w("Exception when instantiating extension class {}, and @ExtensionInstance static field not present", e, extensionClass.getSimpleName());
            }
        }

        if (instance == null)
            logger.w("Class {} could not be loaded as a extension class", extensionClass);

        ExtensionData data = (ExtensionData) extensionClass.getAnnotation(ExtensionData.class);

        Extension e = new Extension(data.extensionName(), data.version(), data.appVersion(), instance);
        this.extensions.add(e);
        logger.d("Extension \"{}\" successfully loaded", e.name);
        return e;
    }

    public void loadExtensionsFromClasspath() throws ReflectiveOperationException {
        for (Class c : app.getReflections().getTypesAnnotatedWith(ExtensionData.class)) {
            logger.d("Found extension class: {}", c.getName());
            Extension e = this.register(c);
            this.app.getExtensionsEventBus().post(new ExtensionEvent.ExtensionLoadEvent(app, e));
        }
    }

    public List<Extension> getExtensions() {
        return this.extensions;
    }
}
