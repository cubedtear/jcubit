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

import io.github.cubedtear.jcubit.math.Rectangle;
import io.github.cubedtear.jcubit.math.Vec2i;
import io.github.cubedtear.jcubit.util.API;

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
     *
     * @param x      The x coordinate of the top-left corner of where the colors should be drawn.
     * @param y      The y coordinate of the top-left corner of where the colors should be drawn.
     * @param width  The length of each row in the color array.
     * @param height The number of rows in the color array.
     * @param colors The color array. Its size must be at least {@code width * height}.
     */
    void draw(int x, int y, int width, int height, int[] colors);

    /**
     * Draws the given sprite in the given position.
     *
     * @param x      The x coordinate of the top-left corner of where the sprite should be drawn.
     * @param y      The y coordinate of the top-left corner of where the sprite should be drawn.
     * @param sprite The sprite to draw.
     */
    void draw(int x, int y, Sprite sprite);

    /**
     * Draws the sprite with the given name in the given position.
     *
     * @param x          The x coordinate of the top-left corner of where the sprite should be drawn.
     * @param y          The y coordinate of the top-left corner of where the sprite should be drawn.
     * @param spriteName The name of the sprite to be drawn.
     */
    void draw(int x, int y, String spriteName);

    /**
     * Draws an {@link AnimatedSprite} at the given coordinates, and passes the elapsed time since the last frame,
     * in order to animate the sprite.
     *
     * @param x       The x coordinate of the top-left corner of where the sprite should be drawn.
     * @param y       The y coordinate of the top-left corner of where the sprite should be drawn.
     * @param deltaNS The elapsed nanoseconds since the last frame.
     * @param sprite  The animates sprite to be drawn.
     */
    @API
    void draw(int x, int y, long deltaNS, AnimatedSprite sprite);

    /**
     * Sets the intersecting region between the current clipping region and the one
     * given in the parameters as the new clipping region to be used from now on.
     * Subsequent calls to any of the {@code draw} methods will be clipped to only draw
     * inside the given region.
     * If a pixel is drawn to (0, 0), it will be drawn to (x, y) instead. And everything
     * out of the rectangle from (0, 0) to (width, height) will not be drawn.
     * Can be reset by calling {@link IRender#doNotClip()}.
     *
     * @param x      The x coordinate of the top-left corner of the clipping region. (Relative to the whole render region).
     * @param y      The y coordinate of the top-left corner of the clipping region. (Relative to the whole render region).
     * @param width  The width of the clipping region.
     * @param height The height of the clipping region.
     */
    @API
    void pushClipping(int x, int y, int width, int height);

    void pushClipping(Rectangle rect);

    /**
     * Remove the last added clipping.
     */
    @API
    void popClipping();

    /**
     * Removes the clipping. Subsequent calls to any of the {@code draw} methods will only clip to the whole rendering
     * region.
     */
    @API
    void doNotClip();

    /**
     * Sets whether semi-transparent colors should be blended together. My slow the rendering considerably.
     *
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

    void pushTranslation(Vec2i delta);

    void popTranslation();

    void drawBorder(Rectangle rect, int size, int color);
}
