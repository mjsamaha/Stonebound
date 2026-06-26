package com.lobsterchops.stonebound.game.config;

public final class ScreenConfig {
	
	public static final int TILE_SIZE = 16;
	
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;
	
	public static final float CENTER_DIVISOR = 2f;
	
	public static final long DISPLAY_DURATION = 7_000_000_000L; // 5 seconds in nanoseconds
		
	public static final long BLINK_PERIOD_NANOS = 900_000_000L; // 900 ms

	private ScreenConfig() {
	}
}
