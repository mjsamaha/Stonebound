package com.lobsterchops.stonebound.game.engine.util;

import com.lobsterchops.stonebound.game.config.GameConfig;

public class FpsCounter {
	
	private long timer = 0L;
	private int frameCount = 0;
	
	public void frame(long elapsedNanos) {
		timer += elapsedNanos;
		frameCount++;
	}
	
	public boolean shouldUpdate() {
        return timer >= GameConfig.TIMER_INTERVAL;
    }

    public int consumeFps() {
        int fps = frameCount;
        frameCount = 0;
        timer = 0L;
        return fps;
    }

}
