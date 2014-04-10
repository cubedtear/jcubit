/*
 * Copyright (c) 2014 Aritzh (Aritz Lopez)
 *
 * This file is part of AritzhUtil
 *
 * AritzhUtil is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * AritzhUtil is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with AritzhUtil.
 * If not, see http://www.gnu.org/licenses/.
 */

package io.github.aritzhack.aritzh.render;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Aritz Lopez
 */
public class SpriteSheetLoader {

    private static final Pattern spritePattern = Pattern.compile("(\\w+).png (\\d+) (\\d+) (\\d+) (\\d+)");

    public static Map<String, Sprite> load(String shtFile) {
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

            Map<String, Sprite> sprites = Maps.newHashMap();

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

    public static Sprite getSprite(BufferedImage image, int x, int y, int w, int h) {
        int[] pixels = image.getRGB(x, y, w, h, null, 0, w);
        return new Sprite(w, h, pixels);
    }
}
