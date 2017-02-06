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

package io.github.cubedtear.jcubit.awt.util;

import com.google.common.collect.Maps;
import io.github.cubedtear.jcubit.awt.render.Sprite;
import io.github.cubedtear.jcubit.util.API;
import io.github.cubedtear.jcubit.util.ARGBColorUtil;
import io.github.cubedtear.jcubit.util.Set2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

/**
 * Utility class to handle things related to Sprites, such as scaling
 *
 * @author Aritz Lopez
 */
@API
public class SpriteUtil {

    private static final Comparator<Sprite> spriteComparator = new Comparator<Sprite>() {
        @Override
        public int compare(Sprite o1, Sprite o2) {
            return o1.getWidth() == o2.getWidth() ? o1.getHeight() - o2.getHeight() : o1.getWidth() - o2.getWidth();
        }
    };

    /**
     * Creates a new Sprite with a circle drawn onto it
     *
     * @param diameter  The diameter of the circle (will be the width and height of the sprite)
     * @param color     The color of the circle (in ARGB format: 0xAARRGGBB)
     * @param lineWidth The thickness of the circle. <span style="color:Aqua; font-weight:bold">Warning: if thickness is bigger than 1, it will not draw a perfectly filled circle</span>
     * @return A new Sprite with a circle drawn onto it
     */
    @API
    public static Sprite circle(int diameter, int color, int lineWidth) {

        int[] pix = new int[diameter * diameter];

        for (int i = 0; i < lineWidth; i++) {
            drawCircle(pix, diameter - i, diameter, color);
        }

        return new Sprite(diameter, diameter, pix);
    }

    private static void drawCircle(int[] pix, int diameter, int width, int color) {

        int center = width / 2;
        int radius = diameter / 2 - 1;

        int d = (5 - (radius * 4)) / 4;
        int x = 0;
        int y = radius;

        do {
            pix[center + x + (center + y) * width] = color;
            pix[center + x + (center - y) * width] = color;
            pix[center - x + (center + y) * width] = color;
            pix[center - x + (center - y) * width] = color;
            pix[center + y + (center + x) * width] = color;
            pix[center + y + (center - x) * width] = color;
            pix[center - y + (center + x) * width] = color;
            pix[center - y + (center - x) * width] = color;

            if (d < 0) {
                d += 2 * x + 1;
            } else {
                d += 2 * (x - y) + 1;
                y--;
            }
            x++;
        } while (x <= y);
    }

    /**
     * Scales an image by a factor, using the given method
     *
     * @param original The sprite to scale
     * @param scale    The factor by which the sprite should be scaled
     * @param method   The scaling method
     * @return The scaled sprite
     */
    @API
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

    /**
     * Flips a Sprite horizontally (as if it was seen in a mirror above or below it)
     *
     * @param original The sprite to flip horizontally
     * @return The Sprite flipped
     */
    @API
    public static Sprite flipH(Sprite original) {
        int[] newPix = new int[original.getPixels().length];

        for (int y = 0; y < original.getHeight(); y++) {
            int newY = original.getHeight() - y - 1;
            System.arraycopy(original.getPixels(), y * original.getWidth(), newPix, newY * original.getWidth(), original.getWidth());
        }
        return new Sprite(original.getWidth(), original.getHeight(), newPix);
    }

    /**
     * Flips a Sprite vertically (as if it was seen in a mirror to the left or right)
     *
     * @param original The sprite to flip vertically
     * @return The Sprite flipped
     */
    @API
    public static Sprite flipV(Sprite original) {
        int[] newPix = new int[original.getPixels().length];

        for (int x = 0; x < original.getWidth(); x++) {
            int newX = original.getWidth() - x - 1;
            for (int y = 0; y < original.getHeight(); y++) {
                newPix[newX + y * original.getWidth()] = original.getPixels()[x + y * original.getWidth()];
            }
        }
        return new Sprite(original.getWidth(), original.getHeight(), newPix);
    }

