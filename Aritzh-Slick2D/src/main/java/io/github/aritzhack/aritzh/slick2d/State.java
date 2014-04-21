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

package io.github.aritzhack.aritzh.slick2d;

import io.github.aritzhack.aritzh.slick2d.gui.GUI;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Slightly improved version of {@link org.newdawn.slick.state.GameState}, that directly extends
 * {@link io.github.aritzhack.aritzh.slick2d.gui.GUI} and has getters for the
 * {@link AGame game} and the {@link org.newdawn.slick.GameContainer container}
 *
 * @author Aritz Lopez
 */
@SuppressWarnings("unchecked")
public abstract class State<T extends AGame> extends GUI implements GameState {
    protected T game;

    /**
     * Create a state for the specified game
     *
     * @param game The game that will hold this state
     */
    public State(T game) {
        super(game.getGC().getWidth(), game.getGC().getHeight());
        this.game = game;
    }

    /**
     * Returns the game that holds this state
     *
     * @return the game that holds this state
     */
    public T getGame() {
        return game;
    }

    /**
     * Returns the GameContainer that holds the Game that holds this state
     *
     * @return the GameContainer that holds the Game that holds this state
     * @see AGame#getGC()
     */
    public GameContainer getGC() {
        return this.game.getGC();
    }

    @Override
    public final void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.init(container, (T) game);
    }

    @Override
    public final void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        super.render(g);
        this.render(container, (T) game, g);
    }

    @Override
    public final void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        this.update(container, (T) game, delta);
    }

    /**
     * Called when the state should be initialized
     *
     * @param container The GameContainer
     * @param game      The game that holds this state
     * @throws SlickException
     */
    public void init(GameContainer container, T game) throws SlickException {

    }

    /**
     * Called on every update cycle
     *
     * @param container The GameContainer
     * @param game      The game that holds this state
     * @param delta     The amount of time that has passed in millisecond since last update
     * @throws SlickException
     */
    public void update(GameContainer container, T game, int delta) throws SlickException {

    }

    /**
     * Called on every render cycle, so that the state can be rendered
     *
     * @param gc   The GameContainer
     * @param game The game that holds this state
     * @param g    The Graphics environment into which this state should be drawn
     * @throws SlickException
     */
    public abstract void render(GameContainer gc, T game, Graphics g) throws SlickException;

    // region ...Unused final implemented methods...

    @Override
    public final void enter(GameContainer container, StateBasedGame game) throws SlickException {

    }

    @Override
    public final void leave(GameContainer container, StateBasedGame game) throws SlickException {

    }

    // endregion
}
