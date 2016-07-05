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

package io.github.cubedtear.jcubit.awt.gameEngine.core;

import io.github.cubedtear.jcubit.util.API;

/**
 * @author Aritz Lopez
 */
public interface IGameEngine extends Runnable {

	/**
	 * Called when the game should start.
	 */
	void start();

	/**
	 * Called when the game should stop
	 */
	void stop();

	/**
	 * Called each time the game should be updated.
	 */
	void update();

	/**
	 * Called each time the game should be rendered.
	 */
	void render();

	/**
	 * Called once per second.
	 */
	void updatePS();

	/**
	 * @return The game of this engine.
     */
	IGame getGame();

	/**
	 * @return The frames per second. Will probably change between calls.
     */
	@API
	int getFPS();

	/**
	 * @return The updated per second.  Will probably change between calls.
     */
	@API
	int getUPS();
}
