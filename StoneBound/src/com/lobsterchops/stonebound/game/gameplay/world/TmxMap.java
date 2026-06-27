package com.lobsterchops.stonebound.game.gameplay.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Immutable map contract for orthogonal tile maps.
 */
public final class TmxMap {

	private final String mapName;
	private final int widthInTiles;
	private final int heightInTiles;
	private final int tileWidth;
	private final int tileHeight;
	private final List<TilesetDef> tilesets;
	private final List<TileLayer> layers;

	public TmxMap(String mapName, int widthInTiles, int heightInTiles, int tileWidth, int tileHeight,
			List<TilesetDef> tilesets, List<TileLayer> layers) {
		if (mapName == null || mapName.isBlank())
			throw new IllegalArgumentException("mapName is required");
		if (widthInTiles <= 0 || heightInTiles <= 0)
			throw new IllegalArgumentException("map size must be > 0");
		if (tileWidth <= 0 || tileHeight <= 0)
			throw new IllegalArgumentException("tile size must be > 0");
		if (tilesets == null || tilesets.isEmpty())
			throw new IllegalArgumentException("tilesets required");
		if (layers == null || layers.isEmpty())
			throw new IllegalArgumentException("layers required");

		this.mapName = mapName;
		this.widthInTiles = widthInTiles;
		this.heightInTiles = heightInTiles;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.tilesets = Collections.unmodifiableList(new ArrayList<>(tilesets));
		this.layers = Collections.unmodifiableList(new ArrayList<>(layers));
	}

	public String getMapName() {
		return mapName;
	}

	public int getWidthInTiles() {
		return widthInTiles;
	}

	public int getHeightInTiles() {
		return heightInTiles;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public int getWidthInPixels() {
		return widthInTiles * tileWidth;
	}

	public int getHeightInPixels() {
		return heightInTiles * tileHeight;
	}

	public List<TilesetDef> getTilesets() {
		return tilesets;
	}

	public List<TileLayer> getLayers() {
		return layers;
	}

	/**
	 * Resolves a tileset for a raw GID (0 = empty/no tile).
	 */
	public TilesetDef findTilesetForGid(int gid) {
		if (gid <= 0)
			return null;
		TilesetDef best = null;
		for (TilesetDef ts : tilesets) {
			if (ts.getFirstGid() <= gid && (best == null || ts.getFirstGid() > best.getFirstGid())) {
				best = ts;
			}
		}
		return best;
	}

	@Override
	public String toString() {
		return "TmxMap{" + "mapName='" + mapName + '\'' + ", sizeTiles=" + widthInTiles + "x" + heightInTiles
				+ ", tileSize=" + tileWidth + "x" + tileHeight + ", tilesets=" + tilesets.size() + ", layers="
				+ layers.size() + '}';
	}
}