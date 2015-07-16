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

/**
 * A function that can be applied to an {@code input} object of type {@code I}, and
 * returns an object of type {@code R}. Accepts an array of objects as parameters,
 * so that they can be used in the implementation
 *
 * @author Aritz Lopez
 */
public interface ParametrizedFunction<I, R> {

	/**
	 * Apply this ParametrizedFunction to {@code input} with {@code args} as parameters
	 *
	 * @param input The object to which this ParametrizedFunction will be applied
	 * @param args  The extra arguments needed to run the function
	 * @return The return value of applying {@code this} to {@code input}
	 */
	public R apply(I input, Object... args);
}
