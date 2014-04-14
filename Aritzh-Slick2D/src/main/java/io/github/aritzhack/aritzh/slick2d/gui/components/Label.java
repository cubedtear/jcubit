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

import io.github.aritzhack.aritzh.slick2d.util.SlickUtil;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

/**
 * A label that can hold a String to be drawn in a GUI
 *
 * @author Aritz Lopez
 */
public class Label extends GUIComponent {

    protected final String s;
    protected final Rectangle bounds;

    /**
     * <p>Creates a Label with the specified String at the specified position</p>
     * <p>Note: The width and the height of the Label will be determined the first time they are drawn</p>
     *
     * @param s   The String this Label will draw
     * @param pos The position of this Label (the top-left corner)
     */
    public Label(String s, Point pos) {
        this(s, new Rectangle(pos.getX(), pos.getY(), -1, 0));
    }

    /**
     * Creates a Label with the specified String and the specified bounds
     *
     * @param s      The String this Label will draw
     * @param bounds The bounds of this Label
     */
    public Label(String s, Rectangle bounds) {
        super(SlickUtil.getPos(bounds));
        this.bounds = bounds;
        this.s = s;
    }

    @Override
    public void render(Graphics g) {
        if (this.bounds.getWidth() == -1) this.bounds.setSize(g.getFont().getWidth(s), g.getFont().getHeight(s));
        g.drawString(s, this.pos.getX(), this.pos.getY());
    }

    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }
}
