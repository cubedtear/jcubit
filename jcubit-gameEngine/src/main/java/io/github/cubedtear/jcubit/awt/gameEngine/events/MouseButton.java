package io.github.cubedtear.jcubit.awt.gameEngine.events;

import java.awt.event.MouseEvent;

/**
 * @author Aritz Lopez
 */
public enum MouseButton {
    LEFT, MIDDLE, RIGHT;

    public static MouseButton fromAwt(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                return LEFT;
            case MouseEvent.BUTTON2:
                return MIDDLE;
            case MouseEvent.BUTTON3:
                return RIGHT;
            default:
                if ((e.getModifiers() & (MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON1_MASK)) != 0) return LEFT;
                else if ((e.getModifiers() & (MouseEvent.BUTTON2_DOWN_MASK | MouseEvent.BUTTON2_MASK)) != 0)
                    return MIDDLE;
                else if ((e.getModifiers() & (MouseEvent.BUTTON3_DOWN_MASK | MouseEvent.BUTTON3_MASK)) != 0)
                    return RIGHT;
                else return null;
        }
    }
}