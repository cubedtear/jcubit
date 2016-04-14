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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
	 * @deprecated Use {@link Collection#forEach(Consumer)} instead.
	 */
	@Deprecated
	public static <I> void applyToAll(Collection<I> coll, Function<I, ?> function) {
		coll.forEach(function::apply);
	}

	/**
	 * Applies a function to the keys of the map. Note: Uses a for-each loop.
	 *
	 * @param map      The map with the keys the function will be applied to.
	 * @param function The function to apply.
	 * @param <K>      The type of the keys of the map.
	 * @deprecated Use {@link Collection#forEach(Consumer) map.keySet().forEach()} instead.
	 */
	public static <K> void applyToKeys(Map<K, ?> map, Function<? super K, ?> function) {
		map.keySet().forEach(function::apply);
	}

	/**
	 * Applies a function to the values of the map. Note: Uses a for-each loop.
	 *
	 * @param map      The map with the values the function will be applied to.
	 * @param function The function to apply.
	 * @param <V>      The type of the values of the map.
	 * @deprecated Use {@link Collection#forEach(Consumer) map.values().forEach()} instead.
	 */
	public static <V> void applyToValues(Map<?, V> map, Function<? super V, ?> function) {
		map.values().forEach(function::apply);
	}

	/**
	 * Returns the sum of all integers from the iterable
	 *
	 * @param iter The iterable
	 * @return the sum of all integers from the iterable
	 * @deprecated Use {@link IntStream#sum()} instead
	 */
	@Deprecated
	public static int integerSum(Iterable<Integer> iter) {
		return StreamSupport.stream(iter.spliterator(), false).mapToInt(i -> i).sum();
	}

	/**
	 * Returns the sum of all floats from the iterable
	 *
	 * @param iter The iterable
	 * @return the sum of all floats from the iterable
	 * @deprecated Use {@link DoubleStream#sum()} instead
	 */
	@Deprecated
	public static float floatSum(Iterable<Float> iter) {
		return (float) StreamSupport.stream(iter.spliterator(), false).mapToDouble(i -> i).sum();
	}

	/**
	 * Returns the sum of all doubles from the iterable
	 *
	 * @param iter The iterable
	 * @return the sum of all doubles from the iterable
	 * @deprecated Use {@link DoubleStream#sum()} instead
	 */
	@Deprecated
	public static double doubleSum(Iterable<Double> iter) {
		return StreamSupport.stream(iter.spliterator(), false).mapToDouble(i -> i).sum();
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
