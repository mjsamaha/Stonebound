package com.lobsterchops.stonebound.game.gameplay.world;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Central registry from logical map IDs to TMX resource paths.
 * Keep this independent from rendering/loading code.
 */
public final class MapRegistry {

    private static final Map<MapId, String> TMX_PATHS;

    static {
        EnumMap<MapId, String> m = new EnumMap<>(MapId.class);
        m.put(MapId.START_MAP, "res/gfx/maps/map01.tmx");
        TMX_PATHS = Collections.unmodifiableMap(m);
    }

    private MapRegistry() {
    }

    public static String pathFor(MapId id) {
        String path = TMX_PATHS.get(id);
        if (path == null) {
            throw new IllegalArgumentException("No TMX path registered for map id: " + id);
        }
        return path;
    }

    public static MapId getStartupMapId() {
        return MapId.START_MAP;
    }

    public static boolean has(MapId id) {
        return TMX_PATHS.containsKey(id);
    }

    public static Map<MapId, String> all() {
        return TMX_PATHS;
    }
}