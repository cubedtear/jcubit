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

package io.github.cubedtear.jcubit.awt.render;

import com.google.common.base.Preconditions;

/**
 * Collection of {@link Background Backgrounds}, used to only have to draw the whole background once.
 * @author Aritz Lopez
 */
public class ParallaxBG {
	private final Background[] backgrounds;

	/**
	 * Creates the collection of backgrounds. They will be drawn in the order given, which means
	 * the first background will be the one in the back, and the last one the one in the front.
	 * @param backgrounds The background that will be drawn.
     */
	public ParallaxBG(Background... backgrounds) {
		Preconditions.checkArgument(backgrounds != null, "Background list cannot be null!");
		Preconditions.checkArgument(backgrounds.length != 0, "Background list cannot be empty!");

		this.backgrounds = backgrounds;
	}

	/**
	 * Draw all the backgrounds.
	 * @param render The renderer into which the background will be rendered.
	 * @param deltaNS The nanoseconds elapsed since the last frame.
     */
	public void render(IRender render, long deltaNS) {
		for (Background b : this.backgrounds) {
			b.render(render, deltaNS);
		}
	}
}
