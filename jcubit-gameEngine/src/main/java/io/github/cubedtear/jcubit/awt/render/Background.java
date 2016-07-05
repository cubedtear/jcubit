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

/**
 * Utility class to draw backgrounds that move to the left at a constant speed.
 * @author Aritz Lopez
 */
public class Background {

	protected final Sprite sprite;
	protected final boolean alignToBottom;
	protected final int period;
	protected int xPos = 0;
	protected long delta = 0;

	/**
	 * Creates a background that will be aligned as said, and will move at the given speed. The speed is in
	 * pixels per second.
	 * @param sprite The sprite of the background.
	 * @param alignToBottom Whether the background should be aligned to the bottom ({@code true}) or the top {@code false} of the screen.
	 * @param speed The pixels the background should be moved, per second.
     */
	public Background(Sprite sprite, boolean alignToBottom, int speed) {
		this.sprite = sprite;
		this.alignToBottom = alignToBottom;
		this.period = 1_000_000_000 / speed;
	}

	/**
	 * Draw the background into the given renderer.
	 * @param render The renderer into which the background should be drawn.
	 * @param deltaNS The number of nanoseconds elapsed since the last call to this method.
     */
	public void render(IRender render, long deltaNS) {
		final int yPos = alignToBottom ? render.getHeight() - this.sprite.getHeight() : 0;

		delta += deltaNS;

		int xPos = this.xPos;

		if (delta > period) {
			delta -= period;
			xPos--;
		}
		this.xPos = xPos;

		render.draw(xPos, yPos, sprite);

		while (xPos + sprite.getWidth() < render.getWidth()) {
			xPos += sprite.getWidth();
			render.draw(xPos, yPos, sprite);
		}
	}
}
