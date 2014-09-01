package io.github.aritzhack.aritzh.lwjgl.graphics;

import io.github.aritzhack.aritzh.lwjgl.util.BufferUtils;
import io.github.aritzhack.aritzh.lwjgl.util.GraphicsUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

/**
 * @author Aritz Lopez
 */
public class Texture {

    private int width, height;
    private int texId;

    public Texture(String path) {
        this.texId = load(path);
    }

    public int load(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        width = image.getWidth();
        height = image.getHeight();

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        int[] data = new int[width * height];
        for (int i = 0; i < pixels.length; i++) {
            data[i] = GraphicsUtils.ARGBtoRGBA(pixels[i]);
        }

        int tex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tex);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.toIntBuffer(data));
        glBindTexture(GL_TEXTURE_2D, 0);

        return tex;
    }
}
