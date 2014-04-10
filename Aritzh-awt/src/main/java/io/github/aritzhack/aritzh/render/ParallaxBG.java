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

import com.google.common.base.Preconditions;

/**
 * @author Aritz Lopez
 */
public class ParallaxBG {
    private final Background[] backgrounds;

    public ParallaxBG(Background... backgrounds) {
        Preconditions.checkArgument(backgrounds != null, "Background list cannot be null!");
        Preconditions.checkArgument(backgrounds.length != 0, "Background list cannot be empty!");

        this.backgrounds = backgrounds;
    }

    public void render(IRender render, long deltaNS) {
        for (Background b : this.backgrounds) {
            b.render(render, deltaNS);
        }
    }
}
