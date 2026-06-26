package com.lobsterchops.stonebound.game.gameplay.entity;

import java.awt.Graphics2D;
import java.util.UUID;

import com.lobsterchops.stonebound.game.config.types.EntityType;
import com.lobsterchops.stonebound.game.engine.gfx.RenderLayerKey;
import com.lobsterchops.stonebound.game.engine.gfx.Renderable;
import com.lobsterchops.stonebound.game.engine.gfx.Renderer;
import com.lobsterchops.stonebound.game.engine.math.Vector2;

public abstract class Entity implements Renderable {
	 
    protected final String     id;
    protected final EntityType type;
    protected       Vector2    position;
    protected       boolean    active = true;
 
    protected Entity(EntityType type, float x, float y) {
        this.id       = UUID.randomUUID().toString();
        this.type     = type;
        this.position = new Vector2(x, y);
    }
 
 
    /** Advances this entity's logic by one game tick. */
    public abstract void update();
 
    /**
     * Draws this entity through the renderer API.
     *
     * <p>Implementors must NOT call {@code renderer.getRaw().dispose()} and must
     * NOT store the renderer reference beyond this call.
     *
     * @param renderer the active renderer for this frame; never null
     */
    @Override
    public abstract void render(Renderer renderer);
 
 
    /**
     * Returns {@link RenderLayerKey#ENTITIES} by default.
     * Override in subclasses that need a different layer.
     */
    @Override
    public RenderLayerKey getRenderLayer() {
        return RenderLayerKey.ENTITIES;
    }
 
 
    public String     getId()       { return id; }
    public EntityType getType()     { return type; }
    public Vector2    getPosition() { return position; }
    public boolean    isActive()    { return active; }
    public void       setActive(boolean active) { this.active = active; }
}
 