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

import io.github.aritzhack.aritzh.logging.core.ILogger;
import io.github.aritzhack.aritzh.logging.SLF4JLogger;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Slightly improved version of {@link org.newdawn.slick.state.StateBasedGame}, which has some default fonts,
 * a {@link io.github.aritzhack.aritzh.logging.core.ILogger logger} and a getter for the
 * {@link org.newdawn.slick.GameContainer container}
 *
 * @author Aritz Lopez
 */
public abstract class AGame extends StateBasedGame {

    public static final ILogger LOG = new SLF4JLogger("Game");
    public static Font FNT_COMIC_SANS_18;
    public static Font FNT_COMIC_SANS_24;
    private GameContainer container;

    /**
     * Create a new state based game
     */
    public AGame(String name) {
        super(name);
    }

    @Override
    public final void initStatesList(GameContainer container) throws SlickException {
        this.container = container;
        FNT_COMIC_SANS_18 = new TrueTypeFont(new java.awt.Font("Comic Sans MS", java.awt.Font.PLAIN, 18), true);
        FNT_COMIC_SANS_24 = new TrueTypeFont(new java.awt.Font("Comic Sans MS", java.awt.Font.BOLD, 24), true);
        this.initStates();
    }

    /**
     * Here should be initialized all the {@link io.github.aritzhack.aritzh.slick2d.State states}
     *
     * @throws SlickException
     */
    public abstract void initStates() throws SlickException;

    /**
     * <p>Returns the GameContainer</p>
     * <p>WARNING: The field is initialized when the game is started, so this will be null before {@link AGame#initStates()} is called</p>
     *
     * @return the GameContainer
     */
    public GameContainer getGC() {
        return this.container;
    }
}
