package io.github.cubedtear.jcubit.awt.gameEngine.events;

/**
 * @author Aritz Lopez
 */
public abstract class KeyboardEvent extends Event {

    protected final int keyCode;

    public KeyboardEvent(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
