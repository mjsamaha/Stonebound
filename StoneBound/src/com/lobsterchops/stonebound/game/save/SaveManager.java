package com.lobsterchops.stonebound.game.save;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.lobsterchops.stonebound.game.engine.util.FileUtil;
import com.lobsterchops.stonebound.game.engine.util.Logger;


public class SaveManager {

    private static final Path SAVE_DIR = Paths.get(
            System.getProperty("user.home"), ".stonebound", "saves");

    public void save(SaveFile file) {
        Path dest = SAVE_DIR.resolve("slot" + file.getSlot() + ".sav");
        try {
            FileUtil.writeText(dest, toJson(file));
            Logger.info("Saved to " + dest);
        } catch (IOException e) {
            Logger.error("Failed to save: " + e.getMessage(), e);
        }
    }

    public SaveFile load(int slot) {
        Path src = SAVE_DIR.resolve("slot" + slot + ".sav");
        if (!FileUtil.exists(src)) return null;
        try {
            String json = FileUtil.readText(src);
            return fromJson(slot, json);
        } catch (IOException e) {
            Logger.error("Failed to load slot " + slot + ": " + e.getMessage(), e);
            return null;
        }
    }


    private String toJson(SaveFile file) {
        // TODO: replace with Jackson / Gson / manual JSON
        return "{\"slot\":" + file.getSlot()
             + ",\"playTime\":" + file.getPlayTimeSeconds() + "}";
    }

    private SaveFile fromJson(int slot, String json) {
        // TODO: proper parsing
        return new SaveFile(slot);
    }
}