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

import java.util.Arrays;

/**
 * @author Aritz Lopez
 */
public class Sprite {

    private final int[] pixels;
    private final int width;
    private final int height;

    public Sprite(int width, int height, int[] pixels) {
        Preconditions.checkArgument(width >= 0 && height >= 0, "Sprite sizes cannot be negative");
        Preconditions.checkArgument(pixels.length == width * height, "Pixel array size does not match the given width and height");
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public Sprite(int width, int height, int color) {
        Preconditions.checkArgument(width >= 0 && height >= 0, "Sprite sizes cannot be negative");
        Preconditions.checkArgument((long) width * (long) height < Integer.MAX_VALUE, "Sizes are too big (" + width + " * " + height + " > Integer.MAX_VALUE)");
        this.height = height;
        this.width = width;
        this.pixels = new int[width * height];
        Arrays.fill(this.pixels, color);
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Sprite copy() {
        int[] newPix = Arrays.copyOf(this.pixels, this.pixels.length);
        return new Sprite(this.width, this.height, newPix);
    }
}
