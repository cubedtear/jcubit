package io.github.cubedtear.jcubit.awt.gameEngine.events;

import io.github.cubedtear.jcubit.math.Vec2i;

/**
 * @author Aritz Lopez
 */
public abstract class MouseEvent extends Event {

    protected final Vec2i pos;
    protected final MouseButton button;

    public MouseEvent(Vec2i pos, MouseButton button) {
        this.pos = pos;
        this.button = button;
    }

    public Vec2i getPos() {
        return pos;
    }

    public MouseButton getButton() {
        return button;
    }

    public abstract MouseEvent move(Vec2i delta);
}