    /**
     * Converts a Sprite into a BufferedImage.
     *
     * @param s The sprite
     * @return an image representing the given sprite.
     */
    @API
    public static BufferedImage toImage(Sprite s) {
        BufferedImage image = new BufferedImage(s.getWidth(), s.getHeight(), BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, s.getWidth(), s.getHeight(), s.getPixels(), 0, s.getWidth());
        return image;
    }

    /**
     * Packs a set of sprites into a single sprite.
     *
     * @param sprites The sprites to pack.
     * @return A set of two objects: The sprite, where all the given sprites are packed into, and a map of
     * sprite name to the rectangle that corresponds to it in the generated spritesheet.
     */
    @API
    public static Set2<Sprite, Map<String, Rectangle>> packSprites(Map<Sprite, String> sprites) {
        int heightSum = 0, widthSum = 0;
        ArrayList<Sprite> spriteList = new ArrayList<>(sprites.keySet());
        Collections.sort(spriteList, spriteComparator);

        for (Sprite s : spriteList) {
            heightSum += s.getHeight();
            widthSum += s.getWidth();
        }

        Map<Sprite, Rectangle> packing = Maps.newHashMap();
        int x = 0;
        int y = 0;
        int maxY = 0;

        widthSum /= (Math.floor(Math.sqrt(spriteList.size())));
        for (Sprite s : spriteList) {
            if (x + s.getWidth() > widthSum) {
                if (y + s.getHeight() > heightSum) {
                    heightSum += s.getHeight();
                }
                x = 0;
                y += maxY;
                maxY = 0;
            }
            if (s.getHeight() > maxY) maxY = s.getHeight();

            packing.put(s, new Rectangle(x, y, s.getWidth(), s.getHeight()));
            x += s.getWidth();
        }

        BufferedImage img = new BufferedImage(widthSum, heightSum, BufferedImage.TYPE_INT_ARGB);

        for (Map.Entry<Sprite, Rectangle> e : packing.entrySet()) {
            Rectangle rect = e.getValue();
            img.setRGB(rect.x, rect.y, rect.width, rect.height, e.getKey().getPixels(), 0, rect.width);
        }

        Map<String, Rectangle> ret2 = Maps.newHashMap();

        for (Sprite s : spriteList) {
            String name = sprites.get(s);
            Rectangle rect = packing.get(s);
            ret2.put(name, rect);
        }
        return new Set2<>(new Sprite(cropBottomTransparent(img)), ret2);
    }

    private static BufferedImage cropBottomTransparent(BufferedImage img) {
        int maxY = img.getHeight();
        boolean isTransparent = true;

        while (isTransparent) {
            maxY--;
            if (maxY == 0) break;
            for (int x = 0; x < img.getWidth() && isTransparent; x++) {
                if (img.getRGB(x, maxY) != 0) isTransparent = false;
            }
        }

        return img.getSubimage(0, 0, img.getWidth(), maxY + 1);
    }

