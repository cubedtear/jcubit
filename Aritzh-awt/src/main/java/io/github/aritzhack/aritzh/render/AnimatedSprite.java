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

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * @author Aritz Lopez
 */
public class AnimatedSprite {

    private final long delay;
    private final int spriteAmount;
    List<Sprite> sprites = Lists.newArrayList();
    private long currentDelta = 0;
    private int currentSprite = 0;

    public AnimatedSprite(Map<String, Sprite> sprites, String name, int frameCount, long delayInMillis) {
        this.delay = delayInMillis * 1_000_000;

        for (int i = 0; i < frameCount; i++) {
            Sprite s = sprites.get(name + i);
            if (s == null) break;
            this.sprites.add(s);
        }
        this.spriteAmount = this.sprites.size();
    }

    public Sprite getCurrentFrame(long delta) {
        this.currentDelta += delta;
        if (this.currentDelta > this.delay) {
            this.currentDelta -= this.delay;
            this.currentSprite++;
            if (this.currentSprite >= this.spriteAmount) this.currentSprite = 0;
        }
        return this.sprites.get(currentSprite);
    }
}
