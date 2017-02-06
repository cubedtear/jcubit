package io.github.cubedtear.jcubit.awt.gameEngine.font;

import io.github.cubedtear.jcubit.awt.render.Sprite;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aritz Lopez
 */
public class Glyph {

    private Sprite sprite;
    private char ch;
    private int xoffset;
    private int yoffset;
    private int xadvance;
    private Map<Glyph, Integer> kerns = new HashMap<>();

    public Glyph(char ch, int xoffset, int yoffset, int xadvance, Sprite sprite) {
        this.ch = ch;
        this.xoffset = xoffset;
        this.yoffset = yoffset;
        this.xadvance = xadvance;
        this.sprite = sprite;
    }

    public void addKerning(Glyph g, int kerning) {
        this.kerns.put(g, kerning);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public char getCh() {
        return ch;
    }

    public int getXoffset() {
        return xoffset;
    }

    public int getYoffset() {
        return yoffset;
    }

    public int getXadvance() {
        return xadvance;
    }

    public int getKern(Glyph other) {
        return kerns.containsKey(other) ? kerns.get(other) : 0;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && ch == ((Glyph) o).ch;
    }

    @Override
    public int hashCode() {
        return ch;
    }
}
