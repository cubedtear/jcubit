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

import com.google.common.base.Preconditions;

/**
 * Utility class to hold one of two objects, of possibly different type.
 * @author Aritz Lopez
 * @deprecated It is just silly.
 */
@Deprecated
public class OneOrOther<T, U> {

	private final T one;
	private final U other;

	/**
	 * Holds either {@code one} or {@code other}. One of the two MUST be {@code null}.
	 * @param one The one.
	 * @param other The other.
     */
	public OneOrOther(@Nullable T one, @Nullable U other) {
		Preconditions.checkArgument(one != null && other == null || one == null && other != null, "One and only one of the two arguments must not be null!");
		this.one = one;
		this.other = other;
	}

	/**
	 * Holds the given object, as One.
	 * @param one The object to hold.
	 * @param <T> The type of the hold object.
	 * @param <U> The type that would be the other possible object.
     * @return The given object.
     */
	public static <T, U> OneOrOther<T, U> ofOne(T one) {
		return new OneOrOther<>(one, null);
	}

	/**
	 * Holds the given object, as Other.
	 * @param other The object to hold.
	 * @param <T> The type that would be the other possible object.
	 * @param <U> The type of the hold object.
	 * @return The given object.
	 */
	public static <T, U> OneOrOther<T, U> ofOther(U other) {
		return new OneOrOther<>(null, other);
	}

	/**
	 * @return The one that is not null
	 */
	public Object getNonNull() {
		return this.isOne() ? one : other;
	}

	/**
	 * @return whether the object hold is of the first type.
	 */
	public boolean isOne() {
		return this.one != null;
	}

	/**
	 * @return whether the object hold is of the second type.
	 */
	public boolean isOther() {
		return this.other != null;
	}

	/**
	 * @return the object of the first type. If the second type is hold, null is returned.
	 */
	public T getOne() {
		return one;
	}

	/**
	 * @return the object of the second type. If the first type is hold, null is returned.
	 */
	public U getOther() {
		return other;
	}

	/**
	 * @return Swaps the first and second types.
     */
	public OneOrOther<U, T> flip() {
		return new OneOrOther<>(other, one);
	}

}
