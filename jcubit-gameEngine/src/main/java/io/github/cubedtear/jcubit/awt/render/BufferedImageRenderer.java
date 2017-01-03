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
import com.google.common.collect.Maps;
import io.github.cubedtear.jcubit.math.Rectangle;
import io.github.cubedtear.jcubit.math.Vec2i;
import io.github.cubedtear.jcubit.util.ARGBColorUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.Map;
import java.util.Stack;

/**
 * Renders to a BufferedImage, which may then be stored as a file, or drawn into a {@link Canvas}
 *
 * @author Aritz Lopez
 */
public class BufferedImageRenderer implements IRender {

    private final BufferedImage image;
    private final int width;
    private final int height;
    private final Rectangle bounds;
    private final Map<String, Sprite> sprites;
    private final int[] pixels;
    private Stack<Rectangle> clippings;
    private Stack<Vec2i> translations;
    private boolean blend = false;
    private int backgroundColor = 0xFF_00_00_00;

    /**
     * Creates a renderer with the given size, and no spritesheet.
     * If this constructor is used, the method {@link BufferedImageRenderer#draw(int, int, String)} will not work, and
     * will throw an {@link IllegalArgumentException}.
     *
     * @param width  The width (in pixels) of the renderer.
     * @param height The height (in pixels) of the
     * @see BufferedImageRenderer#BufferedImageRenderer(int, int, Map)
     */
    public BufferedImageRenderer(int width, int height) {
        this(width, height, Maps.<String, Sprite>newHashMap());
    }

    /**
     * Creates a renderer with the given name and spritesheet.
     *
     * @param width   The width (in pixels) of the renderer.
     * @param height  The height (in pixels) of the renderer.
     * @param sprites The spritesheet.
     */
    public BufferedImageRenderer(int width, int height, Map<String, Sprite> sprites) {
        this.width = width;
        this.height = height;
        this.sprites = sprites;

        this.image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        this.pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData();
        this.bounds = new Rectangle(0, 0, width, height);
        this.clippings = new Stack<>();
        this.translations = new Stack<>();
    }

    public void setBlend(boolean blend) {
        this.blend = blend;
    }

    @Override
    public void clear() {
        Arrays.fill(this.pixels, this.backgroundColor);
    }

    @Override
    public void draw(int x, int y, int width, int height, int[] colors) {
        Rectangle clipped = new Rectangle(x, y, width, height);

        if (!this.translations.empty()) clipped = clipped.move(this.translations.peek());
        if (!this.clippings.isEmpty()) clipped = clipped.intersection(this.clippings.peek());
        clipped = this.bounds.intersection(clipped);

        for (int yp = clipped.y; yp < clipped.getY2(); yp++) {
            final int spriteY = yp - clipped.y;
            if (spriteY < 0) continue; // If the sprite's top left is outside the screen
            for (int xp = clipped.x; xp < clipped.getX2(); xp++) {
                final int spriteX = xp - clipped.x;
                if (spriteX < 0) continue;
                int color = colors[spriteX + spriteY * width];
                int alpha = ARGBColorUtil.getAlpha(color);
                if (alpha == 0) continue;
                final int index = xp + yp * this.width;
                if (!blend || alpha == 0xFF) this.pixels[index] = color;
                else this.pixels[index] = ARGBColorUtil.composite(color, this.pixels[index]);
            }
        }
    }

    @Override
    public void draw(int x, int y, Sprite sprite) {
        Preconditions.checkArgument(sprite != null, "Sprite cannot be null!");
        this.draw(x, y, sprite.getWidth(), sprite.getHeight(), sprite.getPixels());
    }

    @Override
    public void draw(int x, int y, String spriteName) {
        this.draw(x, y, this.sprites.get(spriteName));
    }

    @Override
    public void draw(int x, int y, long deltaNS, AnimatedSprite sprite) {
        this.draw(x, y, sprite.getCurrentFrame(deltaNS));
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void pushClipping(int x, int y, int width, int height) {
        this.pushClipping(new Rectangle(x, y, width, height));
    }

    public void pushClipping(Rectangle rect) {
        if (this.clippings.isEmpty()) {
            if (!this.translations.empty()) this.clippings.push(rect.move(this.translations.peek()));
            else this.clippings.push(rect);
        } else {
            if (!this.translations.empty()) this.clippings.push(rect.move(this.translations.peek()).intersection(this.clippings.peek()));
            else this.clippings.push(rect.intersection(this.clippings.peek()));
        }
    }

    @Override
    public void popClipping() {
        if (!this.clippings.isEmpty()) this.clippings.pop();
    }

    @Override
    public void doNotClip() {
        this.clippings.clear();
    }

    @Override
    public void setClearColor(int clearColor) {
        this.backgroundColor = clearColor;
    }

    public void pushTranslation(Vec2i delta) {
        if (!this.translations.empty()) this.translations.push(delta.add(this.translations.peek()));
        else this.translations.push(delta);
    }

    public void popTranslation() {
        if (!this.translations.empty()) this.translations.pop();
    }

    public void drawBorder(Rectangle rect, int size, int color) {
        if (!this.translations.empty()) rect = rect.move(this.translations.peek());
        for (int x = rect.x; x < rect.width + rect.x; x++) {
            for (int y = rect.y; y < rect.height + rect.y; y++) {
                if (x < size + rect.x || x >= rect.width - size + rect.x || y < size + rect.y || y >= rect.height - size + rect.y) {
                    this.pixels[x + y * this.width] = color;
                }
            }
        }
    }
}
