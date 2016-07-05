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

package io.github.cubedtear.jcubit.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Aritz Lopez
 */
@SuppressWarnings({"unused", "ThrowableResultOfMethodCallIgnored"})
public class ReflectionUtil extends SecurityManager {

    private static final ReflectionUtil INSTANCE = new ReflectionUtil();
    private static final int OFFSET = 1;

    /**
     * Returns the {@link java.lang.Class} that called the method calling this method.
     * @return The caller of the method that called this method.
     */
    public static Class<?> getCallingClass() {
        return INSTANCE.getClassContext()[OFFSET + 1];
    }

    /**
     * Returns the {@link java.lang.Class} at {@code depth} position in the stack trace.
     * @param depth The number of classes to go back.
     *              -1 will be {@link ReflectionUtil},
     *              0 will be the caller to this method,
     *              and so on.
     * @return the {@link java.lang.Class} at {@code depth} position in the stack trace.
     */
    public static Class<?> getCallingClass(int depth) {
        return INSTANCE.getClassContext()[OFFSET + depth];
    }

    /**
     * Checks if the given class is somewhere in the current stack trace.
     * @param clazz The class to find in the stack trace.
     * @return whether the given class is somewhere in the current stack trace.
     */
    public static boolean isCalledByClass(Class<?> clazz) {
        Class<?>[] classes = INSTANCE.getClassContext();
        for (int i = OFFSET + 1; i < classes.length; i++) {
            if (classes[i] == clazz) {
                return true;
            }
        }
        return false;
    }

    private static final FileFilter jarAndZips = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.isFile() && (f.getName().endsWith(".jar") || f.getName().endsWith(".zip"));
        }
    };

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
     * Adds all files in the folder that the FileFilter accepts to the classpath
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
     * Adds the given path as a folder recursively to the classpath, accepting all files with .zip and .jar extension.
     * @param folder The folder to add to the classpath.
     * @return A map of the paths that raised an exception to their exception.
     * @throws NoSuchMethodException when the system classloader does not have an "addUrl" method.
     */
    public static Map<Path, Exception> addFolderToClasspath(Path folder) throws ReflectiveOperationException {
        return ReflectionUtil.addFolderToClasspath(folder, jarAndZips);
    }

    /**
     * Adds the given path as a folder recursively to the classpath, accepting all files accepted by the file filter.
     * @param folder The folder to add to the classpath.
     * @param fileFilter The files under the given folder accepted by this filter will be added to the classpath.
     * @return A map of the paths that raised an exception to their exception.
     * @throws NoSuchMethodException when the system classloader does not have an "addUrl" method.
     */
    public static Map<Path, Exception> addFolderToClasspath(Path folder, final FileFilter fileFilter) throws NoSuchMethodException {
        final Map<Path, Exception> ret = Maps.newHashMap();

        final URLClassLoader sysURLClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<? extends URLClassLoader> classLoaderClass = URLClassLoader.class;

        final Method method = classLoaderClass.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);

        FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!fileFilter.accept(file.toFile())) return FileVisitResult.CONTINUE;

                try {
                    method.invoke(sysURLClassLoader, file.toUri().toURL());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    ret.put(file, e);
                }
                return FileVisitResult.CONTINUE;
            }
        };

        try {
            Files.walkFileTree(folder, visitor);
        } catch (IOException e) {
            e.printStackTrace();
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
        if (clazz == null || annotation == null) return false;
        if (clazz.isArray()) return classHasAnnotation(clazz.getComponentType(), annotation);
        for (Annotation a : clazz.getAnnotations()) {
            if (a.annotationType().equals(annotation)) return true;
        }
        if (classHasAnnotation(clazz.getSuperclass(), annotation)) return true;
        for (Class inter : clazz.getInterfaces()) {
            if (classHasAnnotation(inter, annotation)) return true;
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

    /**
     * Gets the set of all fields, including the inherited ones.
     * @param c The type for which the fields have to be returned.
     * @return a set containing all the fields of the given class.
     */
    public static Set<Field> getAllFields(Class c) {
        Set<Field> res = Sets.newHashSet();
        Collections.addAll(res, c.getDeclaredFields());
        if (c.getSuperclass() != null) res.addAll(getAllFields(c.getSuperclass()));
        for (Class inter : c.getInterfaces()) {
            res.addAll(getAllFields(inter));
        }
        return res;
    }
}
