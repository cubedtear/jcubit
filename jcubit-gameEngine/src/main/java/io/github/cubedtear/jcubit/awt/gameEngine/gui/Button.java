package io.github.cubedtear.jcubit.awt.gameEngine.gui;

import io.github.cubedtear.jcubit.awt.gameEngine.events.*;
import io.github.cubedtear.jcubit.awt.render.IRender;
import io.github.cubedtear.jcubit.awt.render.Sprite;
import io.github.cubedtear.jcubit.awt.util.SpriteUtil;
import io.github.cubedtear.jcubit.math.Rectangle;
import io.github.cubedtear.jcubit.util.Consumer;


/**
 * @author Aritz Lopez
 */
public class Button extends GUIElement {

    private static final int BORDER_SIZE = 4;
    private static final int BG_COLOR = 0xFF888888;

    private final Sprite sprite, spriteDown;
    private final Consumer<Button> handler;
    private boolean isPressed = false;

    public Button(Rectangle bounds, Sprite sprite, Consumer<Button> handler) {
        super(bounds);
        final Sprite background = new Sprite(bounds.width, bounds.height, BG_COLOR);
        final Sprite border = SpriteUtil.addBeveledBorder(background, BG_COLOR, BORDER_SIZE, false);
        final Sprite invertedBorder = SpriteUtil.addBeveledBorder(background, BG_COLOR, BORDER_SIZE, true);
        this.sprite = SpriteUtil.compose(sprite, border, false);
        Sprite contentDown = SpriteUtil.scale(sprite, 0.9f, SpriteUtil.ScalingMethod.BILINEAR);
        contentDown = SpriteUtil.increaseSizeCentered(contentDown, sprite.getWidth(), sprite.getHeight());
        this.spriteDown = SpriteUtil.compose(contentDown, invertedBorder, true);
        this.handler = handler;
    }

    public Button(Rectangle bounds, Sprite sprite, final Runnable handler) {
        this(bounds, sprite, new Consumer<Button>() {
            @Override
            public void accept(Button button) {
                handler.run();
            }
        });
    }

    public void draw(IRender render) {
        render.draw(bounds.x, bounds.y, this.isPressed ? spriteDown : sprite);
    }

    @Override
    public void onEvent(Event event) {
        if (!(event instanceof MouseEvent) || ((MouseEvent) event).getButton() != MouseButton.LEFT) return;

        if (event instanceof MouseReleasedEvent) {
            this.isPressed = false;
        }
        if (this.bounds.contains(((MouseEvent) event).getPos())) {
            if (event instanceof MousePressedEvent) {
                this.isPressed = true;
            }
            if (event instanceof MouseReleasedEvent) {
                handler.accept(this);
            }
        }
    }
}
