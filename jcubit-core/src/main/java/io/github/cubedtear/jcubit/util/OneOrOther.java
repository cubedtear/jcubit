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
 * @author Aritz Lopez
 */
public class OneOrOther<T, U> {

	private final T one;
	private final U other;

	public OneOrOther(@Nullable T one, @Nullable U other) {
		Preconditions.checkArgument(one != null && other == null || one == null && other != null, "One and only one of the two arguments must not be null!");
		this.one = one;
		this.other = other;
	}

	public static <T, U> OneOrOther<T, U> ofOne(T one) {
		return new OneOrOther<>(one, null);
	}

	public static <T, U> OneOrOther<T, U> ofOther(U other) {
		return new OneOrOther<>(null, other);
	}

	public Object getNonNull() {
		return this.isOne() ? one : other;
	}

	public boolean isOne() {
		return this.one != null;
	}

	public T getOne() {
		return one;
	}

	public U getOther() {
		return other;
	}

	public boolean isOther() {
		return this.other != null;
	}

	public OneOrOther<U, T> flip() {
		return new OneOrOther<>(other, one);
	}

}
