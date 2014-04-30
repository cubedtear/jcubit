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

package io.github.aritzhack.aritzh.slick2d.gui.components;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

/**
 * Component of a {@link io.github.aritzhack.aritzh.slick2d.gui.GUI}
 *
 * @author Aritz Lopez
 */
public abstract class GUIComponent {

    protected final Point pos;
    protected boolean pressed, hovered;
    protected boolean visible;
    private Point pressedPoint = null;

    /**
     * Creates a GUIComponent at the specified position
     *
     * @param pos
     */
    public GUIComponent(Point pos) {
        this.pos = pos;
    }

    /**
     * Draws the component into the provided Graphics environment
     *
     * @param g The Graphics environment into which the component will be drawn
     */
    public abstract void render(Graphics g);

    /**
     * Returns the bounds of this GUIComponent
     *
     * @return the bounds of this GUIComponent
     */
    public abstract Rectangle getBounds();

    /**
     * Whether this component is visible (and therefore should be rendered)
     *
     * @return True if the component should be rendered
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the visibility of this component
     *
     * @param visible Whether this component should be visible (and therefore rendered)
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Called when a key is pressed
     *
     * @param key The key code according to {@link org.newdawn.slick.Input}
     * @param c   The char corresponding to the key pressed
     * @see org.newdawn.slick.Input
     */
    public void keyPressed(int key, char c) {

    }

    /**
     * Called when a key is released
     *
     * @param key The key code according to {@link org.newdawn.slick.Input}
     * @param c   The char corresponding to the key released
     * @see org.newdawn.slick.Input
     */
    public void keyReleased(int key, char c) {

    }

    /**
     * Called when the mouse wheel is moved
     *
     * @param change The amount changed (can be positive or negative, depending on the direction)
     */
    public void mouseWheelMoved(int change) {

    }

    /**
     * <p>Called when the mouse was clicked (pressed and released)</p>
     * <p>Note: it is preferred to use {@link io.github.aritzhack.aritzh.slick2d.gui.components.GUIComponent#mouseClicked(int, int, int, int) mouseClicked(int, int, int ,int)}
     * and {@link io.github.aritzhack.aritzh.slick2d.gui.components.GUIComponent#mouseReleased(int, int, int) mouseReleased(int, int, int)}, since they are more accurate</p>
     *
     * @param button     The button that was pressed, according to {@link org.newdawn.slick.Input}
     * @param x          The x-position of the click (in the window, not the screen)
     * @param y          The y-position of the click (in the window, not the screen)
     * @param clickCount The amount of clicks (1 for single click, 2 for double-click etc.)
     */
    public void mouseClicked(int button, int x, int y, int clickCount) {

    }

    /**
     * Called when the mouse was pressed
     *
     * @param button The mouse button, according to {@link org.newdawn.slick.Input}
     * @param x      The x-position of the click (in the window, not the screen)
     * @param y      The y-position of the click (in the window, not the screen)
     */
    public void mousePressed(int button, int x, int y) {
        if (button == Input.MOUSE_LEFT_BUTTON) {
            this.pressed = true;
            this.pressedPoint = new Point(x, y);
        }
    }

    /**
     * Called when the mouse is released
     *
     * @param button The mouse button, according to {@link org.newdawn.slick.Input}
     * @param x      The x-position of the click (in the window, not the screen)
     * @param y      The y-position of the click (in the window, not the screen)
     */
    public void mouseReleased(int button, int x, int y) {
        if (button == Input.MOUSE_LEFT_BUTTON) this.pressed = false;
        this.pressedPoint = null;
    }

    /**
     * Called when the mouse was moved
     *
     * @param oldx The x-position from which it was moved
     * @param oldy The y-position from which it was moved
     * @param newx The x-position to which it was moved
     * @param newy The y-position to which it was moved
     */
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {

    }

    /**
     * Called when the mouse was dragged (i.e. moved while pressed down)
     *
     * @param oldx The x-position from which it was dragged
     * @param oldy The y-position from which it was dragged
     * @param newx The x-position to which it was dragged
     * @param newy The y-position to which it was dragged
     */
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
    }

    /**
     * <p>Whether the mouse was pressed in this component.</p>
     * <p>This differs from {@link GUIComponent#isPressed()} in that {@code isPressed()} checks whether the component
     * is currently being pressed, whereas this checks whether when the mouse was pressed, it pressed this
     * (i.e if the mouse was clicked here, and then, dragged out without releasing it, {@code isPressed()}
     * would return false, whereas this would return true)</p>
     *
     * @return Whether the mouse was pressed in this component
     */
    public boolean wasMousePressedHere() {
        return this.pressedPoint != null;
    }

    /**
     * Returns whether the mouse is over this component
     *
     * @return whether the mouse is over this component
     */
    public boolean isHovered() {
        return hovered;
    }

    /**
     * Sets whether the mouse is over this component
     *
     * @param hovered whether the mouse is over this component
     */
    public void setHovered(boolean hovered) {
        this.hovered = hovered;
        this.pressed &= this.hovered;
    }

    /**
     * <p>Returns whether this component is being pressed with the mouse</p>
     * <p>Note: to see the difference between this and {@code wasMousePressedHere()}, check the javadoc of
     * {@link GUIComponent#wasMousePressedHere()}</p>
     *
     * @return whether this component is being pressed with the mouse
     */
    public boolean isPressed() {
        return pressed;
    }

    /**
     * Sets whether this component is being pressed with the mouse
     *
     * @param pressed whether this component is being pressed with the mouse
     */
    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}
