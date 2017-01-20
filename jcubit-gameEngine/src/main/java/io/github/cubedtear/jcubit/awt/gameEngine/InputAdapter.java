package io.github.cubedtear.jcubit.awt.gameEngine;

import io.github.cubedtear.jcubit.awt.gameEngine.events.*;
import io.github.cubedtear.jcubit.math.Vec2i;

import java.awt.event.FocusEvent;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author Aritz Lopez
 */
public class InputAdapter implements KeyListener, FocusListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private final boolean[] keys = new boolean[KeyEvent.KEY_LAST + 1];
    private final StateGame game;
    private Vec2i mousePos = new Vec2i();

    public InputAdapter(StateGame game) {
        this.game = game;
    }

    public Vec2i getMousePos() {
        return mousePos;
    }

    public boolean isKeyDown(int keycode) {
        return this.keys[keycode];
    }

    // region ...Key listener methods...

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.game.onEvent(new KeyDownEvent(e.getKeyCode()));
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.game.onEvent(new KeyUpEvent(e.getKeyCode()));
        keys[e.getKeyCode()] = false;
    }

    // endregion

    // region ...Focus listener methods...

    @Override
    public void focusGained(FocusEvent e) {
        this.game.onEvent(FocusGainedEvent.INSTANCE);
    }

    @Override
    public void focusLost(FocusEvent e) {
        this.game.onEvent(FocusLostEvent.INSTANCE);
    }

    // endregion

    // region ...Mouse listener methods...

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.game.onEvent(new MousePressedEvent(new Vec2i(e.getX(), e.getY()), MouseButton.fromAwt(e)));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.game.onEvent(new MouseReleasedEvent(new Vec2i(e.getX(), e.getY()), MouseButton.fromAwt(e)));
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.game.onEvent(new MouseDraggedEvent(new Vec2i(e.getX(), e.getY()), MouseButton.fromAwt(e)));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mousePos = Vec2i.fromPoint(e.getPoint());
    }

    // endregion

    // region ...Mouse wheel listener methods...

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.game.onEvent(new io.github.cubedtear.jcubit.awt.gameEngine.events.MouseWheelEvent(e.getPreciseWheelRotation()));
    }

    // endregion
}
