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

package io.github.cubedtear.jcubit.timing;

import com.google.common.collect.Lists;
import io.github.cubedtear.jcubit.collections.CollectionUtil;

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
