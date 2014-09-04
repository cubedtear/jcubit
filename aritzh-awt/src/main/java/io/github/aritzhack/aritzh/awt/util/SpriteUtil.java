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

package io.github.aritzhack.aritzh.awt.util;

import io.github.aritzhack.aritzh.awt.render.Sprite;
import io.github.aritzhack.aritzh.util.ARGBColorUtil;

/**
 * Utility class to handle things related to Sprites, such as scaling
 *
 * @author Aritz Lopez
 */
public class SpriteUtil {

    /**
     * Scales an image by a factor, using the given method
     *
     * @param original The sprite to scale
     * @param scale    The factor by which the sprite should be scaled
     * @param method   The scaling method
     * @return The scaled sprite
     */
    public static Sprite scale(Sprite original, float scale, ScalingMethod method) {
        if (scale == 1.0f) return original.copy();

        switch (method) {
            case NEAREST:
                return scaleNearest(original, scale);
            case BILINEAR:
                return scaleBilinear(original, scale);
        }

        return null;
    }

    private static Sprite scaleNearest(Sprite original, float scale) {
        int newWidth = (int) (original.getWidth() * scale);
        int newHeight = (int) (original.getHeight() * scale);

        int[] newPix = new int[newWidth * newHeight];

        int x_ratio = (original.getWidth() << 16) / newWidth + 1;
        int y_ratio = (original.getHeight() << 16) / newHeight + 1;
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                int x2 = (j * x_ratio >> 16);
                int y2 = (i * y_ratio >> 16);
                newPix[(j + i * newWidth)] = original.getPixels()[(x2 + y2 * original.getWidth())];
            }
        }
        return new Sprite(newWidth, newHeight, newPix);

    }

    private static Sprite scaleBilinear(Sprite original, float scale) {
        int newWidth = (int) (original.getWidth() * scale);
        int newHeight = (int) (original.getHeight() * scale);

        int[] newPix = new int[newWidth * newHeight];

        int offset = 0;

        float x_ratio = ((float) (original.getWidth() - 1)) / newWidth;
        float y_ratio = ((float) (original.getWidth() - 1)) / newHeight;
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                int x = (int) (x_ratio * j);
                int y = (int) (y_ratio * i);
                float x_diff = (x_ratio * j) - x;
                float y_diff = (y_ratio * i) - y;
                int index = x + y * original.getWidth();

                int a = original.getPixels()[index];
                int b = original.getPixels()[index + 1];
                int c = original.getPixels()[index + original.getWidth()];
                int d = original.getPixels()[index + original.getWidth() + 1];

                newPix[offset++] = bilinearInterpolate(a, b, c, d, x_diff, y_diff);
            }
        }

        return new Sprite(newWidth, newHeight, newPix);
    }

    private static int bilinearInterpolate(int colorA, int colorB, int colorC, int colorD, float w, float h) {

        ARGBColorUtil.Color A = new ARGBColorUtil.Color(colorA);
        ARGBColorUtil.Color B = new ARGBColorUtil.Color(colorB);
        ARGBColorUtil.Color C = new ARGBColorUtil.Color(colorC);
        ARGBColorUtil.Color D = new ARGBColorUtil.Color(colorD);

        // A*(1-w)*(1-h) + B*w*(1-h) + C*h*(1-w) + D*w*h

        int a = (int) (A.a * (1 - w) * (1 - h) + B.a * w * (1 - h) + C.a * h * (1 - w) + D.a * w * h);
        int r = (int) (A.r * (1 - w) * (1 - h) + B.r * w * (1 - h) + C.r * h * (1 - w) + D.r * w * h);
        int g = (int) (A.g * (1 - w) * (1 - h) + B.g * w * (1 - h) + C.g * h * (1 - w) + D.g * w * h);
        int b = (int) (A.b * (1 - w) * (1 - h) + B.b * w * (1 - h) + C.b * h * (1 - w) + D.b * w * h);

        return ARGBColorUtil.getColor(a, r, g, b);

    }

    public static Sprite rotate90(Sprite original, Rotation rotation) {
        int newWidth = rotation == Rotation._180 ? original.getWidth() : original.getHeight();
        int newHeight = rotation == Rotation._180 ? original.getHeight() : original.getWidth();
        int[] newPix = new int[original.getPixels().length];

        for (int x = 0; x < original.getWidth(); x++) {
            for (int y = 0; y < original.getHeight(); y++) {
                int newY = rotation == Rotation._90 ? x : rotation == Rotation._180 ? original.getHeight() - y - 1 : original.getWidth() - x - 1;
                int newX = rotation == Rotation._90 ? original.getHeight() - y - 1 : rotation == Rotation._180 ? original.getWidth() - x - 1 : y;

                newPix[newX + newY * newWidth] = original.getPixels()[x + y * original.getWidth()];
            }
        }
        return new Sprite(newWidth, newHeight, newPix);
    }

    public static Sprite flipH(Sprite original) {
        int[] newPix = new int[original.getPixels().length];

        for (int y = 0; y < original.getHeight(); y++) {
            int newY = original.getHeight() - y - 1;
            for (int x = 0; x < original.getWidth(); x++) {
                newPix[x + newY * original.getWidth()] = original.getPixels()[x + y * original.getWidth()];
            }
        }
        return new Sprite(original.getWidth(), original.getHeight(), newPix);
    }

    public static Sprite flipV(Sprite original) {
        int[] newPix = new int[original.getPixels().length];

        for (int x = 0; x < original.getWidth(); x++) {
            int newX = (original.getWidth() - x - 1);
            for (int y = 0; y < original.getHeight(); y++) {
                newPix[newX + y * original.getWidth()] = original.getPixels()[x + y * original.getWidth()];
            }
        }
        return new Sprite(original.getWidth(), original.getHeight(), newPix);
    }


    private static int interpolate(int color1, int color2, int L, float l) {
        int a1 = ARGBColorUtil.getAlpha(color1);
        int r1 = ARGBColorUtil.getRed(color1);
        int g1 = ARGBColorUtil.getGreen(color1);
        int b1 = ARGBColorUtil.getBlue(color1);

        int a2 = ARGBColorUtil.getAlpha(color2);
        int r2 = ARGBColorUtil.getRed(color2);
        int g2 = ARGBColorUtil.getGreen(color2);
        int b2 = ARGBColorUtil.getBlue(color2);

        int a3 = (int) (a1 + l * (a2 - a1) / (float) L);
        int r3 = (int) (r1 + l * (r2 - r1) / (float) L);
        int g3 = (int) (g1 + l * (g2 - g1) / (float) L);
        int b3 = (int) (b1 + l * (b2 - b1) / (float) L);

        return ARGBColorUtil.getColor(a3, r3, g3, b3);
    }

    public static enum Rotation {
        _90, _180, _270
    }

    /**
     * Available scaling methods (<a href="http://en.wikipedia.org/wiki/Image_scaling#Scaling_methods">Wikipedia page</a>)
     */
    public static enum ScalingMethod {
        /**
         * Nearest neighbor interpolation (<a href="http://en.wikipedia.org/wiki/Nearest-neighbor_interpolation">Wikipedia page</a>)
         */
        NEAREST,
        /**
         * Bilinear interpolation (<a href="http://en.wikipedia.org/wiki/Bilinear_interpolation">Wikipedia page</a>)
         */
        BILINEAR

    }
}
