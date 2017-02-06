package io.github.cubedtear.jcubit.awt.gameEngine.gui;

import io.github.cubedtear.jcubit.awt.gameEngine.events.Event;
import io.github.cubedtear.jcubit.awt.gameEngine.events.MouseEvent;
import io.github.cubedtear.jcubit.awt.render.IRender;
import io.github.cubedtear.jcubit.math.Rectangle;
import io.github.cubedtear.jcubit.math.Vec2i;

import static io.github.cubedtear.jcubit.awt.gameEngine.gui.ScrollBar.Direction.HORIZONTAL;
import static io.github.cubedtear.jcubit.awt.gameEngine.gui.ScrollBar.Direction.VERTICAL;

/**
 * @author Aritz Lopez
 */
public class ScrollPane extends Pane {

    private final ScrollBar horiz;
    private final ScrollBar vert;
    private Vec2i scroll = new Vec2i(0, 0);
    private Rectangle contentBounds;

    public ScrollPane(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.contentBounds = Rectangle.EMPTY;
        this.vert = new ScrollBar(width - ScrollBar.DIM, 0, ScrollBar.DIM, height - ScrollBar.DIM, VERTICAL);
        this.horiz = new ScrollBar(0, height - ScrollBar.DIM, width - ScrollBar.DIM, ScrollBar.DIM, HORIZONTAL);
        vert.setVisible(false);
        horiz.setVisible(false);
    }

    @Override
    public void draw(IRender render) {
        if (!this.isVisible()) return;
        render.pushClipping(bounds.x, bounds.y, bounds.width - (isVerticalScrollbarVisible() ? ScrollBar.DIM : 0), bounds.height - (isHorizontalScrollbarVisible() ? ScrollBar.DIM : 0));
        render.pushTranslation(new Vec2i(bounds.x, bounds.y));

        if (isHorizontalScrollbarVisible() || isVerticalScrollbarVisible()) {
            render.pushTranslation(scroll.negate());
        }

        synchronized (this.children) {
            for (GUIElement e : this.children) e.draw(render);
        }
        render.popClipping();

        if (isHorizontalScrollbarVisible() || isVerticalScrollbarVisible()) {
            render.popTranslation();
        }

        this.horiz.draw(render);
        this.vert.draw(render);

        render.drawBorder(this.bounds.move(this.bounds.getTopLeft().negate()), 1, 0xFFFF0000);

        render.popTranslation();
    }

    private boolean isHorizontalScrollbarVisible() {
        return contentBounds.width > bounds.width - ScrollBar.DIM;
    }

    private boolean isVerticalScrollbarVisible() {
        return contentBounds.height > bounds.height - ScrollBar.DIM;
    }

    public void update() {
        int horiz = (int) ((contentBounds.width - (bounds.width - ScrollBar.DIM)) * this.horiz.getScroll());
        int vert = (int) ((contentBounds.height - (bounds.height - ScrollBar.DIM)) * this.vert.getScroll());
        if (horiz < 0) horiz = 0;
        if (vert < 0) vert = 0;
        scroll = new Vec2i(horiz, vert);
        super.update();
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof MouseEvent) {
            event = ((MouseEvent) event).move(this.bounds.getTopLeft().negate());
        }

        this.vert.onEvent(event);
        this.horiz.onEvent(event);
        event = moveByScroll(event);
        for (GUIElement e : this.children) e.onEvent(event);
    }

    public void add(GUIElement... elements) {
        super.add(elements);
        for (GUIElement e : elements)
            this.contentBounds = this.contentBounds.extendToContain(e.bounds);
        this.horiz.setVisible(contentBounds.width > bounds.width - ScrollBar.DIM);
        this.vert.setVisible(contentBounds.height > bounds.height - ScrollBar.DIM);
    }

    /**
     * Moves the event from this ScrollPane's interior coordinate system, to the
     * coordinate system of the children of this ScrollPane, where it should match with the position
     * and size of any direct children.
     * If it does not make sense to "move" the event (e.g. it is a keyboard event, and not a mouse event),
     * this method will have no efect, and return the event given.
     *
     * @param event The event to move.
     * @return The event, moved as needed.
     */
    public Event moveByScroll(Event event) {
        if (event instanceof MouseEvent) {
            event = ((MouseEvent) event).move(this.scroll);
        }
        return event;
    }
}
