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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * A 2D sprite with integer ARGB format (32bit, 8 per channel).
 * @author Aritz Lopez
 */
public class Sprite {

	private final int[] pixels;
	private final int width;
	private final int height;

	/**
	 * Creates a sprite with the given size and pixels.
	 * The format of the pixel array is the following: the first index of the array is the top-leftmost pixel,
	 * and the next one is the one to the right of that. It keeps going for {@code width} pixels. The next pixel will7
	 * then be the first (leftmost) pixel in the second row, from the top.
	 * @param width The width of the sprite.
	 * @param height The height of the sprite.
	 * @param pixels The pixels. Its size MUST be {@code width * height}.
     */
	public Sprite(int width, int height, int[] pixels) {
		Preconditions.checkArgument(width >= 0 && height >= 0, "Sprite sizes cannot be negative");
		Preconditions.checkArgument(pixels.length == width * height, "Pixel array size does not match the given width and height");
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}

	/**
	 * Creates a sprite with the given size, filled with the given color.
	 * @param width The width of the sprite.
	 * @param height The height of the sprite.
	 * @param color The color to fill the sprite with, in ARGB format.
     */
	public Sprite(int width, int height, int color) {
		Preconditions.checkArgument(width >= 0 && height >= 0, "Sprite sizes cannot be negative");
		Preconditions.checkArgument((long) width * (long) height < Integer.MAX_VALUE, "Sizes are too big (" + width + " * " + height + " > Integer.MAX_VALUE)");
		this.height = height;
		this.width = width;
		this.pixels = new int[width * height];
		Arrays.fill(this.pixels, color);
	}

	/**
	 * Creates a sprite from the classloader's resources with the given path, trimmed.
	 * @param sprite The name or path to the sprites' file.
	 * @throws IOException If an error occurs reading the file.
	 * @see ImageIO#read(InputStream)
     */
	public Sprite(String sprite) throws IOException {
		this(ImageIO.read(Sprite.class.getClassLoader().getResourceAsStream(sprite.trim())));
	}

	/**
	 * Creates a sprite from the given file.
	 * @param file The file to read the sprite from.
	 * @throws IOException If an error occurs reading the file.
     */
	public Sprite(File file) throws IOException {
		this(ImageIO.read(file));
	}

	/**
	 * Creates a sprite from the given BufferedImage.
	 * @param image The image to create the sprite from.
     */
	public Sprite(BufferedImage image) {
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = image.getRGB(0, 0, width, height, null, 0, width);
	}

	/**
	 * @return The pixel array of this sprite.
     */
	public int[] getPixels() {
		return pixels;
	}

	/**
	 * @return The width of this sprite.
     */
	public int getWidth() {
		return width;
	}

	/**
	 * @return The height of this sprite.
     */
	public int getHeight() {
		return height;
	}

	/**
	 * Creates an exact copy of {@code this} sprite. The resultin sprite will be totally independent to {@code this} sprite.
	 * @return a copy of {@code this} sprite.
     */
	public Sprite copy() {
		int[] newPix = Arrays.copyOf(this.pixels, this.pixels.length);
		return new Sprite(this.width, this.height, newPix);
	}
}
