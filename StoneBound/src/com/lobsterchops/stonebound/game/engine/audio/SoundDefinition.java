package com.lobsterchops.stonebound.game.engine.audio;

public class SoundDefinition {

	private final String id;
	private final String resourcePath;
	private final SoundType type;
	private final AudioCategory category;
	private final float baseVolume;
	private final boolean looping;

	public SoundDefinition(String id, String resourcePath, SoundType type, AudioCategory category, float baseVolume,
			boolean looping) {
		this.id = id;
		this.resourcePath = resourcePath;
		this.type = type;
		this.category = category;
		this.baseVolume = baseVolume;
		this.looping = looping;
	}

	public String getId() {
		return id;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public SoundType getType() {
		return type;
	}

	public AudioCategory getCategory() {
		return category;
	}

	public float getBaseVolume() {
		return baseVolume;
	}

	public boolean isLooping() {
		return looping;
	}
}
