package com.lobsterchops.stonebound.game.ui.core;

import java.awt.Graphics2D;

public interface UILayer {
	
	void update();
	
	void render(Graphics2D g2);
	
	default boolean isVisible() {
		return true;
	}

}
