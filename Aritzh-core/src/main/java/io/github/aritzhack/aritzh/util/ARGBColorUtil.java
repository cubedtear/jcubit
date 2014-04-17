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

package io.github.aritzhack.aritzh.util;

/**
 * Utility class to deal with ARGB color format
 *
 * @author Aritz Lopez
 */
@SuppressWarnings("UnusedDeclaration")
public class ARGBColorUtil {

    /**
     * The mask to get the two least significant bytes of an integer;
     */
    public static final int MASK = 0xFF;
    /**
     * The shifting used to put the alpha component in position
     */
    public static final int ALPHA_SHIFT = 24;
    /**
     * The shifting used to put the red component in position
     */
    public static final int RED_SHIFT = 16;
    /**
     * The shifting used to put the green component in position
     */
    public static final int GREEN_SHIFT = 8;
    /**
     * The shifting used to put the blue component in position
     */
    public static final int BLUE_SHIFT = 0;

    /**
     * Composes two colors with the ARGB format: <br>
     * The format of the color integer is as follows: 0xAARRGGBB
     * Where:
     * <ol>
     * <li>AA is the alpha component (0-255)</li>
     * <li>RR is the red component (0-255)</li>
     * <li>GG is the green component (0-255)</li>
     * <li>BB is the blue component (0-255)</li>
     * </ol>
     * NOTE: The source of this method is quite obscure, but it's done this way because it's performance-critical (this method could be run thousands of times per second!)<br>
     * The code (unobscured) does this: <br><br>
     * <code>
     * double alpha1 = getAlpha(foreground) / 256.0;<br>
     * double alpha2 = getAlpha(background) / 256.0;<br>
     * <br>
     * if (alpha1 == 1.0 || alpha2 == 0) return foreground;<br>
     * else if (alpha1 == 0) return background;<br>
     * <br>
     * int red1 = getRed(foreground);<br>
     * int red2 = getRed(background);<br>
     * int green1 = getGreen(foreground);<br>
     * int green2 = getGreen(background);<br>
     * int blue1 = getBlue(foreground);<br>
     * int blue2 = getBlue(background);<br>
     * <br>
     * double doubleAlpha = (alpha1 + alpha2 * (1 - alpha1));<br>
     * int finalAlpha = (int) (doubleAlpha * 256);<br>
     * <br>
     * double cAlpha2 = alpha2 * (1 - alpha1) * 0.5;<br>
     * <br>
     * int finalRed = (int) (red1 * alpha1 + red2 * cAlpha2);<br>
     * int finalGreen = (int) (green1 * alpha1 + green2 * cAlpha2);<br>
     * int finalBlue = (int) (blue1 * alpha1 + blue2 * cAlpha2);<br>
     * return getColor(finalAlpha, finalRed, finalGreen, finalBlue);<br><br>
     * </code>
     *
     * @param foreground The foreground color (above)
     * @param background The background color (below)
     * @return A composition of both colors, in ARGB format
     */
    public static int composite(final int foreground, final int background) {

        double alpha1 = (foreground >>> ALPHA_SHIFT) / 256.0;
        double alpha2 = (background >>> ALPHA_SHIFT) / 256.0;

        if (alpha2 == 0) return foreground;
        else if (alpha1 == 0) return background;

        double cAlpha2 = alpha2 * (1 - alpha1) * 0.5;
        return ((int) ((alpha1 + alpha2 * (1 - alpha1)) * 256) << ALPHA_SHIFT) | ((int) (((foreground >> RED_SHIFT) & MASK) * alpha1 + ((background >> RED_SHIFT) & MASK) * cAlpha2) << RED_SHIFT) | ((int) (((foreground >> GREEN_SHIFT) & MASK) * alpha1 + ((background >> GREEN_SHIFT) & MASK) * cAlpha2) << GREEN_SHIFT) | (int) ((foreground & MASK) * alpha1 + (background & MASK) * cAlpha2);
    }

    /**
     * Gets a color composed of the specified channels. All values must be between 0 and 255, both inclusive
     *
     * @param alpha The alpha channel [0-255]
     * @param red   The red channel [0-255]
     * @param green The green channel [0-255]
     * @param blue  The blue channel [0-255]
     * @return The color composed of the specified channels
     */
    public static int getColor(final int alpha, final int red, final int green, final int blue) {
        return (alpha << ALPHA_SHIFT) | (red << RED_SHIFT) | (green << GREEN_SHIFT) | blue;
    }

    /**
     * Returns the red channel of the color
     *
     * @param color The color to get the red channel of
     * @return The red channel of the color
     */
    public static int getRed(final int color) {
        return (color >> RED_SHIFT) & MASK;
    }

    /**
     * Returns the green channel of the color
     *
     * @param color The color to get the green channel of
     * @return The green channel of the color
     */
    public static int getGreen(final int color) {
        return (color >> GREEN_SHIFT) & MASK;
    }

    /**
     * Returns the blue channel of the color
     *
     * @param color The color to get the blue channel of
     * @return The blue channel of the color
     */
    public static int getBlue(final int color) {
        return (color & MASK);
    }

    /**
     * Returns the alpha channel of the color
     *
     * @param color The color to get the alpha channel of
     * @return The alpha channel of the color
     */
    public static int getAlpha(final int color) {
        return (color >> ALPHA_SHIFT) & MASK;
    }

}
