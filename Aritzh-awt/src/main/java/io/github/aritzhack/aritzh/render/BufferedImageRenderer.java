/*
 * Copyright (c) 2014 Aritzh (Aritz Lopez)
 *
 * This file is part of AritzhUtil
 *
 * AritzhUtil is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * AritzhUtil is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with AritzhUtil.
 * If not, see http://www.gnu.org/licenses/.
 */

package io.github.aritzhack.aritzh.render;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.github.aritzhack.aritzh.ARGBColorUtil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Aritz Lopez
 */
public class BufferedImageRenderer implements IRender {

    private final BufferedImage image;
    private final int width;
    private final int height;
    private final Map<String, Sprite> sprites;
    private final int[] pixels;

    public BufferedImageRenderer(int width, int height) {
        this(width, height, Maps.newHashMap());
    }

    public BufferedImageRenderer(int width, int height, Map<String, Sprite> sprites) {
        this.width = width;
        this.height = height;
        this.sprites = sprites;

        this.image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        this.pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData();
    }

    @Override
    public void clear() {
        Arrays.fill(this.pixels, 0x00_00_00_FF);
    }

    @Override
    public void draw(int x, int y, int width, int height, int[] colors) {
        int maxX = Math.min(this.width - x, width);
        int maxY = Math.min(this.height - y, height);


        for (int yp = 0; yp < maxY; yp++) {
            final int beforeY = yp + y;
            if (beforeY < 0) continue;
            for (int xp = 0; xp < maxX; xp++) {
                final int beforeX = xp + x;
                if (beforeX < 0) continue;
                int color = colors[xp + yp * width];
                if (ARGBColorUtil.getAlpha(color) == 0) continue;
                this.pixels[beforeX + beforeY * this.width] = color;
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

    @Override
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
}