    /**
     * Converts a map of sprite names to regions into a sprite sheet descriptor. Used two write the second output of
     * {@link SpriteUtil#packSprites(Map)} int a string, so that it can be written into a file.
     * Careful: The name of the spritesheet image file will have to be written into the file before this is written!
     *
     * @param sheet The sheet to write into a string.
     * @return The string representing the spritesheet.
     */
    @API
    public static String sheetMapToString(Map<String, Rectangle> sheet) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Rectangle> e : sheet.entrySet()) {
            Rectangle r = e.getValue();
            result
                    .append(e.getKey().replaceAll("[^\\w]]", "_"))
                    .append(".png ")
                    .append(r.x)
                    .append(" ")
                    .append(r.y)
                    .append(" ")
                    .append(r.width)
                    .append(" ")
                    .append(r.height)
                    .append("\n");
        }
        return result.toString();
    }

    @API
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

    /**
     * <p>Rotates a Sprite</p>
     * Note: If the angle is either 90, 180, 270 or -90,
     * {@link SpriteUtil#rotate90(Sprite, SpriteUtil.Rotation) SpriteUtil.rotate90(Sprite, Rotation)}
     * will be used instead, since it is much faster
     *
     * @param original The sprite to rotate
     * @param angle    The rotation angle, in degrees
     * @return The rotated image
     */
    @API
    public static Sprite rotate(Sprite original, double angle) {

        if (angle == 90.0) return SpriteUtil.rotate90(original, Rotation._90);
        else if (angle == 180.0) return SpriteUtil.rotate90(original, Rotation._180);
        else if (angle == 270.0 || angle == -90.0) return SpriteUtil.rotate90(original, Rotation._270);

        final double radians = Math.toRadians(angle);
        final double cos = Math.cos(radians);
        final double sin = Math.sin(radians);

        int newWidth = (int) (Math.abs(cos * original.getWidth()) + Math.abs(sin * original.getHeight()));
        int newHeight = (int) (Math.abs(cos * original.getHeight()) + Math.abs(sin * original.getWidth()));

        int xDelta = (newWidth - original.getWidth()) / 2;
        int yDelta = (newHeight - original.getHeight()) / 2;


        final int[] pixels2 = new int[newWidth * newHeight];

        int centerX = original.getWidth() / 2;
        int centerY = original.getHeight() / 2;

        for (int x = -xDelta; x < newWidth - xDelta; x++)
            for (int y = -yDelta; y < newHeight - yDelta; y++) {
                int m = x - centerX;
                int n = y - centerY;
                int j = (int) (m * cos + n * sin + centerX);
                int k = (int) (n * cos - m * sin + centerY);

                if (j >= 0 && j < original.getWidth() && k >= 0 && k < original.getHeight()) {
                    pixels2[(y + yDelta) * newWidth + x + xDelta] = original.getPixels()[k * original.getWidth() + j];
                }
            }
        return new Sprite(newWidth, newHeight, pixels2);
    }

    /**
     * Rotates an image by multiples of 90 degrees
     * This is a much faster version of
     * {@link SpriteUtil#rotate(Sprite, double) SpriteUtil.rotate(Sprite, double)}
     *
     * @param original The sprite to rotate
     * @param angle    The rotation angle
     * @return The rotated sprite
     */
    public static Sprite rotate90(Sprite original, Rotation angle) {
        int newWidth = angle == Rotation._180 ? original.getWidth() : original.getHeight();
        int newHeight = angle == Rotation._180 ? original.getHeight() : original.getWidth();
        int[] newPix = new int[original.getPixels().length];

        for (int x = 0; x < original.getWidth(); x++) {
            for (int y = 0; y < original.getHeight(); y++) {
                int newY = angle == Rotation._90 ? x : angle == Rotation._180 ? original.getHeight() - y - 1 : original.getWidth() - x - 1;
                int newX = angle == Rotation._90 ? original.getHeight() - y - 1 : angle == Rotation._180 ? original.getWidth() - x - 1 : y;

                newPix[newX + newY * newWidth] = original.getPixels()[x + y * original.getWidth()];
            }
        }
        return new Sprite(newWidth, newHeight, newPix);
    }

    /**
     * Adds a border to a Sprite (the border is added inside, so part of the sprite will be covered)
     *
     * @param original The sprite to which the border will be added
     * @param color    The color of the border (in ARGB format: 0xAARRGGBB)
     * @param size     The size of the border, in pixels
     * @return The sprite with the border
     */
    @API
    public static Sprite addBorder(Sprite original, int color, int size) {
        int[] newPix = Arrays.copyOf(original.getPixels(), original.getPixels().length);
        for (int x = 0; x < original.getWidth(); x++) {
            for (int y = 0; y < original.getHeight(); y++) {
                if (x < size || x >= original.getWidth() - size || y < size || y >= original.getHeight() - size) {
                    newPix[x + y * original.getWidth()] = color;
                }
            }
        }
        return new Sprite(original.getWidth(), original.getHeight(), newPix);
    }

    @API
    public static Sprite addBeveledBorder(Sprite original, int baseColor, int size, boolean inverted) {
        int[] newPix = Arrays.copyOf(original.getPixels(), original.getPixels().length);
        final int width = original.getWidth();
        final int height = original.getHeight();

        int dark = ARGBColorUtil.darken(ARGBColorUtil.darken(ARGBColorUtil.darken(ARGBColorUtil.darken(baseColor))));
        int bright = ARGBColorUtil.brighten(ARGBColorUtil.brighten(ARGBColorUtil.brighten(ARGBColorUtil.brighten(baseColor))));

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (y < size && x >= y && width - x - 1 > y) {
                    newPix[x + y * width] = !inverted ? bright : dark;
                } else if (y >= height - size && height - y - 1 < x && width - x - 1 >= height - y - 1) {
                    newPix[x + y * width] = !inverted ? dark : bright;
                } else if (x < size && x < y && height - y - 1 >= x) {
                    newPix[x + y * width] = !inverted ? baseColor : dark;
                } else if (x >= width - size && width - x - 1 <= y && width - x - 1 < height - y - 1) {
                    newPix[x + y * width] = !inverted ? dark : baseColor;
                }
            }
        }
        return new Sprite(width, original.getHeight(), newPix);
    }

    public static Sprite increaseSize(Sprite original, int newWidth, int newHeight, int newX, int newY) {
        if (newWidth < original.getWidth() || newHeight < original.getHeight()) {
            throw new IllegalArgumentException("New sizes must be bigger than or equal to the original sizes!");
        }
        if (original.getWidth() + newX > newWidth || original.getHeight() + newY > newHeight) {
            throw new IllegalArgumentException("Sprite cannot be drawn outside the given new size!");
        }

        int[] pix = new int[newWidth * newHeight];
        for (int y = 0; y < original.getHeight(); y++) {
            System.arraycopy(original.getPixels(), y * original.getWidth(), pix, newX + (y + newY) * newWidth, original.getWidth());
        }
        return new Sprite(newWidth, newHeight, pix);
    }

    @API
    public static Sprite increaseSizeCentered(Sprite original, int newWidth, int newHeight) {
        return increaseSize(original, newWidth, newHeight, (newWidth - original.getWidth()) / 2, (newHeight - original.getHeight()) / 2);
    }

    @API
    public static Sprite replaceColor(Sprite s, int original, int toReplace) {
        int[] newPix = new int[s.getWidth() * s.getHeight()];
        for (int i = 0; i < newPix.length; i++) {
            newPix[i] = s.getPixels()[i] == original ? toReplace : s.getPixels()[i];
        }
        return new Sprite(s.getWidth(), s.getHeight(), newPix);
    }

    @API
    public static Sprite replaceColorIgnoreAlpha(Sprite s, int original, int toReplace) {
        int[] newPix = new int[s.getWidth() * s.getHeight()];
        for (int i = 0; i < newPix.length; i++) {
            newPix[i] = (s.getPixels()[i] & 0x00FFFFFF) == (original & 0x00FFFFFF) ? ((toReplace & 0x00FFFFFF) | (s.getPixels()[i] & 0xFF000000)) : s.getPixels()[i];
        }
        return new Sprite(s.getWidth(), s.getHeight(), newPix);
    }

    @API
    public static Sprite getSpriteFromClasspath(String path) {
        try {
            return new Sprite(ImageIO.read(SpriteUtil.class.getClassLoader().getResourceAsStream(path)));
        } catch (IOException e) {
            return null;
        }
    }

    @API
    public static Sprite compose(Sprite top, Sprite bot, boolean blend) {
        final int width = top.getWidth();
        final int height = top.getHeight();
        if (width != bot.getWidth() || height != bot.getHeight())
            throw new IllegalArgumentException("Sprites sizes must be equal!");

        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (blend)
                    pixels[x + y * width] = ARGBColorUtil.composite(top.getPixels()[x + y * width], bot.getPixels()[x + y * width]);
                else {
                    Sprite s = ARGBColorUtil.getAlpha(top.getPixels()[x + y * width]) != 0 ? top : bot;
                    pixels[x + y * width] = s.getPixels()[x + y * width];
                }
            }
        }
        return new Sprite(width, height, pixels);
    }

    /**
     * Multiples of 90 for exact rotations
     */
    public enum Rotation {
        _90, _180, _270
    }

    /**
     * Available scaling methods (<a href="http://en.wikipedia.org/wiki/Image_scaling#Scaling_methods">Wikipedia page</a>)
     */
    public enum ScalingMethod {
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
