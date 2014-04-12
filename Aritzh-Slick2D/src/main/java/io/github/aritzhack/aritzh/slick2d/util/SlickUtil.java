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

package io.github.aritzhack.aritzh.slick2d.util;

import io.github.aritzhack.aritzh.slick2d.Game;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.LogSystem;

/**
 * @author Aritz Lopez
 */
public class SlickUtil {

    /**
     * Gets the top-left corner of a rectangle
     *
     * @param rectangle The rectangle of which the top-left corner will be returned
     * @return The top-left corner of the rectangle
     */
    public static Point getPos(Rectangle rectangle) {
        return new Point(rectangle.getX(), rectangle.getY());
    }

    /**
     * Copies a rectangle.
     *
     * @param r The rectangle to copy.
     * @return A copy of the rectangle.
     */
    public static Rectangle copyOf(Rectangle r) {
        return new Rectangle(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    /**
     * Copies a point.
     *
     * @param p The point to copy.
     * @return A copy of the point.
     */
    public static Point copyOf(Point p) {
        return new Point(p.getX(), p.getY());
    }

    /**
     * Translate a rectangle by {@code dx} in the horizontal axis and by {@code dy} in the vertical axis.
     *
     * @param r  The rectangle to translate.
     * @param dx The amount of pixels to move in the horizontal axis (can be negative).
     * @param dy The amount of pixels to move in the vertical axis (can be negative).
     * @return The translated rectangle, for convenience.
     */
    public static Rectangle translate(Rectangle r, float dx, float dy) {
        r.setX(r.getX() + dx);
        r.setY(r.getY() + dy);
        return r;
    }

    /**
     * Translate a point by {@code dx} in the horizontal axis and by {@code dy} in the vertical axis.
     *
     * @param p  The point to translate.
     * @param dx The amount of pixels to move in the horizontal axis (can be negative).
     * @param dy The amount of pixels to move in the vertical axis (can be negative).
     * @return The translated point, for convenience.
     */
    public static Point translate(Point p, float dx, float dy) {
        p.setX(p.getX() + dx);
        p.setY(p.getY() + dy);
        return p;
    }

    /**
     * Checks whether the rectangle is within the boundaries of the rectangle. If edges coincide,
     * it is considered as true (if the same rectangle is checked against itself it will return true)
     *
     * @param out The outer rectangle, that has to contain the inner one
     * @param in The inner rectangle that must the outer must contain
     * @return Whether the outer rectangle contains the inner one
     */
    public static boolean includesOrContains(Rectangle out, Rectangle in) {
        return (in.getMinX() >= out.getMinX()
                && in.getMinY() >= out.getMinY()
                && in.getMaxX() <= out.getMaxX()
                && in.getMaxY() <= out.getMaxY());
    }

    /**
     * Formats a rectangle as a string, with this format: {@code {xCoord, yCoord, width x height}}
     * @param r The rectangle to format
     * @return A string representing the rectangle
     */
    public static String toString(Rectangle r) {
        return String.format("{%s, %s, %s x %s}", r.getX(), r.getY(), r.getWidth(), r.getWidth());
    }

    /**
     * Formats a point as a string, with this format: {@code {xCoord, yCoord}}
     * @param p The point to format
     * @return A string representing the point
     */
    public static String toString(Point p) {
        return String.format("{%s, %s}", p.getX(), p.getY());
    }

    public static Image newColorImage(int width, int height, Color color) {
        try {
            Image img = new Image(width, height);
            final Graphics g = img.getGraphics();
            g.setColor(color);
            g.fillRect(0, 0, width, height);
            g.flush();
            return img;
        } catch (SlickException e) {
            Game.LOG.e("Error creating image", e);
        }
        return null;
    }

    public static final LogSystem nullSystem = new LogSystem() {
        @Override
        public void error(String message, Throwable e) {
            Game.LOG.e(message, e);
        }

        @Override
        public void error(Throwable e) {
            Game.LOG.e("Slick2D Error: ", e);
        }

        @Override
        public void error(String message) {
            Game.LOG.e(message);
        }

        @Override
        public void warn(String message) {
        }

        @Override
        public void warn(String message, Throwable e) {
        }

        @Override
        public void info(String message) {
        }

        @Override
        public void debug(String message) {
        }
    };
}
