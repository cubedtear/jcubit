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

import java.awt.image.BufferedImage;

/**
 * @author Aritz Lopez
 */
public interface IRender {

    void clear();

    void draw(int x, int y, int width, int height, int[] colors);

    void draw(int x, int y, Sprite sprite);

    void draw(int x, int y, String spriteName);

    void draw(int x, int y, long deltaNS, AnimatedSprite sprite);

    BufferedImage getImage();

    int getWidth();

    int getHeight();
}
