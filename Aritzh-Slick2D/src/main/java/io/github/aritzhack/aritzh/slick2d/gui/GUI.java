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

package io.github.aritzhack.aritzh.slick2d.gui;

import com.google.common.collect.Lists;
import io.github.aritzhack.aritzh.slick2d.gui.components.GUIComponent;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;

import java.util.List;

/**
 * Special {@link org.newdawn.slick.InputListener} that can hold
 * {@link io.github.aritzhack.aritzh.slick2d.gui.components.GUIComponent GUIComponents} and that can be rendered
 *
 * @author Aritz Lopez
 */
public abstract class GUI implements InputListener {

    protected final int width, height;
    protected final List<GUIComponent> components = Lists.newArrayList();

    /**
     * Creates a GUI with the specified size
     *
     * @param width  The width of the GUI
     * @param height The height of the GUI
     */
    public GUI(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Draws this GUI into the provided Graphics object
     *
     * @param g The graphic environment into which this GUI should be drawn
     */
    public void render(Graphics g) {
        for (GUIComponent c : this.components) c.render(g);
    }

    // region ...Controller...

    @Override
    public void controllerLeftPressed(int controller) {

    }

    @Override
    public void controllerLeftReleased(int controller) {

    }

    @Override
    public void controllerRightPressed(int controller) {

    }

    @Override
    public void controllerRightReleased(int controller) {

    }

    @Override
    public void controllerUpPressed(int controller) {

    }

    @Override
    public void controllerUpReleased(int controller) {

    }

    @Override
    public void controllerDownPressed(int controller) {

    }

    @Override
    public void controllerDownReleased(int controller) {

    }

    @Override
    public void controllerButtonPressed(int controller, int button) {

    }

    @Override
    public void controllerButtonReleased(int controller, int button) {

    }

    // endregion

    // region ...Keyboard...

    @Override
    public void keyPressed(int key, char c) {
        for (GUIComponent comp : this.components) comp.keyPressed(key, c);
    }

    @Override
    public void keyReleased(int key, char c) {
        for (GUIComponent comp : this.components) comp.keyReleased(key, c);
    }

    // endregion

    // region ...Mouse...

    @Override
    public void mouseWheelMoved(int change) {
        for (GUIComponent comp : this.components) {
            comp.mouseWheelMoved(change);
        }
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        for (GUIComponent comp : this.components) if (comp.getBounds().contains(x, y)) comp.mousePressed(button, x, y);
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        for (GUIComponent comp : this.components)
            if (comp.getBounds().contains(x, y)) {
                if (comp.wasMousePressedHere()) comp.mouseClicked(button, x, y, 1);
                comp.mouseReleased(button, x, y);
            }
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        for (GUIComponent comp : this.components) {
            if (comp.getBounds().contains(oldx, oldy)) comp.setHovered(!comp.isHovered());
            if (comp.getBounds().contains(newx, newy)) comp.mouseMoved(oldx, oldy, newx, newy);
        }
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        for (GUIComponent comp : this.components) {
            if (comp.getBounds().contains(newx, newy)) {
                comp.setPressed(!comp.isPressed() && comp.wasMousePressedHere());
            } else {
                comp.setPressed(!comp.isPressed());
            }
            if (comp.getBounds().contains(newx, newy)) {
                comp.mouseDragged(oldx, oldy, newx, newy);
            }
        }
    }
    // endregion

    // region ...Others...

    @Override
    public void setInput(Input input) {

    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputEnded() {

    }

    @Override
    public void inputStarted() {

    }

    // endregion
}
