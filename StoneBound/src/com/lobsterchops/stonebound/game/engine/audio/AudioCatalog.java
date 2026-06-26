package com.lobsterchops.stonebound.game.engine.audio;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AudioCatalog {

    private final Map<String, SoundDefinition> definitions = new HashMap<>();
    
    public void register(SoundDefinition def) {
        if (definitions.containsKey(def.getId())) {
            throw new IllegalStateException("Duplicate sound id: " + def.getId());
        }
        definitions.put(def.getId(), def);
    }

    public SoundDefinition get(String id) {
        SoundDefinition def = definitions.get(id);
        if (def == null) throw new IllegalArgumentException("Unknown sound: " + id);
        return def;
    }

    public Map<String, SoundDefinition> all() {
        return Collections.unmodifiableMap(definitions);
    }
}
