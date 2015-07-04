package io.github.aritzhack.aritzh.awt.gameEngine.test;

import io.github.aritzhack.aritzh.gameEngine.IGame;

import java.awt.*;

/**
 * Abstract empty implementation of IGame for use in {@link io.github.aritzhack.aritzh.awt.gameEngine.test.TestEngine}
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

    public void onPostRender(Graphics g) {

    }
}
