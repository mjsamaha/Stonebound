package com.lobsterchops.stonebound.game.engine.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {

    @SuppressWarnings("rawtypes")
    private final Map<Class<?>, List<Listener>> listeners = new HashMap<>();

    public <E extends Event> void subscribe(Class<E> type, Listener<E> listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    public <E extends Event> void unsubscribe(Class<E> type, Listener<E> listener) {
        List<?> list = listeners.get(type);
        if (list != null) list.remove(listener);
    }

    @SuppressWarnings("unchecked")
    public <E extends Event> void publish(E event) {
        List<Listener> list = listeners.get(event.getClass());
        if (list == null) return;
        for (Listener l : list) {
            if (event.isConsumed()) break;
            l.onEvent(event);
        }
    }

    public void clear() { listeners.clear(); }
}
