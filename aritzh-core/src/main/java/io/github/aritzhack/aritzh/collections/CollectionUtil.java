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

package io.github.aritzhack.aritzh.collections;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Aritz Lopez
 */
public class CollectionUtil {

    /**
     * Applies a function to all elements in the collection. Note: Uses a for-each loop.
     *
     * @param coll     The collection of elements to apply the function to.
     * @param function The function to apply.
     */
    public static <I, R> void applyToAll(Collection<I> coll, Function<I, R> function) {
        for (I input : coll) function.apply(input);
    }

    /**
     * Applies a function to the keys of the map. Note: Uses a for-each loop.
     *
     * @param map      The map with the keys the function will be applied to.
     * @param function The function to apply.
     */
    public static <I, K extends I> void applyToKeys(Map<K, ?> map, Function<I, ?> function) {
        for (K key : map.keySet()) function.apply(key);
    }

    /**
     * Applies a function to the values of the map. Note: Uses a for-each loop.
     *
     * @param map      The map with the values the function will be applied to.
     * @param function The function to apply.
     */
    public static <I, V extends I> void applyToValues(Map<?, V> map, Function<I, ?> function) {
        for (V value : map.values()) function.apply(value);
    }

    /**
     * Append one or more elements to an array (Add them to the end).
     *
     * @param arr          The array the elements will be appended to.
     * @param lastElements The elements to append, in order (first to last).
     * @return The array with all elements (The same as the {@code arr} argument).
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> T[] append(T[] arr, T... lastElements) {
        T[] ret = (T[]) new Object[arr.length + lastElements.length];
        System.arraycopy(lastElements, 0, ret, arr.length, lastElements.length);
        System.arraycopy(arr, 0, ret, 0, arr.length);
        return ret;
    }

    /**
     * Prepend one or more elements to an array (Add them to the beginning).
     *
     * @param arr           The array the elements will be appended to.
     * @param firstElements The elements to prepend, in order (first to last).
     * @return The array with all elements (The same as the {@code arr} argument).
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> T[] prepend(T[] arr, T... firstElements) {
        T[] ret = (T[]) new Object[arr.length + firstElements.length];
        System.arraycopy(firstElements, 0, ret, 0, firstElements.length);
        System.arraycopy(arr, 0, ret, firstElements.length, arr.length);
        return ret;
    }

    /**
     * Convert a {@link Byte} array to a primitive {@code byte} array.
     *
     * @param byteArray The array to convert to primitive.
     * @return The primitive array the argument represents.
     * @throws NullPointerException If a null was found, thrown when unboxing.
     */
    public static byte[] toPrimitiveArray(Byte[] byteArray) throws NullPointerException {
        Preconditions.checkArgument(byteArray != null);
        byte[] ret = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) ret[i] = byteArray[i];
        return ret;
    }

    /**
     * Convert a {@link Integer} array to a primitive {@code int} array.
     *
     * @param integerArray The array to convert to primitive.
     * @return The primitive array the argument represents.
     * @throws NullPointerException If a null was found, thrown when unboxing.
     */
    public static int[] toPrimitiveArray(Integer[] integerArray) throws NullPointerException {
        Preconditions.checkArgument(integerArray != null);
        int[] ret = new int[integerArray.length];
        for (int i = 0; i < integerArray.length; i++) ret[i] = integerArray[i];
        return ret;
    }

    /**
     * Convert a {@link Short} array to a primitive {@code short} array.
     *
     * @param shortArray The array to convert to primitive.
     * @return The primitive array the argument represents.
     * @throws NullPointerException If a null was found, thrown when unboxing.
     */
    public static short[] toPrimitiveArray(Short[] shortArray) throws NullPointerException {
        Preconditions.checkArgument(shortArray != null);
        short[] ret = new short[shortArray.length];
        for (int i = 0; i < shortArray.length; i++) ret[i] = shortArray[i];
        return ret;
    }

    /**
     * Convert a {@link Long} array to a primitive {@code long} array.
     *
     * @param longArray The array to convert to primitive.
     * @return The primitive array the argument represents.
     * @throws NullPointerException If a null was found, thrown when unboxing.
     */
    public static long[] toPrimitiveArray(Long[] longArray) throws NullPointerException {
        Preconditions.checkArgument(longArray != null);
        long[] ret = new long[longArray.length];
        for (int i = 0; i < longArray.length; i++) ret[i] = longArray[i];
        return ret;
    }

    /**
     * Convert a {@link Float} array to a primitive {@code float} array.
     *
     * @param floatArray The array to convert to primitive.
     * @return The primitive array the argument represents.
     * @throws NullPointerException If a null was found, thrown when unboxing.
     */
    public static float[] toPrimitiveArray(Float[] floatArray) throws NullPointerException {
        Preconditions.checkArgument(floatArray != null);
        float[] ret = new float[floatArray.length];
        for (int i = 0; i < floatArray.length; i++) ret[i] = floatArray[i];
        return ret;
    }

    /**
     * Convert a {@link Double} array to a primitive {@code double} array.
     *
     * @param doubleArray The array to convert to primitive.
     * @return The primitive array the argument represents.
     * @throws NullPointerException If a null was found, thrown when unboxing.
     */
    public static double[] toPrimitiveArray(Double[] doubleArray) throws NullPointerException {
        Preconditions.checkArgument(doubleArray != null);
        double[] ret = new double[doubleArray.length];
        for (int i = 0; i < doubleArray.length; i++) ret[i] = doubleArray[i];
        return ret;
    }

    /**
     * Convert a {@link Character} array to a primitive {@code char} array.
     *
     * @param characterArray The array to convert to primitive.
     * @return The primitive array the argument represents.
     * @throws NullPointerException If a null was found, thrown when unboxing.
     */
    public static char[] toPrimitiveArray(Character[] characterArray) throws NullPointerException {
        Preconditions.checkArgument(characterArray != null);
        char[] ret = new char[characterArray.length];
        for (int i = 0; i < characterArray.length; i++) ret[i] = characterArray[i];
        return ret;
    }

    /**
     * Returns the sum of all integers from the iterable
     * @param iter The iterable
     * @return the sum of all integers from the iterable
     */
    public static int integerSum(Iterable<Integer> iter) {
        int ret = 0;
        for (Integer i : iter) {
            ret += i;
        }
        return ret;
    }

    /**
     * Returns the sum of all floats from the iterable
     * @param iter The iterable
     * @return the sum of all floats from the iterable
     */
    public static float floatSum(Iterable<Float> iter) {
        float ret = 0;
        for (Float i : iter) {
            ret += i;
        }
        return ret;
    }

    /**
     * Returns the sum of all doubles from the iterable
     * @param iter The iterable
     * @return the sum of all doubles from the iterable
     */
    public static double doubleSum(Iterable<Double> iter) {
        double ret = 0;
        for (Double i : iter) {
            ret += i;
        }
        return ret;
    }

    /**
     * Returns the last element of a list, or null if it is empty
     * @param list The list
     * @return the last element of a list, or null if it is empty
     */
    public static <T> T getLast(List<T> list) {
        return list.isEmpty() ? null : list.get(list.size()-1);
    }
}
