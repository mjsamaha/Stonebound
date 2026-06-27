package com.lobsterchops.stonebound.game.gameplay.world;

import java.util.Arrays;

/**
 * Immutable tile layer using row-major int[] GIDs. GID=0 means empty tile.
 */
public final class TileLayer {

	private final String name;
	private final int width;
	private final int height;
	private final boolean visible;
	private final float opacity;
	private final int[] gids;

	public TileLayer(String name, int width, int height, boolean visible, float opacity, int[] gids) {
		if (name == null || name.isBlank())
			throw new IllegalArgumentException("name is required");
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException("width/height must be > 0");
		if (opacity < 0f || opacity > 1f)
			throw new IllegalArgumentException("opacity must be in [0..1]");
		if (gids == null)
			throw new IllegalArgumentException("gids must not be null");
		if (gids.length != width * height) {
			throw new IllegalArgumentException("gids length must be width*height");
		}

		this.name = name;
		this.width = width;
		this.height = height;
		this.visible = visible;
		this.opacity = opacity;
		this.gids = Arrays.copyOf(gids, gids.length);
	}

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isVisible() {
		return visible;
	}

	public float getOpacity() {
		return opacity;
	}

	public int getGid(int tileX, int tileY) {
		if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height)
			return 0;
		return gids[(tileY * width) + tileX];
	}

	public int[] copyGids() {
		return Arrays.copyOf(gids, gids.length);
	}

	@Override
	public String toString() {
		return "TileLayer{" + "name='" + name + '\'' + ", size=" + width + "x" + height + ", visible=" + visible
				+ ", opacity=" + opacity + '}';
	}
}