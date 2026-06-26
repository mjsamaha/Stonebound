package com.lobsterchops.stonebound.game.ui.core;

import java.awt.Graphics2D;

public abstract class Overlay {
	
	protected boolean visible = false;
	
	public void show() {
		visible = true;
		onShow();
	}
	
	public void hide() {
		visible = false;
		onHide();
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	protected void onShow() {
		
	}
	
	protected void onHide() {
		
	}
	
	public abstract void update();
	
	public abstract void render(Graphics2D g2);

}
