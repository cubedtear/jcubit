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

package io.github.aritzhack.aritzh.util;

import com.google.common.base.Preconditions;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Aritz Lopez
 */
public class OneOrOther<T, U> {

    private final Optional<T> one;
    private final Optional<U> other;

    public OneOrOther(@Nullable T one, @Nullable U other) {
        Preconditions.checkArgument(one != null && other == null || one == null && other != null, "One and only one of the two arguments must not be null!");
        this.one = Optional.ofNullable(one);
        this.other = Optional.ofNullable(other);
    }

    public static <T, U> OneOrOther<T, U> ofOne(T one) {
        return new OneOrOther<>(one, null);
    }

    public static <T, U> OneOrOther<T, U> ofOther(U other) {
        return new OneOrOther<>(null, other);
    }

    public Object getNonNull() {
        return one.isPresent() ? one : other;
    }

    public void consume(Consumer<? super T> consumeOne, Consumer<? super U> consumeOther) {
        one.ifPresent(consumeOne);
        other.ifPresent(consumeOther);
    }

    public T getOne() {
        return one.get();
    }

    public U getOther() {
        return other.get();
    }

    public <R> R map(Function<T, R> ofOne, Function<U, R> ofOther) {
        return one.isPresent() ? one.map(ofOne).get() : other.map(ofOther).get();
    }

    public OneOrOther<U, T> flip() {
        return new OneOrOther<>(other.get(), one.get());
    }

}
