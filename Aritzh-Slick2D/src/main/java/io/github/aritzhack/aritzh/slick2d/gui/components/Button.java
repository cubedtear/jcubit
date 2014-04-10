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

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

/**
 * @author Aritz Lopez
 */
public class Button extends Label {

    private static final Color COLOR_1 = new Color(new java.awt.Color(43, 43, 44).getRGB()); // So that IntelliJ recognises the color
    private static final Color COLOR_2 = new Color(new java.awt.Color(128, 128, 128).getRGB());
    private static final Color COLOR_3 = new Color(new java.awt.Color(189, 189, 189).getRGB());
    private static final int BORDER_SIZE = 2;
    private final Runnable onClicked;

    private float tx = -1, ty = -1;

    public Button(Rectangle bounds, Runnable onClicked) {
        this("", bounds, onClicked);
    }

    public Button(String s, Rectangle bounds, Runnable onClicked) {
        super(s, bounds);
        this.onClicked = onClicked;
    }

    @Override
    public void render(Graphics g) {
        if (tx == -1) {
            this.tx = this.bounds.getWidth() / 2 - g.getFont().getWidth(this.s) / 2;
            this.ty = this.bounds.getHeight() / 2 - g.getFont().getLineHeight() / 2 - 2;
        }

        g.setColor(this.pressed ? COLOR_2 : this.hovered ? COLOR_3 : COLOR_2);
        g.fillRect(this.bounds.getX(), this.bounds.getY(), this.bounds.getWidth(), this.bounds.getHeight());

        g.setColor(this.pressed ? COLOR_1 : this.hovered ? COLOR_2 : COLOR_3);
        g.fillRect(this.bounds.getX() + BORDER_SIZE, this.bounds.getY() + BORDER_SIZE, this.bounds.getWidth() - 2 * BORDER_SIZE, this.bounds.getHeight() - 2 * BORDER_SIZE);

        g.setColor(this.pressed ? COLOR_3 : this.hovered ? COLOR_1 : COLOR_1);
        g.translate(this.tx, this.ty);
        super.render(g);
        g.translate(-this.tx, -this.ty);
    }


    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        super.mouseClicked(button, x, y, clickCount);
        if (button != Input.MOUSE_LEFT_BUTTON) return;
        if (this.onClicked != null) this.onClicked.run();
    }
}
