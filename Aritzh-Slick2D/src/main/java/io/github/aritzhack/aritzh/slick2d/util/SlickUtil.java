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

    public static Point getPos(Rectangle rectangle) {
        return new Point(rectangle.getX(), rectangle.getY());
    }

    public static Rectangle copyOf(Rectangle r) {
        return new Rectangle(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public static Point copyOf(Point p) {
        return new Point(p.getX(), p.getY());
    }

    public static Rectangle translate(Rectangle r, float dx, float dy) {
        r.setX(r.getX() + dx);
        r.setY(r.getY() + dy);
        return r;
    }

    public static Point translate(Point p, float dx, float dy) {
        p.setX(p.getX() + dx);
        p.setY(p.getY() + dy);
        return p;
    }

    public static boolean includesOrContains(Rectangle out, Rectangle in) {
        return (in.getMinX() >= out.getMinX()
                && in.getMinY() >= out.getMinY()
                && in.getMaxX() <= out.getMaxX()
                && in.getMaxY() <= out.getMaxY());
    }

    public static String toString(Rectangle r) {
        return String.format("{%s, %s, %s x %s}", r.getX(), r.getY(), r.getWidth(), r.getWidth());
    }

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
