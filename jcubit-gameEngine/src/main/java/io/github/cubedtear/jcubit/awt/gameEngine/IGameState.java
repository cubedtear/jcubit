package io.github.cubedtear.jcubit.awt.gameEngine;

import io.github.cubedtear.jcubit.awt.gameEngine.events.Event;
import io.github.cubedtear.jcubit.awt.render.IRender;

/**
 * @author Aritz Lopez
 */
public interface IGameState {

    void update(StateGame game);

    void onEvent(Event event, StateGame game);

    void draw(IRender render);
}
