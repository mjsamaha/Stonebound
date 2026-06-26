package com.lobsterchops.stonebound.game.gameplay.ai;

import java.util.ArrayList;
import java.util.List;

import com.lobsterchops.stonebound.game.gameplay.entity.LivingEntity;


public class AISystem {

    private final List<LivingEntity> agents = new ArrayList<>();

    public void register(LivingEntity entity) { agents.add(entity); }
    public void remove(LivingEntity entity)   { agents.remove(entity); }

    public void update() {
        for (LivingEntity agent : agents) {
            if (agent.isActive()) {
                // TODO: run behaviour tree / state machine
            }
        }
    }
}
