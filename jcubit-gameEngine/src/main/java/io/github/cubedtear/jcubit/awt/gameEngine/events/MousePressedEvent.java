package io.github.cubedtear.jcubit.awt.gameEngine.events;

import io.github.cubedtear.jcubit.math.Vec2i;

/**
 * @author Aritz Lopez
 */
public class MousePressedEvent extends MouseEvent {

    public MousePressedEvent(Vec2i pos, MouseButton button) {
        super(pos, button);
    }
}
