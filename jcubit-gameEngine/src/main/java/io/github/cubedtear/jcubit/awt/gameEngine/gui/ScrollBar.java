package io.github.cubedtear.jcubit.awt.gameEngine.gui;

import io.github.cubedtear.jcubit.awt.gameEngine.events.*;
import io.github.cubedtear.jcubit.awt.render.IRender;
import io.github.cubedtear.jcubit.awt.render.Sprite;
import io.github.cubedtear.jcubit.awt.util.SpriteUtil;
import io.github.cubedtear.jcubit.math.Vec2i;

/**
 * @author Aritz Lopez
 */
public class ScrollBar extends GUIElement {

    public static final int DIM = 25;
    private static final int BG_COLOR = 0xFF444444;

    private static final Sprite upButton;
    private static final Sprite downButton;
    private static final Sprite leftButton;
    private static final Sprite rightButton;
    private static final Sprite bar;

    static {
        upButton = SpriteUtil.getSpriteFromClasspath("scroll_up.png");
        downButton = SpriteUtil.getSpriteFromClasspath("scroll_down.png");
        leftButton = SpriteUtil.getSpriteFromClasspath("scroll_left.png");
        rightButton = SpriteUtil.getSpriteFromClasspath("scroll_right.png");
        bar = SpriteUtil.getSpriteFromClasspath("scroll_bar.png");
    }

    private final Direction dir;
    private final Sprite background;
    private int barPos = 0;
    private int tempBarPos = -1;
    private int maxScrollAmount;
    private Vec2i dragStarted = null;

    public ScrollBar(int x, int y, int width, int height, Direction dir) {
        super(x, y, width, height);
        if (dir == Direction.VERTICAL && height <= 3 * DIM) // 2 Buttons + Scroll bar
            throw new IllegalArgumentException("Vertical bar height must be bigger than " + 3 * DIM + "!");
        else if (dir == Direction.HORIZONTAL && width <= 3 * DIM)
            throw new IllegalArgumentException("Horizontal bar width must be bigger than " + 3 * DIM + "!");

        this.dir = dir;
        if (dir == Direction.VERTICAL) {
            background = new Sprite(DIM, height, BG_COLOR);
            maxScrollAmount = height - 3 * DIM;
        } else {
            background = new Sprite(width, DIM, BG_COLOR);
            maxScrollAmount = width - 3 * DIM;
        }
    }

    public void draw(IRender render) {
        if (!this.isVisible()) return;
        render.draw(bounds.x, bounds.y, background);
        if (dir == Direction.VERTICAL) {
            render.draw(bounds.x, bounds.y, upButton);
            render.draw(bounds.x, bounds.y + bounds.height - downButton.getHeight(), downButton);
            render.draw(bounds.x, bounds.y + upButton.getHeight() + (tempBarPos == -1 ? barPos : tempBarPos), bar);
        } else {
            render.draw(bounds.x, bounds.y, leftButton);
            render.draw(bounds.getX2() - rightButton.getWidth(), bounds.y, rightButton);
            render.draw(bounds.x + rightButton.getWidth() + (tempBarPos == -1 ? barPos : tempBarPos), bounds.y, bar);
        }
    }

    @Override
    public void onEvent(Event event) {
        if (!(event instanceof MouseEvent)) return;
        MouseEvent me = ((MouseEvent) event);
        if (dir == Direction.VERTICAL) {
            if (event instanceof MousePressedEvent && bounds.contains(me.getPos())) {
                this.dragStarted = me.getPos();
                this.tempBarPos = barPos;
            } else if (event instanceof MouseReleasedEvent && this.dragStarted != null) {
                int dy = me.getPos().y - this.dragStarted.y;
                this.barPos += dy;
                this.barPos = Math.min(maxScrollAmount, Math.max(0, barPos));
                this.tempBarPos = -1;
                this.dragStarted = null;
            } else if (event instanceof MouseDraggedEvent && this.dragStarted != null) {
                int dy = me.getPos().y - this.dragStarted.y;
                this.tempBarPos = barPos + dy;
                this.tempBarPos = Math.min(maxScrollAmount, Math.max(0, tempBarPos));
            }
        } else if (dir == Direction.HORIZONTAL) {
            if (event instanceof MousePressedEvent && bounds.contains(me.getPos())) {
                this.dragStarted = me.getPos();
                this.tempBarPos = barPos;
            } else if (event instanceof MouseReleasedEvent && this.dragStarted != null) {
                int dx = me.getPos().x - this.dragStarted.x;
                this.barPos += dx;
                this.barPos = Math.min(maxScrollAmount, Math.max(0, barPos));
                this.tempBarPos = -1;
                this.dragStarted = null;
            } else if (event instanceof MouseDraggedEvent && this.dragStarted != null) {
                int dx = me.getPos().x - this.dragStarted.x;
                this.tempBarPos = barPos + dx;
                this.tempBarPos = Math.min(maxScrollAmount, Math.max(0, tempBarPos));
            }
        }
    }

    public double getScroll() {
        return (tempBarPos == -1 ? barPos : tempBarPos) / (double) this.maxScrollAmount;
    }

    public enum Direction {
        HORIZONTAL, VERTICAL
    }
}
