package com.lobsterchops.stonebound.game.engine.gfx;

public interface Renderable {
	
	RenderLayerKey getLayer(); // which layer to submit
	 
	void render(Renderer renderer); // the draw call

}
