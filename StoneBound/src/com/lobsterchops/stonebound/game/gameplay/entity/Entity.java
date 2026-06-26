package com.lobsterchops.stonebound.game.gameplay.entity;

import java.awt.Graphics2D;
import java.util.UUID;

import com.lobsterchops.stonebound.game.config.types.EntityType;
import com.lobsterchops.stonebound.game.engine.math.Vector2;

public abstract class Entity {

    protected final String     id;
    protected final EntityType type;
    protected       Vector2    position;
    protected       boolean    active = true;

    protected Entity(EntityType type, float x, float y) {
        this.id       = UUID.randomUUID().toString();
        this.type     = type;
        this.position = new Vector2(x, y);
    }

    public abstract void update();
    public abstract void render(Graphics2D g2);

    public String     getId()       { return id; }
    public EntityType getType()     { return type; }
    public Vector2    getPosition() { return position; }
    public boolean    isActive()    { return active; }
    public void       setActive(boolean active) { this.active = active; }
}
