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

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * Class to hold a list of sprites, and animate it.
 * @author Aritz Lopez
 */
public class AnimatedSprite {

	private final long delay;
	private final int spriteAmount;
	List<Sprite> sprites = Lists.newArrayList();
	private long currentDelta;
	private int currentSprite = 0;

	/**
	 * Creates an animated sprite from the given spritesheet, and name, the number of frames the animation
	 * consists of, and the delay between frames in milliseconds.
	 *
	 * The animation's sprites will be taken from the spritesheet with names in the form of <i>nameNUMBER</i>, where
	 * NUMBER is a number from 0 to {@code frameCount - 1}.
	 * @param sprites The spritesheet from which the animation's frames will be taken.
	 * @param name The base name of the animation. To get each frame an index will be appended to this base name.
	 * @param frameCount The total number of frames the animation cosnsists of.
	 * @param delayInMillis The delay between two consecutive frames. In other words, the number of milliseconds each
	 *                      frame will stay.
     */
	public AnimatedSprite(Map<String, Sprite> sprites, String name, int frameCount, long delayInMillis) {
		this.delay = delayInMillis * 1_000_000;

		for (int i = 0; i < frameCount; i++) {
			Sprite s = sprites.get(name + i);
			if (s == null) break;
			this.sprites.add(s);
		}
		this.spriteAmount = this.sprites.size();
		currentDelta = 0;
	}

	/**
	 * Calculates and returns the next frame, after {@code delta} milliseconds from the last one.
	 * @param delta The elapsed milliseconds since the last call to this method.
	 * @return The next sprite to display.
     */
	public Sprite getCurrentFrame(long delta) {
		this.currentDelta += delta;
		if (this.currentDelta > this.delay) {
			this.currentDelta -= this.delay;
			this.currentSprite++;
			if (this.currentSprite >= this.spriteAmount) this.currentSprite = 0;
		}
		return this.sprites.get(currentSprite);
	}
}
