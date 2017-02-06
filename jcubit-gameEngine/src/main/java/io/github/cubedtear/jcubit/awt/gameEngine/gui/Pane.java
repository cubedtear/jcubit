package io.github.cubedtear.jcubit.awt.gameEngine.gui;

import com.google.common.collect.Sets;
import io.github.cubedtear.jcubit.awt.gameEngine.events.Event;
import io.github.cubedtear.jcubit.awt.gameEngine.events.MouseEvent;
import io.github.cubedtear.jcubit.awt.render.IRender;
import io.github.cubedtear.jcubit.math.Rectangle;

import java.util.Collections;
import java.util.Set;

/**
 * @author Aritz Lopez
 */
public class Pane extends GUIElement {

    protected final Set<GUIElement> children = Sets.newHashSet();

    public Pane(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public Pane(Rectangle bounds) {
        super(bounds);
    }

    @Override
    public void draw(IRender render) {
        render.pushClipping(bounds.x, bounds.y, bounds.width, bounds.height);
        for (GUIElement child : children) child.draw(render);
        render.popClipping();
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof MouseEvent) {
            if (!this.bounds.contains(((MouseEvent) event).getPos())) return;
            event = ((MouseEvent) event).move(this.bounds.getTopLeft().negate());
        }
        super.onEvent(event);
        for (GUIElement child : children) child.onEvent(event);
    }

    @Override
    public void update() {
        for (GUIElement child : children) child.update();
    }

    public void add(GUIElement ...elements) {
        synchronized (this.children) {
            Collections.addAll(children, elements);
        }
    }
}
