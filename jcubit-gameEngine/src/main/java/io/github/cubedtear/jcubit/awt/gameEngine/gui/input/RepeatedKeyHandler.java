package io.github.cubedtear.jcubit.awt.gameEngine.gui.input;

import io.github.cubedtear.jcubit.awt.gameEngine.StateGame;

/**
 * @author Aritz Lopez
 */
public class RepeatedKeyHandler {

    private final int keyCode;
    private final long firstDelay;
    private final long repeatDelay;
    private long lastUPress;
    private long secondLastUPress;
    private long lastURepeat;

    public RepeatedKeyHandler(int keyCode, long firstDelay, long repeatDelay) {
        this.keyCode = keyCode;
        this.firstDelay = firstDelay;
        this.repeatDelay = repeatDelay;
    }

    public boolean handle(StateGame game) {
        if (game.isKeyDown(keyCode)) {
            if (lastUPress == -1) {
                lastUPress = System.currentTimeMillis();
                return true;
            } else if (System.currentTimeMillis() - lastUPress > firstDelay) {
                if (secondLastUPress == -1) {
                    secondLastUPress = System.currentTimeMillis();
                    return true;
                } else if (System.currentTimeMillis() - secondLastUPress > firstDelay) {
                    if (lastURepeat == -1 || System.currentTimeMillis() - lastURepeat > repeatDelay) {
                        lastURepeat = System.currentTimeMillis();
                        return true;
                    }
                }
            }
        } else {
            lastUPress = -1;
            lastURepeat = -1;
            secondLastUPress = -1;
        }
        return false;
    }
}
