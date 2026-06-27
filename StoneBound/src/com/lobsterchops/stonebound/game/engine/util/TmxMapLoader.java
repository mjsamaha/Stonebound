package com.lobsterchops.stonebound.game.engine.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lobsterchops.stonebound.game.gameplay.world.TileLayer;
import com.lobsterchops.stonebound.game.gameplay.world.TilesetDef;
import com.lobsterchops.stonebound.game.gameplay.world.TmxMap;

/**
 * Lightweight TMX loader (orthogonal + CSV tile layers + external/internal tilesets).
 */
public final class TmxMapLoader {

    public TmxMap load(String tmxPath) {
        if (tmxPath == null || tmxPath.isBlank()) {
            throw new IllegalArgumentException("tmxPath is required");
        }

        File tmxFile = new File(tmxPath);
        if (!tmxFile.exists()) {
            throw new IllegalArgumentException("TMX not found: " + tmxPath);
        }

        try {
            Document doc = parseXml(tmxFile);
            Element mapEl = doc.getDocumentElement();
            if (mapEl == null || !"map".equals(mapEl.getTagName())) {
                throw new IllegalArgumentException("Invalid TMX: root <map> not found in " + tmxPath);
            }

            String orientation = requireAttr(mapEl, "orientation", "map");
            if (!"orthogonal".equalsIgnoreCase(orientation)) {
                throw new IllegalArgumentException("Unsupported orientation '" + orientation + "' in " + tmxPath + ". Only orthogonal is supported.");
            }

            int mapWidth = parsePositiveInt(requireAttr(mapEl, "width", "map"), "map width");
            int mapHeight = parsePositiveInt(requireAttr(mapEl, "height", "map"), "map height");
            int tileWidth = parsePositiveInt(requireAttr(mapEl, "tilewidth", "map"), "map tilewidth");
            int tileHeight = parsePositiveInt(requireAttr(mapEl, "tileheight", "map"), "map tileheight");

            List<TilesetDef> tilesets = parseTilesets(mapEl, tmxFile, tileWidth, tileHeight);
            List<TileLayer> layers = parseLayers(mapEl, mapWidth, mapHeight, tmxPath);

            String mapName = tmxFile.getName();
            return new TmxMap(mapName, mapWidth, mapHeight, tileWidth, tileHeight, tilesets, layers);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to load TMX '" + tmxPath + "': " + ex.getMessage(), ex);
        }
    }

    private static Document parseXml(File file) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        dbf.setIgnoringComments(true);
        dbf.setIgnoringElementContentWhitespace(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        return doc;
    }

    private static List<TilesetDef> parseTilesets(Element mapEl, File tmxFile, int mapTileWidth, int mapTileHeight) throws IOException {
        NodeList nodes = mapEl.getElementsByTagName("tileset");
        List<TilesetDef> out = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if (n.getNodeType() != Node.ELEMENT_NODE) continue;
            Element tilesetEl = (Element) n;
            int firstGid = parsePositiveInt(requireAttr(tilesetEl, "firstgid", "tileset"), "tileset firstgid");

            if (tilesetEl.hasAttribute("source")) {
                String source = requireAttr(tilesetEl, "source", "tileset");
                File tsxFile = resolveRelativeFile(tmxFile, source);
                if (!tsxFile.exists()) {
                    throw new IllegalArgumentException("TSX source not found: " + tsxFile.getPath());
                }
                Document tsxDoc;
                try {
                    tsxDoc = parseXml(tsxFile);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Failed to parse TSX '" + tsxFile.getPath() + "': " + ex.getMessage(), ex);
                }
                Element tsxTileset = tsxDoc.getDocumentElement();
                if (tsxTileset == null || !"tileset".equals(tsxTileset.getTagName())) {
                    throw new IllegalArgumentException("Invalid TSX: root <tileset> missing in " + tsxFile.getPath());
                }
                out.add(buildTilesetDef(firstGid, tsxTileset, tsxFile, mapTileWidth, mapTileHeight));
            } else {
                out.add(buildTilesetDef(firstGid, tilesetEl, tmxFile, mapTileWidth, mapTileHeight));
            }
        }

        if (out.isEmpty()) {
            throw new IllegalArgumentException("TMX contains no <tileset> elements");
        }

