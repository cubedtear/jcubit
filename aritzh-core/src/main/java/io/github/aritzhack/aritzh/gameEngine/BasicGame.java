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

package io.github.aritzhack.aritzh.gameEngine;

import com.google.common.base.Preconditions;

/**
 * @author Aritz Lopez
 */
public class BasicGame implements IGameEngine {

	protected final IGame gameHandler;
	private final boolean lockFps;
	protected boolean running = false;
	protected int fps, ups;
	private int targetUps;

	public BasicGame(IGame game) {
		this(game, 60);
	}

	public BasicGame(IGame game, int targetUps) {
		this(game, targetUps, false);
	}

	public BasicGame(IGame game, int targetUps, boolean lockFps) {
		Preconditions.checkArgument(game != null, "Game cannot be null!");
		this.gameHandler = game;
		this.lockFps = lockFps;
		this.targetUps = Math.max(targetUps, -1);
	}

	public BasicGame(IGame game, boolean lockFps) {
		this(game, 60, lockFps);
	}

	@Override
	public void run() {

		double delta = 0;

		long lastNano = System.nanoTime();
		long lastMillis = System.currentTimeMillis();

		this.getGame().onStart();

		this.running = true;

		while (this.running) {

			double NSPerTick = 1000000000.0 / this.targetUps;

			long now = System.nanoTime();
			delta += (now - lastNano) / NSPerTick;
			lastNano = now;

			if (delta >= 1 || this.targetUps < 0) {
				this.update();
				this.ups++;
				delta--;
				if (lockFps) {
					this.render();
					this.fps++;
				}
			}

			if (!lockFps) {
				this.render();
				this.fps++;
			}

			if (System.currentTimeMillis() - lastMillis >= 1000) {
				lastMillis += 1000;
				this.updatePS();
				this.fps = this.ups = 0;
			}
		}
	}

	public int getTargetUps() {
		return targetUps;
	}

	public void setTargetUps(int targetUps) {
		this.targetUps = targetUps;
	}

	@Override
	public void start() {
		this.getGame().onStart();
	}

	@Override
	public void stop() {
		this.getGame().onStop();
	}

	@Override
	public void update() {
		this.getGame().onUpdate();
	}

	@Override
	public void render() {
		this.getGame().onRender();
	}

	@Override
	public void updatePS() {
		this.getGame().onUpdatePS();
	}

	@Override
	public IGame getGame() {
		return this.gameHandler;
	}

	@Override
	public int getFPS() {
		return fps;
	}

	@Override
	public int getUPS() {
		return ups;
	}
}
