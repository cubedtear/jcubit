package io.github.cubedtear.jcubit.awt.gameEngine.events;

import io.github.cubedtear.jcubit.math.Vec2i;

/**
 * @author Aritz Lopez
 */
public class MouseReleasedEvent extends MouseEvent {

    public MouseReleasedEvent(Vec2i pos, MouseButton button) {
        super(pos, button);
    }

    @Override
    public MouseEvent move(Vec2i delta) {
        return new MouseReleasedEvent(pos.add(delta), button);
    }

}
