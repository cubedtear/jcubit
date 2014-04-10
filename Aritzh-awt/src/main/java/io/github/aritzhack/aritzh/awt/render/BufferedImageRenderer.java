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

package io.github.aritzhack.aritzh.awt.render;

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
