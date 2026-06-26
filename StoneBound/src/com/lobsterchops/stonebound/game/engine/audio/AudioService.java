package com.lobsterchops.stonebound.game.engine.audio;

public interface AudioService {

    void init();

    void shutdown();

    void update();

    void play(String soundId);

    void stop(String soundId);

    void stopAll();

    void setVolume(AudioCategory category, float volume);

    float getVolume(AudioCategory category);
}