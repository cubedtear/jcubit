package io.github.cubedtear.jcubit.awt.gameEngine.gui;

import io.github.cubedtear.jcubit.awt.gameEngine.font.Font;
import io.github.cubedtear.jcubit.awt.render.IRender;

/**
 * @author Aritz Lopez
 */
public class Label extends GUIElement {

    private String text;
    private Font font;

    public Label(int x, int y, String text, Font font) {
        super(x, y, font.getStringWidth(text), font.getLineHeight() * text.split("\n").length);
        this.text = text;
        this.font = font;
    }

    public void draw(IRender render) {
        this.font.render(render, text, bounds.x, bounds.y);
    }

    public String getText() {
        return text;
    }

    public Font getFont() {
        return font;
    }
}
