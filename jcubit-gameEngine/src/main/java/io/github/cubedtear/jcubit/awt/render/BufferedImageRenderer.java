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
import io.github.cubedtear.jcubit.math.Vec4i;
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
    private final Map<String, Sprite> sprites;
    private final int[] pixels;
    private Stack<Vec4i> clippings;
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
     * @param height  The height (in pixels) of the
     * @param sprites The spritesheet.
     */
    public BufferedImageRenderer(int width, int height, Map<String, Sprite> sprites) {
        this.width = width;
        this.height = height;
        this.sprites = sprites;

        this.image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        this.pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData();
        this.clippings = new Stack<>();
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
        Vec4i clip;
        if (this.clippings.isEmpty()) {
            clip = new Vec4i(0, 0, width, height);
        } else clip = clippings.pop();

        int maxX = Math.min(clip.z - x, width);
        int maxY = Math.min(clip.w - y, height);
        int minX = Math.max(clip.x - x, 0);
        int minY = Math.max(clip.y - y, 0);

        for (int yp = minY; yp < maxY; yp++) {
            final int beforeY = yp + y;
            if (beforeY < 0) continue;
            for (int xp = minX; xp < maxX; xp++) {
                final int beforeX = xp + x;
                if (beforeX < 0) continue;
                int color = colors[xp + yp * width];
                int alpha = ARGBColorUtil.getAlpha(color);
                if (alpha == 0) continue;
                final int index = beforeX + beforeY * this.width;
                if (!blend || alpha == 0xFF) this.pixels[(index)] = color;
                else this.pixels[(index)] = ARGBColorUtil.composite(color, this.pixels[(index)]);
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
        if (this.clippings.isEmpty()) this.clippings.push(new Vec4i(x, y, width, height));
        else {
            final Vec4i top = this.clippings.pop();
            Rectangle prev = new Rectangle(top.x, top.y, top.z, top.w);
            Rectangle next = new Rectangle(x, y, width, height);
            Rectangle intersection = next.intersection(prev);
            this.clippings.push(new Vec4i(intersection.x, intersection.y, intersection.width, intersection.height));
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
}
