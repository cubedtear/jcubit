package io.github.cubedtear.jcubit.awt.gameEngine.events;

/**
 * @author Aritz Lopez
 */
public class MouseWheelEvent extends Event {

    private final double scrollAmount;

    public MouseWheelEvent(double scrollAmount) {
        this.scrollAmount = scrollAmount;
    }

    public double getScrollAmount() {
        return scrollAmount;
    }
}
