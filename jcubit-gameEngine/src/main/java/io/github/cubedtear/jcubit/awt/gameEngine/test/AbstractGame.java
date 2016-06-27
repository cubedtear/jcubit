package io.github.cubedtear.jcubit.awt.gameEngine.test;

import io.github.cubedtear.jcubit.awt.gameEngine.core.IGame;

/**
 * Abstract empty implementation of IGame for use in {@link TestEngine}
 *
 * @author Aritz Lopez
 */
public abstract class AbstractGame implements IGame {

	@Override
	public void onStart() {
	}

	@Override
	public void onStop() {
	}

	@Override
	public void onRender() {
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public abstract String getGameName();

	@Override
	public void onUpdatePS() {
	}

	public void onPostRender() {
	}
}
