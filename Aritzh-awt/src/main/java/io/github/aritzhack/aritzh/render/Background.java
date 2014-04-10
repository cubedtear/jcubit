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

/**
 * @author Aritz Lopez
 */
public class Background {

    protected final Sprite sprite;
    protected final boolean alignToBottom;
    protected final int speed;
    protected int xPos = 0;
    protected long delta = 0;

    public Background(Sprite sprite, boolean alignToBottom, int speed) {
        this.sprite = sprite;
        this.alignToBottom = alignToBottom;
        this.speed = speed;
    }

    public void render(IRender render, long deltaNS) {
        final int yPos = alignToBottom ? render.getHeight() - this.sprite.getHeight() : 0;

        delta += deltaNS;

        final int period = 100000000 / speed;

        int xPos = this.xPos;

        if (delta > period) {
            delta -= period;
            xPos--;
        }

        this.xPos = xPos;

        render.draw(xPos, yPos, sprite);

        while (xPos + sprite.getWidth() < render.getWidth()) {
            xPos += sprite.getWidth();
            render.draw(xPos, yPos, sprite);
        }
    }
}
