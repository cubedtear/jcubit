package io.github.cubedtear.jcubit.awt.gameEngine.font;

import com.google.common.collect.Maps;
import io.github.cubedtear.jcubit.awt.render.IRender;
import io.github.cubedtear.jcubit.awt.render.Sprite;
import io.github.cubedtear.jcubit.awt.util.SpriteUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author Aritz Lopez
 */
public class Font {

    private Map<Character, Glyph> glyphs;
    private int size;
    private int lineHeight;
    private int base;
    private int color;

    public Font(Map<Character, Glyph> glyphs, int size, int lineHeight, int base) {
        this(glyphs, size, lineHeight, base, 0xFF000000);
    }

    public Font(Map<Character, Glyph> glyphs, int size, int lineHeight, int base, int color) {
        this.glyphs = glyphs;
        this.size = size;
        this.lineHeight = lineHeight;
        this.base = base;
        this.color = color;
    }

    public static Font loadFromClasspath(String fontPath) throws IOException {
        InputStream is = Font.class.getClassLoader().getResourceAsStream(fontPath);
        InputStream imageis = Font.class.getClassLoader().getResourceAsStream(fontPath + ".png");
        return load(is, imageis);
    }

    public static Font load(InputStream descriptor, InputStream image) throws IOException {
        BufferedImage img = ImageIO.read(image);
        BufferedReader br = new BufferedReader(new InputStreamReader(descriptor));


        Map<Character, Glyph> glyphs = Maps.newHashMap();
        int size = -1;
        int lineHeight = -1;
        int base = -1;

        String line;
        while ((line = br.readLine()) != null) {
            final String[] tokens = line.split(" ");
            if ("info".equalsIgnoreCase(tokens[0])) {
                for (int i = 1; i < tokens.length; i++) {
                    if (tokens[i].startsWith("size")) {
                        size = Integer.parseInt(tokens[i].split("=")[1]);
                        break;
                    }
                }
            } else if ("common".equalsIgnoreCase(tokens[0])) {
                for (int i = 1; i < tokens.length; i++) {
                    if (tokens[i].startsWith("lineHeight")) {
                        lineHeight = Integer.parseInt(tokens[i].split("=")[1]);
                        break;
                    } else if (tokens[i].startsWith("base")) {
                        base = Integer.parseInt(tokens[i].split("=")[1]);
                        break;
                    }
                }
            } else if ("char".equalsIgnoreCase(tokens[0])) {
                Sprite sprite;
                char ch = 0;
                int xoffset = -1;
                int yoffset = -1;
                int xadvance = -1;
                int x = -1;
                int y = -1;
                int width = -1;
                int height = -1;

                for (int i = 1; i < tokens.length; i++) {
                    if (tokens[i].startsWith("id=")) {
                        ch = (char) Integer.parseInt(tokens[i].split("=")[1]);
                    } else if (tokens[i].startsWith("x=")) {
                        x = Integer.parseInt(tokens[i].split("=")[1]);
                    } else if (tokens[i].startsWith("y=")) {
                        y = Integer.parseInt(tokens[i].split("=")[1]);
                    } else if (tokens[i].startsWith("width=")) {
                        width = Integer.parseInt(tokens[i].split("=")[1]);
                    } else if (tokens[i].startsWith("height=")) {
                        height = Integer.parseInt(tokens[i].split("=")[1]);
                    } else if (tokens[i].startsWith("xoffset=")) {
                        xoffset = Integer.parseInt(tokens[i].split("=")[1]);
                    } else if (tokens[i].startsWith("yoffset=")) {
                        yoffset = Integer.parseInt(tokens[i].split("=")[1]);
                    } else if (tokens[i].startsWith("xadvance=")) {
                        xadvance = Integer.parseInt(tokens[i].split("=")[1]);
                    }
                }
                int[] pixels = img.getRGB(x, y, width, height, null, 0, width);
                sprite = new Sprite(width, height, pixels);
                glyphs.put(ch, new Glyph(ch, xoffset, yoffset, xadvance, sprite));
            } else if ("kerning".equalsIgnoreCase(tokens[0])) {
                int first = -1;
                int second = -1;
                int amount = -1;
                for (int i = 1; i < tokens.length; i++) {
                    if (tokens[i].startsWith("first")) {
                        first = (char) Integer.parseInt(tokens[i].split("=")[1]);
                    } else if (tokens[i].startsWith("second")) {
                        second = Integer.parseInt(tokens[i].split("=")[1]);
                    } else if (tokens[i].startsWith("amount")) {
                        amount = Integer.parseInt(tokens[i].split("=")[1]);
                    }
                }
                glyphs.get((char) second).addKerning(glyphs.get((char) first), amount);
            }
        }
        return new Font(glyphs, size, lineHeight, base);
    }

    public Font getColorVariant(int newColor) {
        Map<Character, Glyph> newGlyphs = Maps.newHashMap();
        for (Map.Entry<Character, Glyph> e : this.glyphs.entrySet()) {
            final Glyph g = e.getValue();
            newGlyphs.put(e.getKey(), new Glyph(g.getCh(), g.getXoffset(), g.getYoffset(), g.getXadvance(), SpriteUtil.replaceColorIgnoreAlpha(g.getSprite(), this.color, newColor)));
        }
        return new Font(newGlyphs, size, lineHeight, base, newColor);
    }

    public void render(IRender render, String str, int x, int y) {
        render.setBlend(true);
        final char[] chars = str.toCharArray();
        Glyph previous = null;
        int xpos = x;
        int ypos = y;
        for (char ch : chars) {
            if (ch == '\n') {
                ypos += lineHeight;
                xpos = x;
                continue;
            }
            Glyph current = glyphs.get(ch);
            if (current == null) continue;
            final int kern = previous != null ? current.getKern(previous) : 0;
            render.draw(xpos + current.getXoffset() + kern, ypos + current.getYoffset(), current.getSprite());
            xpos += current.getXadvance() + kern;
            previous = current;
        }
        render.setBlend(false);
    }

    public int getStringWidth(String s) {
        int maxX = -1;
        final char[] chars = s.toCharArray();
        Glyph previous = null;
        int x = 0;
        // int y = 0; // For height too
        for (char ch : chars) {
            if (ch == '\n') {
                // y += lineHeight; // For height too
                x = 0;
                continue;
            }
            Glyph current = glyphs.get(ch);
            if (current == null) continue;
            final int kern = previous != null ? current.getKern(previous) : 0;
            x += current.getXadvance() + kern;
            previous = current;
            maxX = x > maxX ? x : maxX;
        }
        return maxX;
    }

    public int getSize() {
        return size;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public int getBase() {
        return base;
    }
}
