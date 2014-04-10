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
