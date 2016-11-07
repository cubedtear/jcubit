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

import io.github.cubedtear.jcubit.util.API;

import java.awt.image.BufferedImage;

/**
 * @author Aritz Lopez
 */
public interface IRender {

	/**
	 * Clear the screen to a predefined color.
	 */
	void clear();

	/**
	 * Draws the given array of colors with the given size into the given coordinates.
	 * @param x The x coordinate of the top-left corner of where the colors should be drawn.
	 * @param y The y coordinate of the top-left corner of where the colors should be drawn.
	 * @param width The length of each row in the color array.
	 * @param height The number of rows in the color array.
     * @param colors The color array. Its size must be at least {@code width * height}.
     */
	void draw(int x, int y, int width, int height, int[] colors);

	/**
	 * Draws the given sprite in the given position.
	 * @param x The x coordinate of the top-left corner of where the sprite should be drawn.
	 * @param y The y coordinate of the top-left corner of where the sprite should be drawn.
	 * @param sprite The sprite to draw.
     */
	void draw(int x, int y, Sprite sprite);

	/**
	 * Draws the sprite with the given name in the given position.
	 * @param x The x coordinate of the top-left corner of where the sprite should be drawn.
	 * @param y The y coordinate of the top-left corner of where the sprite should be drawn.
	 * @param spriteName The name of the sprite to be drawn.
     */
	void draw(int x, int y, String spriteName);

	/**
	 * Draws an {@link AnimatedSprite} at the given coordinates, and passes the elapsed time since the last frame,
	 * in order to animate the sprite.
	 * @param x The x coordinate of the top-left corner of where the sprite should be drawn.
	 * @param y The y coordinate of the top-left corner of where the sprite should be drawn.
	 * @param deltaNS The elapsed nanoseconds since the last frame.
     * @param sprite The animates sprite to be drawn.
     */
	@API
	void draw(int x, int y, long deltaNS, AnimatedSprite sprite);

	/**
	 * Sets the clipping region to be used from now on. Subsequent calls to any of the {@code draw} methods
	 * will be clipped to only draw inside the given region.
	 * Can be reset by using calling this method with a region that includes the whole rendering region,
	 * or simply by calling {@link IRender#doNotClip()}.
	 * @param x The x coordinate of the top-left corner of the clipping region.
	 * @param y The y coordinate of the top-left corner of the clipping region.
	 * @param width The width of the clipping region.
	 * @param height The height of the clipping region.
	 */
	@API
	void setClipping(int x, int y, int width, int height);

	/**
	 * Removes the clipping. Subsequent calls to any of the {@code draw} methods will only clip to the whole rendering
	 * region.
	 */
	void doNotClip();

	/**
	 * Sets whether semi-transparent colors should be blended together. My slow the rendering considerably.
	 * @param blend {@code true} if blending is desired.
     */
	@API
	void setBlend(boolean blend);

	/**
	 * @return the width of this renderer (i.e. The width of the area that can be drawn onto).
     */
	int getWidth();

	/**
	 * @return the height of this renderer (i.e. The width of the area that can be drawn onto).
	 */
	int getHeight();

	/**
	 * Sets the color to use when {@link IRender#clear() clear()} is called.
	 *
	 * @param clearColor The background color to use.
	 */
	void setClearColor(int clearColor);
}
