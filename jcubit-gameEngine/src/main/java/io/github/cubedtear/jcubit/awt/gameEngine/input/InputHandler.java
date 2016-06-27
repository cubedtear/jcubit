/*
 * Copyright 2014 Aritz Lopez
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.cubedtear.jcubit.awt.gameEngine.input;

import io.github.cubedtear.jcubit.awt.gameEngine.CanvasEngine;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Queue;

/**
 * Input handler for the {@link CanvasEngine} class.
 *
 * @author Aritz Lopez
 */
public class InputHandler implements KeyListener, FocusListener, MouseInputListener {

	private final boolean[] keys = new boolean[65536];
	private final List<Integer> checked = new ArrayList<>();
	private final Queue<MouseInputEvent> mouseEvents = new LinkedList<>();
	private boolean focus;
	private Point lastMousePos = new Point();

	// region ... "Getter" methods...

	/**
	 * True if the canvas is focused
	 *
	 * @return True if the canvas is focused
	 */
	public boolean hasFocus() {
		return focus;
	}

	/**
	 * True if the key was pressed, but hadn't been previously checked. Subsequent calls to this method will,
	 * therefore, return always false, as long as the key hasn't been released and pressed again.
	 *
	 * @param keyCode The code that corresponds to the key, according to {@link java.awt.event.KeyEvent}
	 * @return True if the key was pressed, but hadn't been previously checked.
	 */
	public boolean wasKeyTyped(int keyCode) {
		if (this.isKeyDown(keyCode) && !this.checked.contains(keyCode)) {
			this.checked.add(keyCode);
			return true;
		}
		return false;
	}

	/**
	 * Returns true if the key is being pressed.
	 *
	 * @param keyCode The code that corresponds to the key, according to {@link java.awt.event.KeyEvent}
	 * @return true if the key is being pressed.
	 */
	public boolean isKeyDown(int keyCode) {
		return this.keys[keyCode];
	}

	/**
	 * Returns the position of the mouse.
	 *
	 * @return the position of the mouse.
	 */
	public Point getMousePos() {
		return this.lastMousePos;
	}

	/**
	 * Returns a stack of {@link InputHandler.MouseInputEvent MouseInputEvents}
	 * that represent the events the mouse had since it was last pressed.
	 *
	 * @return a stack of mouse events
	 */
	public Queue<MouseInputEvent> getMouseEvents() {
		return this.mouseEvents;
	}

	//endregion

	// region ...Focus listener methods...

	@Override
	public void focusGained(FocusEvent e) {
		this.focus = true;
	}

	@Override
	public void focusLost(FocusEvent e) {
		this.focus = false;
		Arrays.fill(this.keys, false);
	}

	// endregion

	// region ...Keyboard listener methods...

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.keys[e.getKeyCode()] = false;
		this.checked.remove((Integer) e.getKeyCode());
	}

	// endregion

	// region ...Mouse listener methods...

	@Override
	public void mouseClicked(MouseEvent e) {
		mouseEvents.add(MouseInputEvent.fromMouseEvent(MouseAction.CLICKED, e));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.clearMouseEvents();
		mouseEvents.add(MouseInputEvent.fromMouseEvent(MouseAction.PRESSED, e));
	}

	public void clearMouseEvents() {
		this.mouseEvents.clear();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseEvents.add(MouseInputEvent.fromMouseEvent(MouseAction.RELEASED, e));
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseEvents.add(MouseInputEvent.fromMouseEvent(MouseAction.DRAGGED, e));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.lastMousePos.setLocation(e.getX(), e.getY());
	}

	// endregion

	public enum MouseButton {
		LEFT, MIDDLE, RIGHT, NONE;

		public static MouseButton getFromEvent(int eventButton) {
			switch (eventButton) {
				case MouseEvent.BUTTON1:
					return LEFT;
				case MouseEvent.BUTTON2:
					return MIDDLE;
				case MouseEvent.BUTTON3:
					return RIGHT;
				default:
					return NONE;
			}
		}
	}

	public enum MouseAction {
		PRESSED, RELEASED, DRAGGED, CLICKED
	}

	public static class MouseInputEvent {
		private final MouseButton button;
		private final MouseAction action;
		private final Point position;

		public MouseInputEvent(MouseButton button, MouseAction action, Point position) {
			this.button = button;
			this.action = action;
			this.position = position;
		}

		public static MouseInputEvent fromMouseEvent(MouseAction action, MouseEvent e) {
			return new MouseInputEvent(MouseButton.getFromEvent(e.getButton()), action, new Point(e.getX(), e.getY()));
		}

		public MouseButton getButton() {
			return button;
		}

		public MouseAction getAction() {
			return action;
		}

		public Point getPosition() {
			return position;
		}
	}


}
