package com.lobsterchops.stonebound.game.engine.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;


public final class ResourceLoader {

    private ResourceLoader() {}

    public static BufferedImage loadImage(String path) {
        URL url = ResourceLoader.class.getResource(path);
        if (url == null) {
            throw new RuntimeException("Resource not found: " + path);
        }
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image: " + path, e);
        }
    }

    public static InputStream loadStream(String path) {
        InputStream is = ResourceLoader.class.getResourceAsStream(path);
        if (is == null) {
            throw new RuntimeException("Resource stream not found: " + path);
        }
        return is;
    }
}
