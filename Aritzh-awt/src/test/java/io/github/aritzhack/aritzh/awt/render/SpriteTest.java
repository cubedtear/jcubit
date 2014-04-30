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

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Aritz Lopez
 */
public class SpriteTest {

    public static final int COLOR = 0xFF00FFFF;

    @Test
    public void testColorSprite() {
        Sprite sprite = new Sprite(500, 500, COLOR);
        assert Arrays.asList(ArrayUtils.toObject(sprite.getPixels()))
            .stream()
            .allMatch(i -> i == COLOR) :
            "One of the colors was not equal!";
    }
}
