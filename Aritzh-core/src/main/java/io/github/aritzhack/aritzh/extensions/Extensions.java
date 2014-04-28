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
import io.github.aritzhack.aritzh.util.ReflectionUtil;
import io.github.aritzhack.aritzh.extensions.events.ExtensionEvent;
import io.github.aritzhack.aritzh.logging.ILogger;

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

    private final ExtensibleApp app;
    private final ILogger logger;

    public Extensions(ExtensibleApp app) {
        this(app, null);
    }

    public Extensions(ExtensibleApp app, ILogger logger) {
        this.app = app;
        this.logger = logger;
    }

    public void loadAllExtensions(File extensionsFolder) throws ReflectiveOperationException, IOException {
        try {
            if (!extensionsFolder.exists() && !extensionsFolder.mkdirs())
                throw new IOException("Could not create extensions' folder");

            ReflectionUtil.addFolderToClasspath(extensionsFolder);

            for (Class c : app.getReflections().getTypesAnnotatedWith(ExtensionData.class)) {
                if (logger != null) logger.d("Found mod class: {}", c.getName());
                this.register(c);
            }
        } catch (IOException e) {
            if (this.logger != null) logger.e("Error loading mods folder", e);
            else throw e;
        }

        this.app.getExtensionsEventBus().post(new ExtensionEvent.ExtensionLoadEvent(app));
    }

    public void register(Class extensionClass) throws IllegalAccessException, InstantiationException {
        Preconditions.checkNotNull(extensionClass);
        Preconditions.checkArgument(ReflectionUtil.classHasAnnotation(extensionClass, ExtensionData.class), "Tried to register non-extension class " + extensionClass + "!");

        Set<Field> instances = app.getReflections().getFieldsAnnotatedWith(ExtensionInstance.class);

        Object instance = null;

        if (instances != null && instances.size() == 1) {
            instance = instances.iterator().next();
        }

        if (instance == null) {
            try {
                instance = extensionClass.newInstance();
            } catch (InstantiationException e) {
                if (logger != null)
                    logger.e("Exception when instantiating extension class", new IllegalArgumentException(e));
                else throw e;
            } catch (IllegalAccessException e) {
                if (logger != null)
                    logger.e("Exception when instantiating extension class", new IllegalArgumentException(e));
                else throw e;
            }
        }

        if (instance == null && logger != null)
            logger.w("Class {} could not be loaded as a extension class", extensionClass);

        ExtensionData data = (ExtensionData) extensionClass.getAnnotation(ExtensionData.class);

        Extension e = new Extension(data.extensionName(), data.version(), data.appVersion(), instance);
        this.extensions.add(e);
        if (this.logger != null) logger.d("Extension \"{}\" successfully loaded", e.name);
    }

    public List<Extension> getExtensions() {
        return this.extensions;
    }
}
