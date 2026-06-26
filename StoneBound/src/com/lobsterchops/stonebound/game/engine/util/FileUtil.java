package com.lobsterchops.stonebound.game.engine.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtil {

    private FileUtil() {}

    public static String readText(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    public static void writeText(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, content, StandardCharsets.UTF_8);
    }

    public static byte[] readBytes(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    public static void writeBytes(Path path, byte[] data) throws IOException {
        Files.createDirectories(path.getParent());
        Files.write(path, data);
    }

    public static boolean exists(Path path) {
        return Files.exists(path);
    }
}
