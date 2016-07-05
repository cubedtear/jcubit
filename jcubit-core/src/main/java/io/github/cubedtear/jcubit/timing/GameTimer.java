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
import io.github.cubedtear.jcubit.util.API;

import java.util.List;

/**
 * Timer to be used in games' logic.
 * Useful to calculate the updates per second (UPS) and frames per second (FPS).
 * Both numbers are calculated each second.
 * This class will only work if the {@link GameTimer#update()} method is called at least once per second,
 * as it uses that call to calculate the UPS and FPS.
 * @author Aritz Lopez
 */
@API
public class GameTimer {

	private int fps, ups, cfps, cups;
	private final List<Integer> fpss = Lists.newArrayList();
	private final List<Integer> upss = Lists.newArrayList();
	private long lastMillis;
	private int secondsPast = 0;

	/**
	 * Creates a new Game Timer. This automatically starts the timer.
	 */
	public GameTimer() {
		this.init();
	}

	/**
	 * Re-initializes this timer.
	 */
	public void init() {
		this.ups = this.fps = 0;
		this.lastMillis = System.currentTimeMillis();
		this.upss.clear();
		this.fpss.clear();
	}

	/**
	 * This should be called each game update.
	 */
	@API
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
		if (++secondsPast >= 5) {
			this.fpss.clear();
			this.upss.clear();
		}
	}

	/**
	 * This should be called each rendering.
	 */
	public void render() {
		this.cfps++;
	}

	/**
	 * Returns the number of times that the {@link GameTimer#render()} method was called the past second.
	 * @return the number of frames per second.
     */
	@API
	public int getFPS() {
		return fps;
	}

	/**
	 * Returns the number of times that the {@link GameTimer#update()}} method was called the past second.
	 * @return the number of updates per second.
	 */
	@API
	public int getUPS() {
		return ups;
	}

	/**
	 * Returns the average FPS in 5 seconds.
	 * @return the average FPS in 5 seconds.
     */
	@API
	public int getAverageFPS() {
		return this.fpss.size() > 0 ? CollectionUtil.integerSum(this.fpss) / this.fpss.size() : this.fps;
	}

	/**
	 * Returns the average UPS in 5 seconds.
	 * @return the average UPS in 5 seconds.
	 */
	@API
	public int getAverageUPS() {
		return this.upss.size() > 0 ? CollectionUtil.integerSum(this.upss) / this.upss.size() : this.ups;
	}
}
