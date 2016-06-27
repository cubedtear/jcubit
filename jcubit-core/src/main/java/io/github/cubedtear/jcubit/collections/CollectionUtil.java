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

package io.github.cubedtear.jcubit.collections;

import com.google.common.base.Function;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Aritz Lopez
 */
public enum CollectionUtil {
    ;

    /**
     * Applies a function to all elements in the collection. Note: Uses a for-each loop.
     * The return value of the function is ignored.
     *
     * @param coll     The collection of elements to apply the function to.
     * @param function The function to apply.
     * @param <I>      Collection content type.
     */
    @Deprecated
    public static <I> void applyToAll(Collection<I> coll, Function<I, ?> function) {
        for (I t : coll) function.apply(t);
    }

    /**
     * Applies a function to the keys of the map. Note: Uses a for-each loop.
     *
     * @param map      The map with the keys the function will be applied to.
     * @param function The function to apply.
     * @param <K>      The type of the keys of the map.
     */
    public static <K> void applyToKeys(Map<K, ?> map, Function<? super K, ?> function) {
        for (K key : map.keySet()) function.apply(key);
    }

    /**
     * Applies a function to the values of the map. Note: Uses a for-each loop.
     *
     * @param map      The map with the values the function will be applied to.
     * @param function The function to apply.
     * @param <V>      The type of the values of the map.
     */
    public static <V> void applyToValues(Map<?, V> map, Function<? super V, ?> function) {
        for (V v : map.values()) function.apply(v);
    }

    /**
     * Returns the sum of all integers from the iterable
     *
     * @param iter The iterable
     * @return the sum of all integers from the iterable
     */
    public static int integerSum(Iterable<Integer> iter) {
        int sum = 0;
        for (Integer i : iter)
            sum += i;
        return sum;
    }

    /**
     * Returns the sum of all floats from the iterable
     *
     * @param iter The iterable
     * @return the sum of all floats from the iterable
     */
    public static float floatSum(Iterable<Float> iter) {
        float sum = 0;
        for (Float f : iter)
            sum += f;
        return sum;
    }

    /**
     * Returns the sum of all doubles from the iterable
     *
     * @param iter The iterable
     * @return the sum of all doubles from the iterable
     */
    public static double doubleSum(Iterable<Double> iter) {
        double sum = 0;
        for (Double d : iter)
            sum += d;
        return sum;
    }

    /**
     * Returns the last element of a list, or null if it is empty
     *
     * @param list The list
     * @param <T>  The type of the contents of the list
     * @return the last element of a list, or null if it is empty
     */
    public static <T> T getLast(List<T> list) {
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

}
