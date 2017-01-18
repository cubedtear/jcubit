package io.github.cubedtear.jcubit.awt.gameEngine;

import java.awt.*;
import java.awt.event.*;

/**
 * @author Aritz Lopez
 */
public class InputAdapter implements KeyListener, FocusListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private final boolean[] keys = new boolean[KeyEvent.KEY_LAST + 1];
    private final StateGame game;
    private Point lastMousePos = new Point();

    public InputAdapter(StateGame game) {
        this.game = game;
    }

    // region ...Key listener methods...

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    // endregion

    // region ...Focus listener methods...

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

    }

    // endregion

    // region ...Mouse listener methods...

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    // endregion

    // region ...Mouse wheel listener methods...

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    // endregion
}
