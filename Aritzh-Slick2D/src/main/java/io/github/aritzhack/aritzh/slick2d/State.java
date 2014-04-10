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
 * @author Aritz Lopez
 */
public abstract class State extends GUI implements GameState {
    protected Game game;

    public State(Game game) {
        super(game.getGC().getWidth(), game.getGC().getHeight());
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public GameContainer getGC() {
        return this.game.getGC();
    }

    @Override
    public final void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.init(container, (Game) game);
    }

    @Override
    public final void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        this.render(container, (Game) game, g);
    }

    @Override
    public final void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        this.update(container, (Game) game, delta);
    }

    public void init(GameContainer container, Game game) throws SlickException {

    }

    public void update(GameContainer container, Game game, int delta) throws SlickException {

    }

    public abstract void render(GameContainer gc, Game game, Graphics g) throws SlickException;

    // region ...Unused final implemented methods...

    @Override
    public final void enter(GameContainer container, StateBasedGame game) throws SlickException {

    }

    @Override
    public final void leave(GameContainer container, StateBasedGame game) throws SlickException {

    }

    public void setGame(Game game) {
        this.game = game;
    }
}
