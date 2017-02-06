package io.github.cubedtear.jcubit.awt.gameEngine.gui;


import com.google.common.collect.Maps;
import io.github.cubedtear.jcubit.awt.gameEngine.events.Event;
import io.github.cubedtear.jcubit.awt.render.IRender;
import io.github.cubedtear.jcubit.math.Rectangle;
import io.github.cubedtear.jcubit.util.Consumer;

import java.util.Map;

/**
 * @author Aritz Lopez
 */
public abstract class GUIElement {
    protected Rectangle bounds;
    protected boolean visible = true;
    protected Map<Class<? extends Event>, Consumer<Event>> listeners = Maps.newHashMap();

    public GUIElement(Rectangle bounds) {
        this.bounds = bounds;
    }

    public GUIElement(int x, int y, int width, int height) {
        this(new Rectangle(x, y, width, height));
    }

    public void onEvent(Event event) {
        for (Map.Entry<Class<? extends Event>, Consumer<Event>> entry : listeners.entrySet()) {
            if (entry.getKey() == event.getClass()) entry.getValue().accept(event);
        }
    }

    public void draw(IRender render) {

    }

    public void update() {

    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setX(int x) {
        this.bounds = new Rectangle(x, bounds.y, bounds.width, bounds.height);
    }

    public void setY(int y) {
        this.bounds = new Rectangle(bounds.x, y, bounds.width, bounds.height);
    }
}
