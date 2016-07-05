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

package io.github.cubedtear.jcubit.awt.util;

import io.github.cubedtear.jcubit.util.API;

import javax.swing.*;
import java.awt.*;

/**
 * @author Aritz Lopez
 */
@API
public class AwtUtil {

	/**
	 * Returns the window that holds the given component.
	 * @param component The component.
	 * @return The window the component is into.
     */
	@API
	public static Window getWindow(Component component) {
		if (component == null) return JOptionPane.getRootFrame();
		else if (component instanceof Window) return (Window) component;
		else return AwtUtil.getWindow(component.getParent());
	}
}