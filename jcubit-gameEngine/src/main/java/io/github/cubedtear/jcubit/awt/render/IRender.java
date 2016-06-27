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

package io.github.cubedtear.jcubit.awt.render;

import java.awt.image.BufferedImage;

/**
 * @author Aritz Lopez
 */
public interface IRender {

	void clear();

	void draw(int x, int y, int width, int height, int[] colors);

	void draw(int x, int y, Sprite sprite);

	void draw(int x, int y, String spriteName);

	void draw(int x, int y, long deltaNS, AnimatedSprite sprite);

	void setBlend(boolean blend);

	BufferedImage getImage();

	int getWidth();

	int getHeight();
}
