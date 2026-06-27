package com.lobsterchops.stonebound.game.gameplay.world;

/**
 * Immutable tileset metadata resolved from TMX/TSX.
 */
public final class TilesetDef {

	private final int firstGid;
	private final String imageSource;
	private final int imageWidth;
	private final int imageHeight;
	private final int tileWidth;
	private final int tileHeight;
	private final int columns;
	private final int tileCount;
	private final int spacing;
	private final int margin;

	public TilesetDef(int firstGid, String imageSource, int imageWidth, int imageHeight, int tileWidth, int tileHeight,
			int columns, int tileCount, int spacing, int margin) {
		if (firstGid < 1)
			throw new IllegalArgumentException("firstGid must be >= 1");
		if (imageSource == null || imageSource.isBlank())
			throw new IllegalArgumentException("imageSource is required");
		if (imageWidth <= 0 || imageHeight <= 0)
			throw new IllegalArgumentException("image size must be > 0");
		if (tileWidth <= 0 || tileHeight <= 0)
			throw new IllegalArgumentException("tile size must be > 0");
		if (columns <= 0)
			throw new IllegalArgumentException("columns must be > 0");
		if (tileCount <= 0)
			throw new IllegalArgumentException("tileCount must be > 0");
		if (spacing < 0 || margin < 0)
			throw new IllegalArgumentException("spacing/margin must be >= 0");

		this.firstGid = firstGid;
		this.imageSource = imageSource;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.columns = columns;
		this.tileCount = tileCount;
		this.spacing = spacing;
		this.margin = margin;
	}

	public int getFirstGid() {
		return firstGid;
	}

	public String getImageSource() {
		return imageSource;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public int getColumns() {
		return columns;
	}

	public int getTileCount() {
		return tileCount;
	}

	public int getSpacing() {
		return spacing;
	}

	public int getMargin() {
		return margin;
	}

	public int getLastGidInclusive() {
		return firstGid + tileCount - 1;
	}

	public boolean containsGid(int gid) {
		return gid >= firstGid && gid <= getLastGidInclusive();
	}

	@Override
	public String toString() {
		return "TilesetDef{" + "firstGid=" + firstGid + ", imageSource='" + imageSource + '\'' + ", image=" + imageWidth
				+ "x" + imageHeight + ", tile=" + tileWidth + "x" + tileHeight + ", columns=" + columns + ", tileCount="
				+ tileCount + ", spacing=" + spacing + ", margin=" + margin + '}';
	}
}
