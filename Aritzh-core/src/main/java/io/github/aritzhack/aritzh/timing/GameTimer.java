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

package io.github.aritzhack.aritzh.timing;

import com.google.common.collect.Lists;
import io.github.aritzhack.aritzh.collections.CollectionUtil;

import java.util.List;

/**
 * @author Aritz Lopez
 */
public class GameTimer {

    private int fps, ups, cfps, cups;
    private List<Integer> fpss = Lists.newArrayList();
    private List<Integer> upss = Lists.newArrayList();
    private long lastMillis;

    public GameTimer() {
        this.init();
    }

    public void init() {
        this.ups = this.fps = 0;
        this.lastMillis = System.currentTimeMillis();
    }

    public void update() {
        this.cups++;

        if (System.currentTimeMillis() - lastMillis >= 1000) {
            this.updatePS();
        }
    }

    private void updatePS() {
        this.fps = this.cfps;
        this.ups = this.cups;
        this.fpss.add(this.fps);
        this.upss.add(this.ups);
        this.cups = this.cfps = 0;
        this.lastMillis += 1000;
    }

    public void render() {
        this.cfps++;
    }

    public int getFPS() {
        return fps;
    }

    public int getUPS() {
        return ups;
    }

    public int getAverageFPS() {
        return this.fpss.size() > 0 ? CollectionUtil.integerSum(this.fpss) / this.fpss.size() : 0;
    }

    public int getAverageUPS() {
        return this.upss.size() > 0 ? CollectionUtil.integerSum(this.upss) / this.upss.size() : 0;
    }
}
