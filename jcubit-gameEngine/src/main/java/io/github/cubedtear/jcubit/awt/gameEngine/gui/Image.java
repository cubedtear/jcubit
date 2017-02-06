package io.github.cubedtear.jcubit.awt.gameEngine.gui;

import io.github.cubedtear.jcubit.awt.render.IRender;
import io.github.cubedtear.jcubit.awt.render.Sprite;
import io.github.cubedtear.jcubit.math.Vec2i;

/**
 * @author Aritz Lopez
 */
public class Image extends GUIElement {

    private final Sprite sprite;

    public Image(int x, int y, Sprite sprite) {
        super(x, y, sprite.getWidth(), sprite.getHeight());
        this.sprite = sprite;
    }

    public Image(Vec2i pos, Sprite sprite) {
        this(pos.x, pos.y, sprite);
    }

    @Override
    public void draw(IRender render) {
        render.draw(bounds.x, bounds.y, sprite);
    }

    public Sprite getSprite() {
        return sprite;
    }
}
