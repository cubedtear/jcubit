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
 * @author Aritz Lopez
 */
public abstract class GUIComponent {

    protected final Point pos;
    private Point pressedPoint = null;
    protected boolean pressed, hovered;
    protected boolean visible;

    public GUIComponent(Point pos) {
        this.pos = pos;
    }

    public abstract void render(Graphics g);

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void keyPressed(int key, char c) {

    }

    public void keyReleased(int key, char c) {

    }

    public void mouseWheelMoved(int change) {

    }

    public void mouseClicked(int button, int x, int y, int clickCount) {

    }

    public void mousePressed(int button, int x, int y) {
        if (button == Input.MOUSE_LEFT_BUTTON) {
            this.pressed = true;
            this.pressedPoint = new Point(x, y);
        }
    }

    public abstract Rectangle getBounds();

    public void mouseReleased(int button, int x, int y) {
        if (button == Input.MOUSE_LEFT_BUTTON) this.pressed = false;
        this.pressedPoint = null;
    }

    public void mouseMoved(int oldx, int oldy, int newx, int newy) {

    }

    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
    }

    public boolean wasMousePressedHere() {
        return this.pressedPoint != null;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
        this.pressed &= this.hovered;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}
