package com.lobsterchops.stonebound.game.engine.audio.content;

import com.lobsterchops.stonebound.game.engine.audio.AudioCatalog;
import com.lobsterchops.stonebound.game.engine.audio.AudioCategory;
import com.lobsterchops.stonebound.game.engine.audio.SoundDefinition;
import com.lobsterchops.stonebound.game.engine.audio.SoundType;

public final class GameAudio {
	
	
	public static void registerAll(AudioCatalog catalog) {
		
		catalog.register(new SoundDefinition(
				SoundIds.UI_CONFIRM, "/audio/sfx/ui_confirm.wav", SoundType.SFX, AudioCategory.SFX, 1.0f, false));
		
		catalog.register(new SoundDefinition(
				SoundIds.UI_CANCEL, "/audio/sfx/ui_cancel.wav", SoundType.SFX, AudioCategory.SFX, 1.0f, false));
	
		catalog.register(new SoundDefinition
				(SoundIds.PRE_LOADER_MUSIC, "/audio/music/preloader_music.wav", SoundType.MUSIC, AudioCategory.MUSIC, 1.0f, true));
		
		catalog.register(new SoundDefinition
				(SoundIds.GAMEPLAY_MUSIC_ONE, "/audio/music/gameplay_music_one.wav", SoundType.MUSIC, AudioCategory.MUSIC, 1.0f, true));
		
	}
	
	private GameAudio() {
	}
	
	
	
	/**
	 * After startup:
	 * 
	 * AudioCatalog catalog = new AudioCatalog();
	 * GameSounds.registerAll(catalog);
	 * 
	 * AudioService audioService = new AudioServiceImplementation(catalog);
	 * 
	 * 
	 * Playing sfx/music:
	 * 
	 * audioService.play(SoundIds.GAMEPLAY_MUSIC);
	 * 
	 */
	

}