        out.sort(Comparator.comparingInt(TilesetDef::getFirstGid));
        return out;
    }

    private static TilesetDef buildTilesetDef(int firstGid, Element tilesetEl, File ownerFile, int mapTileWidth, int mapTileHeight) {
        int tileWidth = parsePositiveInt(getAttrOrDefault(tilesetEl, "tilewidth", Integer.toString(mapTileWidth)), "tileset tilewidth");
        int tileHeight = parsePositiveInt(getAttrOrDefault(tilesetEl, "tileheight", Integer.toString(mapTileHeight)), "tileset tileheight");
        int spacing = parseNonNegativeInt(getAttrOrDefault(tilesetEl, "spacing", "0"), "tileset spacing");
        int margin = parseNonNegativeInt(getAttrOrDefault(tilesetEl, "margin", "0"), "tileset margin");

        int tileCount = parsePositiveInt(requireAttr(tilesetEl, "tilecount", "tileset"), "tileset tilecount");
        int columns = parsePositiveInt(requireAttr(tilesetEl, "columns", "tileset"), "tileset columns");

        NodeList images = tilesetEl.getElementsByTagName("image");
        if (images.getLength() == 0) {
            throw new IllegalArgumentException("Tileset missing <image> element in " + ownerFile.getPath());
        }
        Element imageEl = (Element) images.item(0);
        String imageSourceRaw = requireAttr(imageEl, "source", "image");
        int imageWidth = parsePositiveInt(requireAttr(imageEl, "width", "image"), "tileset image width");
        int imageHeight = parsePositiveInt(requireAttr(imageEl, "height", "image"), "tileset image height");

        File imageFile = resolveRelativeFile(ownerFile, imageSourceRaw);
        String normalizedImagePath = imageFile.getPath().replace('\\', '/');

        return new TilesetDef(
                firstGid,
                normalizedImagePath,
                imageWidth,
                imageHeight,
                tileWidth,
                tileHeight,
                columns,
                tileCount,
                spacing,
                margin
        );
    }

    private static List<TileLayer> parseLayers(Element mapEl, int mapWidth, int mapHeight, String tmxPath) {
        NodeList nodes = mapEl.getElementsByTagName("layer");
        List<TileLayer> out = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if (n.getNodeType() != Node.ELEMENT_NODE) continue;
            Element layerEl = (Element) n;

            String name = getAttrOrDefault(layerEl, "name", "Layer" + i);
            int layerWidth = parsePositiveInt(getAttrOrDefault(layerEl, "width", Integer.toString(mapWidth)), "layer width");
            int layerHeight = parsePositiveInt(getAttrOrDefault(layerEl, "height", Integer.toString(mapHeight)), "layer height");
            if (layerWidth != mapWidth || layerHeight != mapHeight) {
                throw new IllegalArgumentException("Layer '" + name + "' size differs from map size in " + tmxPath);
            }

            boolean visible = !"0".equals(getAttrOrDefault(layerEl, "visible", "1"));
            float opacity = parseOpacity(getAttrOrDefault(layerEl, "opacity", "1"), name);

            Element dataEl = firstChildElementByTag(layerEl, "data");
            if (dataEl == null) {
                throw new IllegalArgumentException("Layer '" + name + "' missing <data> in " + tmxPath);
            }

            String encoding = getAttrOrDefault(dataEl, "encoding", "");
            if (!"csv".equalsIgnoreCase(encoding)) {
                throw new IllegalArgumentException("Layer '" + name + "' must use CSV encoding in " + tmxPath);
            }

            String rawCsv = dataEl.getTextContent();
            int[] gids = parseCsvGids(rawCsv, layerWidth * layerHeight, name, tmxPath);
            out.add(new TileLayer(name, layerWidth, layerHeight, visible, opacity, gids));
        }

        if (out.isEmpty()) {
            throw new IllegalArgumentException("TMX contains no <layer> elements: " + tmxPath);
        }

        return out;
    }

    private static int[] parseCsvGids(String csv, int expectedCount, String layerName, String tmxPath) {
        if (csv == null) csv = "";
        String[] tokens = csv.trim().split(",");
        List<Integer> values = new ArrayList<>(tokens.length);
        for (String token : tokens) {
            String t = token.trim();
            if (t.isEmpty()) continue;
            try {
                long parsed = Long.parseLong(t);
                if (parsed < 0 || parsed > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("Layer '" + layerName + "' has invalid gid '" + t + "' in " + tmxPath);
                }
                values.add((int) parsed);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Layer '" + layerName + "' has non-integer gid '" + t + "' in " + tmxPath, ex);
            }
        }

        if (values.size() != expectedCount) {
            throw new IllegalArgumentException("Layer '" + layerName + "' expected " + expectedCount + " gids, found " + values.size() + " in " + tmxPath);
        }

        int[] out = new int[expectedCount];
        for (int i = 0; i < expectedCount; i++) out[i] = values.get(i);
        return out;
    }

    private static Element firstChildElementByTag(Element parent, String tag) {
        NodeList children = parent.getElementsByTagName(tag);
        if (children.getLength() == 0) return null;
        return (Element) children.item(0);
    }

    private static String requireAttr(Element el, String attr, String elementName) {
        String v = el.getAttribute(attr);
        if (v == null || v.isBlank()) {
            throw new IllegalArgumentException("Missing required attribute '" + attr + "' on <" + elementName + ">");
        }
        return v;
    }

    private static String getAttrOrDefault(Element el, String attr, String defaultValue) {
        String v = el.getAttribute(attr);
        return (v == null || v.isBlank()) ? defaultValue : v;
    }

    private static int parsePositiveInt(String s, String label) {
        try {
            int value = Integer.parseInt(s.trim());
            if (value <= 0) throw new IllegalArgumentException(label + " must be > 0: " + s);
            return value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " must be an integer: " + s, ex);
        }
    }

    private static int parseNonNegativeInt(String s, String label) {
        try {
            int value = Integer.parseInt(s.trim());
            if (value < 0) throw new IllegalArgumentException(label + " must be >= 0: " + s);
            return value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " must be an integer: " + s, ex);
        }
    }

    private static float parseOpacity(String s, String layerName) {
        try {
            float v = Float.parseFloat(s.trim());
            if (v < 0f || v > 1f) {
                throw new IllegalArgumentException("Layer '" + layerName + "' opacity out of range [0..1]: " + s);
            }
            return v;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Layer '" + layerName + "' opacity is not a float: " + s, ex);
        }
    }

    private static File resolveRelativeFile(File ownerFile, String relative) {
        File parent = ownerFile.getParentFile();
        if (parent == null) return new File(relative);
        return new File(parent, relative).getAbsoluteFile();
    }
}
