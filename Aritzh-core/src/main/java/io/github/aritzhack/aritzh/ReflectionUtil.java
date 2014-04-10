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

package io.github.aritzhack.aritzh;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author Aritz Lopez
 */
@SuppressWarnings({"unused", "ThrowableResultOfMethodCallIgnored"})
public class ReflectionUtil {

    private static final FileFilter jarAndZips = f -> f.isFile() && (f.getName().endsWith(".jar") || f.getName().endsWith(".zip"));

    /**
     * Adds all jars and zips in the folder to the classpath
     *
     * @param folder The folder from which the files to add are
     * @return A mapping of the errored files to their corresponding exception.
     * @throws ReflectiveOperationException If the system class loader doesn't have an "addUrl" method
     * @see ReflectionUtil#addFolderToClasspath(java.io.File, java.io.FileFilter)
     */
    public static Map<File, Exception> addFolderToClasspath(File folder) throws ReflectiveOperationException {
        return ReflectionUtil.addFolderToClasspath(folder, jarAndZips);
    }

    /**
     * Adds all files in the folder that the filefilter accepts to the classpath
     *
     * @param folder     The folder into which the files to add are
     * @param fileFilter The filter that tells what files should be added and which not
     * @return A mapping of the errored files to their corresponding exception.
     * @throws ReflectiveOperationException If the system class loader doesn't have an "addUrl" method
     */
    public static Map<File, Exception> addFolderToClasspath(File folder, FileFilter fileFilter) throws ReflectiveOperationException {

        Map<File, Exception> ret = Maps.newHashMap();

        URLClassLoader sysURLClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<? extends URLClassLoader> classLoaderClass = URLClassLoader.class;

        Method method = classLoaderClass.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        for (File f : folder.listFiles(fileFilter)) {
            try {
                method.invoke(sysURLClassLoader, f.toURI().toURL());
            } catch (ReflectiveOperationException | IOException e) {
                ret.put(f, e);
            }
        }
        return ret;
    }

    /**
     * Returns true if that class or any of its supertypes has the annotation
     *
     * @param clazz      The class that needs the annotation
     * @param annotation The annotation to look for
     * @return true if and only if the class or any of its supertypes has the annotation
     */
    public static boolean classHasAnnotation(Class clazz, Class<? extends Annotation> annotation) {
        try {
            Set<Class> hierarchy = ReflectionUtil.flattenHierarchy(clazz);
            for (Class c : hierarchy) {
                if (c.isAnnotationPresent(annotation)) return true;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return false;
    }

    private static Set<Class> flattenHierarchy(Class clazz) {
        Set<Class> ret = Sets.newHashSet();

        ret.add(clazz);

        Class up;
        while ((up = clazz.getSuperclass()) != null) {
            ret.add(up);
            ret.addAll(Arrays.asList(up.getInterfaces()));
            clazz = up;
        }

        return ret;
    }
}
