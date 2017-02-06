package io.github.cubedtear.jcubit.awt.gameEngine.gui;

import io.github.cubedtear.jcubit.awt.gameEngine.events.*;
import io.github.cubedtear.jcubit.awt.gameEngine.font.Font;
import io.github.cubedtear.jcubit.awt.render.IRender;
import io.github.cubedtear.jcubit.awt.render.Sprite;
import io.github.cubedtear.jcubit.awt.util.SpriteUtil;
import io.github.cubedtear.jcubit.math.Rectangle;
import io.github.cubedtear.jcubit.util.Consumer;

/**
 * @author Aritz Lopez
 */
public class TextButton extends GUIElement {

    private static final int BORDER_SIZE = 4;
    private final Font font;
    private final Consumer<TextButton> handler;
    private final int BG_COLOR = 0xFF888888;
    private String text;
    private Sprite sprite, spriteDown;
    private boolean isPressed = false;

    public TextButton(Rectangle bounds, Font font, String text, Consumer<TextButton> handler) {
        super(bounds);
        this.font = font;
        this.text = text;
        this.handler = handler;
        recalculateSprites();
    }

    public TextButton(int x, int y, Font font, String text, Consumer<TextButton> handler) {
        this(new Rectangle(x, y, font.getStringWidth(text) + 4 * BORDER_SIZE, font.getLineHeight() + 4 * BORDER_SIZE), font, text, handler);
    }

    public TextButton(Rectangle bounds, Font font, String text, final Runnable handler) {
        this(bounds, font, text, new Consumer<TextButton>() {
            @Override
            public void accept(TextButton textButton) {
                handler.run();
            }
        });
    }

    public TextButton(int x, int y, Font font, String text, final Runnable handler) {
        this(x, y, font, text, new Consumer<TextButton>() {
            @Override
            public void accept(TextButton textButton) {
                handler.run();
            }
        });
    }

    private void recalculateSprites() {
        final Sprite background = new Sprite(bounds.width, bounds.height, BG_COLOR);
        this.sprite = SpriteUtil.addBeveledBorder(background, BG_COLOR, BORDER_SIZE, false);
        this.spriteDown = SpriteUtil.addBeveledBorder(background, BG_COLOR, BORDER_SIZE, true);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void fitToText() {
        bounds = new Rectangle(bounds.x, bounds.y, font.getStringWidth(text) + 2 * BORDER_SIZE, font.getLineHeight() + 2 * BORDER_SIZE);
        recalculateSprites();
    }

    @Override
    public void onEvent(Event event) {
        if (!(event instanceof MouseEvent) || ((MouseEvent) event).getButton() != MouseButton.LEFT) return;

        if (event instanceof MouseReleasedEvent) {
            this.isPressed = false;
        }
        //noinspection Duplicates
        if (this.bounds.contains(((MouseEvent) event).getPos())) {
            if (event instanceof MousePressedEvent) {
                this.isPressed = true;
            }
            if (event instanceof MouseReleasedEvent) {
                handler.accept(this);
            }
        }
    }

    @Override
    public void draw(IRender render) {
        render.draw(bounds.x, bounds.y, this.isPressed ? spriteDown : sprite);
        this.font.render(render, text, bounds.x + 2*BORDER_SIZE, bounds.y + 2*BORDER_SIZE);
    }
}
