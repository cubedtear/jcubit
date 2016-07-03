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

/**
 * Interface to implement the basic characteristics of a game.
 * @author Aritz Lopez
 */
public interface IGame {

	/**
	 * Called when the game is starting.
	 */
	void onStart();

	/**
	 * Called when the game is being stopped.
	 */
	void onStop();

	/**
	 * Called each time the game should be rendered in the screen.
	 */
	void onRender();

	/**
	 * Called each time the game should be updated. Usually a consistent number of times per second.
	 */
	void onUpdate();

	/**
	 * @return The name of the game.
     */
	String getGameName();

	/**
	 * Called each second.
	 */
	void onUpdatePS();

	/**
	 * Called after normal rendering. Usefult to add UI stuff.
	 */
	void onPostRender();
}
