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

import com.google.common.base.Preconditions;
import io.github.cubedtear.jcubit.util.API;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to read spritesheets from files.
 * <p>
 * A spritesheet is made of two files: The sheet, and the descriptor.
 * The sheet is an image file (readable by {@link ImageIO#read(InputStream)}.
 * <p>
 * The descriptor is a file with several lines, separated as {@link BufferedReader#readLine()}.
 * <p>
 * The first line of the file must be the path of the sheet file.
 * The following lines will have the following format: <p>
 * {@code spriteName.png x y w h} <p>
 * Where:
 * <ul>
 *     <li>spriteName is the name the sprite will have.</li>
 *     <li><i>x</i> is the x coordinate of the top-left corner of the sprite, within the sheet</li>
 *     <li><i>y</i> is the y coordinate of the top-left corner of the sprite, within the sheet</li>
 *     <li><i>w</i> is the width of the sprite, counted from <i>x</i></li>
 *     <li><i>h</i> is the height of the sprite, counted from <i>y</i></li>
 * </ul>
 *
 * Each line will therefore denote a sprite.
 * Note: There is nothing preventing different sprites from overlapping.
 * @author Aritz Lopez
 */
@API
public class SpriteSheetLoader {

	private static final Pattern spritePattern = Pattern.compile("(\\w+).png (\\d+) (\\d+) (\\d+) (\\d+)");

	/**
	 * Loads the sprite from the given path in the classpath.
	 * @param shtFile The path in the classpath for the descriptor.
	 * @return A spritesheet loaded from the given path.
     */
	@API
	public static HashMap<String, Sprite> load(String shtFile) {
		Preconditions.checkArgument(shtFile != null, "File name cannot be null!");
		Preconditions.checkArgument(shtFile.length() != 0, "You must specify a valid path");

		InputStream is = SpriteSheetLoader.class.getClassLoader().getResourceAsStream(shtFile);
		Preconditions.checkArgument(is != null, "Could not find file %s", shtFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		try {
			String line = reader.readLine();
			if (line == null) throw new IllegalArgumentException("First line of the file could not be read");
			InputStream imageStream = SpriteSheetLoader.class.getClassLoader().getResourceAsStream(line.trim());


			BufferedImage sheet = ImageIO.read(imageStream);

			HashMap<String, Sprite> sprites = new HashMap<>();

			while ((line = reader.readLine()) != null) {
				Matcher matcher = spritePattern.matcher(line);
				if (!matcher.matches())
					throw new IllegalArgumentException("Line " + line + " does not match regex " + spritePattern.pattern());
				String name = matcher.group(1);
				int x = Integer.parseInt(matcher.group(2));
				int y = Integer.parseInt(matcher.group(3));
				int w = Integer.parseInt(matcher.group(4));
				int h = Integer.parseInt(matcher.group(5));
				sprites.put(name, SpriteSheetLoader.getSprite(sheet, x, y, w, h));
			}

			return sprites;
		} catch (IOException e) {
			throw new IllegalArgumentException("Error loading sprite sheet: " + shtFile, e);
		}
	}

	/**
	 * Gets part of a BufferedImage as a Sprite.
	 * @param image The image from which the sprite will be taken.
	 * @param x The x coordinate of the top-left corner of the image where the sprite begins.
	 * @param y The y coordinate of the top-left corner of the image where the sprite begins.
	 * @param w The width of the sprite.
     * @param h The height of the sprite.
     * @return A sprite representing a subsection of the given image.
     */
	public static Sprite getSprite(BufferedImage image, int x, int y, int w, int h) {
		int[] pixels = image.getRGB(x, y, w, h, null, 0, w);
		return new Sprite(w, h, pixels);
	}

	/**
	 * @deprecated Use {@code HashMap<String, Sprite>} instead.
	 */
	@Deprecated
	public static class SpriteSheet extends HashMap<String, Sprite> {}
}
