package com.lobsterchops.stonebound.game.ui.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.lobsterchops.stonebound.game.core.ServiceLocator;
import com.lobsterchops.stonebound.game.engine.util.DebugMetrics;

public class DebugOverlay implements UILayer {

	private static final Font FONT = new Font("Monospaced", Font.PLAIN, 12);
	private static final Color COLOR = Color.YELLOW;

	private boolean enabled = true;

	@Override
	public void update() {
		/* reads live from DebugMetrics */ }

	@Override
	public void render(Graphics2D g2) {
		if (!enabled)
			return;
		DebugMetrics metrics = ServiceLocator.resolve(DebugMetrics.class);
		g2.setFont(FONT);
		g2.setColor(COLOR);
		g2.drawString("FPS: " + metrics.getFps(), 10, 30);
	}

	@Override
	public boolean isVisible() {
		return enabled;
	}

	public void toggle() {
		enabled = !enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
