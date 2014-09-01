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
