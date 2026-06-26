package com.lobsterchops.stonebound.game.gameplay.world;

import java.io.InputStream;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.lobsterchops.stonebound.game.engine.util.Logger;

/**
 * Parses a Tiled .tmx file (XML format) and populates a {@link World}
 * with tile IDs read from the first tile layer.
 *
 * <p>Supports TMX encoding: CSV and Base64 (uncompressed, gzip, zlib).
 * Export from Tiled with: Map → Properties → Tile Layer Format = CSV
 * for the simplest setup.
 *
 * <p>Tile size in Tiled MUST match {@link com.lobsterchops.stonebound.game.config.GameConfig#TILE_SIZE}.
 */
public class TmxLoader {

    /**
     * Loads a .tmx file from the classpath and populates the given world.
     *
     * @param resourcePath classpath path e.g. "/maps/world1.tmx"
     * @param world        the world to populate
     */
    public static void load(String resourcePath, World world) {
        try (InputStream is = TmxLoader.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                Logger.error("TMX file not found: " + resourcePath);
                return;
            }

            DocumentBuilder builder = DocumentBuilderFactory
                    .newInstance().newDocumentBuilder();
            Document doc = builder.parse(is);
            doc.getDocumentElement().normalize();

            // Read map dimensions
            Element mapEl = doc.getDocumentElement();
            int mapWidth  = Integer.parseInt(mapEl.getAttribute("width"));   // in tiles
            int mapHeight = Integer.parseInt(mapEl.getAttribute("height"));  // in tiles

            // Find the first tile layer
            NodeList layers = doc.getElementsByTagName("layer");
            if (layers.getLength() == 0) {
                Logger.warn("TMX has no tile layers: " + resourcePath);
                return;
            }

            Element layer = (Element) layers.item(0);
            Element dataEl = (Element) layer
                    .getElementsByTagName("data").item(0);

            String encoding    = dataEl.getAttribute("encoding");
            String compression = dataEl.getAttribute("compression");
            String rawData     = dataEl.getTextContent().trim();

            int[] tileIds = decode(rawData, encoding, compression,
                                   mapWidth, mapHeight);

            // Write IDs into world chunks
            for (int y = 0; y < mapHeight; y++) {
                for (int x = 0; x < mapWidth; x++) {
                    // Tiled GIDs are 1-based; 0 means empty. Shift to 0-based.
                    int gid = tileIds[y * mapWidth + x];
                    int id  = gid == 0 ? 0 : gid - 1;
                    world.setTileId(x, y, id);
                }
            }

            Logger.info("Loaded TMX: " + resourcePath
                      + " (" + mapWidth + "×" + mapHeight + " tiles)");

        } catch (Exception e) {
            Logger.error("Failed to load TMX: " + resourcePath, e);
        }
    }

    private static int[] decode(String data, String encoding,
                                String compression,
                                int width, int height) throws Exception {
        int total = width * height;

        if ("csv".equals(encoding)) {
            String[] parts = data.split(",");
            int[] ids = new int[total];
            for (int i = 0; i < total; i++) {
                ids[i] = Integer.parseInt(parts[i].trim());
            }
            return ids;
        }

        if ("base64".equals(encoding)) {
            byte[] bytes = Base64.getDecoder().decode(data);

            InputStream byteStream = new java.io.ByteArrayInputStream(bytes);
            InputStream dataStream = switch (compression) {
                case "gzip"  -> new GZIPInputStream(byteStream);
                case "zlib"  -> new InflaterInputStream(byteStream);
                default      -> byteStream; // uncompressed
            };

            // Each tile ID is a 4-byte little-endian int
            byte[] buf = dataStream.readAllBytes();
            int[] ids  = new int[total];
            for (int i = 0; i < total; i++) {
                ids[i] = (buf[i*4]     & 0xFF)
                       | (buf[i*4 + 1] & 0xFF) << 8
                       | (buf[i*4 + 2] & 0xFF) << 16
                       | (buf[i*4 + 3] & 0xFF) << 24;
            }
            return ids;
        }

        throw new IllegalArgumentException("Unsupported TMX encoding: " + encoding);
    }
}