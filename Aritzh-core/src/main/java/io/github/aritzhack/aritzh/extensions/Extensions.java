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

package io.github.aritzhack.aritzh.extensions;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.github.aritzhack.aritzh.ReflectionUtil;
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
            } catch (InstantiationException | IllegalAccessException e) {
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
