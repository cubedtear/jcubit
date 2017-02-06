package io.github.cubedtear.jcubit.awt.gameEngine.events;

/**
 * @author Aritz Lopez
 */
public class WindowCloseEvent extends CloseEvent {

    public static final WindowCloseEvent INSTANCE = new WindowCloseEvent();

    private WindowCloseEvent() {
    }
}
