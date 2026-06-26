package com.lobsterchops.stonebound.game.save;

import java.time.LocalDateTime;

public class SaveFile {

    private final int           slot;
    private final LocalDateTime savedAt;
    private       int           playTimeSeconds;

    // TODO: add WorldState, PlayerState, etc.

    public SaveFile(int slot) {
        this.slot    = slot;
        this.savedAt = LocalDateTime.now();
    }

    public int           getSlot()            { return slot; }
    public LocalDateTime getSavedAt()         { return savedAt; }
    public int           getPlayTimeSeconds() { return playTimeSeconds; }
    public void          setPlayTimeSeconds(int s) { this.playTimeSeconds = s; }
}
