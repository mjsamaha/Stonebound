package com.lobsterchops.stonebound.game.engine.audio;

import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.lobsterchops.stonebound.game.engine.util.Logger;


public class AudioServiceImplementation implements AudioService {

    private final AudioCatalog catalog;

    private final Map<AudioCategory, Float> volumes = new EnumMap<>(AudioCategory.class);

    private final Map<String, Clip> activeClips = new HashMap<>();

    private Clip currentMusic;

    public AudioServiceImplementation(AudioCatalog catalog) {
        this.catalog = catalog;

        for (AudioCategory cat : AudioCategory.values()) {
            volumes.put(cat, 1f);
        }
    }

    @Override
    public void play(String soundId) {
        SoundDefinition def = catalog.get(soundId);

        try {
            Clip clip = createClip(def.getResourcePath());
            if (clip == null) return;

            float vol = getEffectiveVolume(def);
            applyVolume(clip, vol);

            if (def.isLooping()) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                currentMusic = clip;
                activeClips.put(soundId, clip);  // ← also track by ID so stop() can find it
            } else {
                clip.start();
                activeClips.put(soundId, clip);
            }

            Logger.info("[Audio] play: " + def.getResourcePath());

        } catch (Exception e) {
            Logger.log("Audio play failed: " + e.getMessage(), soundId);
        }
    }

    @Override
    public void stop(String soundId) {
        Clip clip = activeClips.remove(soundId);

        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    @Override
    public void stopAll() {

        for (Clip clip : activeClips.values()) {
            clip.stop();
            clip.close();
        }

        activeClips.clear();

        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.close();
            currentMusic = null;
        }
    }

    @Override
    public void setVolume(AudioCategory category, float volume) {
        volumes.put(category, AudioMath.clampVolume(volume));
    }

    @Override
    public float getVolume(AudioCategory category) {
        return volumes.getOrDefault(category, 1f);
    }

    private float getEffectiveVolume(SoundDefinition def) {
        return volumes.get(def.getCategory()) * def.getBaseVolume();
    }

    private Clip createClip(String path)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        URL url = getClass().getResource(path);

        if (url == null) {
            Logger.info("[Audio] Missing file: " + path);
            return null;
        }

        AudioInputStream stream = AudioSystem.getAudioInputStream(url);

        Clip clip = AudioSystem.getClip();
        clip.open(stream);

        return clip;
    }

    private void applyVolume(Clip clip, float volume) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;

        FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        float db = AudioMath.linearToDecibels(volume);
        gain.setValue(db);
    }

	@Override
	public void init() {
		
	}

	@Override
	public void shutdown() {
		
	}

	@Override
	public void update() {
		
	}
}