package com.lobsterchops.stonebound.game.ui.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.lobsterchops.stonebound.game.config.ScreenConfig;
import com.lobsterchops.stonebound.game.ui.core.Screen;
import com.lobsterchops.stonebound.game.ui.core.ScreenManager;

public class StoryScreen extends Screen {

	private static final long DISPLAY_DURATION_NANOS = 10_000_000_000L; // 9 s
	private static final long FADE_IN_NANOS = 1_500_000_000L; // 1.5 s text fade-in

	private static final Font STORY_FONT = new Font("Monospaced", Font.PLAIN, 16);
	private static final Font HINT_FONT = new Font("Monospaced", Font.PLAIN, 14);

	// Each entry is one line of the narration.
	private static final String[] LINES = { "The world was not always made of stone.", "",
			"An age ago, the Architects raised the Stonebound Gate —",
			"a structure said to hold the sky itself in place.", "", "No one knows who opened it.", "",
			"You wake at the edge of the ruins,", "carrying nothing but the weight of what came before.", };

	private long elapsed = 0L;
	private boolean transitionRequested = false;

	public StoryScreen(ScreenManager screenManager) {
		super(screenManager);
	}

	@Override
	public void onEnter() {
		elapsed = 0L;
		transitionRequested = false;
	}

	@Override
	public void update(long elapsedNanos) {

		elapsed += elapsedNanos;

		if (!transitionRequested && elapsed >= DISPLAY_DURATION_NANOS) {
			transitionRequested = true;
			screenManager.transitionTo(new GameScreen(screenManager));
		}

	}

	@Override
	public void render(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		float textAlpha = Math.min(1f, (float) elapsed / FADE_IN_NANOS);

		int centerX = ScreenConfig.WIDTH / 2;
		int startY = ScreenConfig.HEIGHT / 2 - ((LINES.length * 26) / 2);

		g2.setFont(STORY_FONT);
		FontMetrics fm = g2.getFontMetrics();

		for (int i = 0; i < LINES.length; i++) {
			String line = LINES[i];
			if (line.isEmpty())
				continue;

			int x = centerX - fm.stringWidth(line) / 2;
			int y = startY + i * 26;

			int alpha255 = (int) (textAlpha * 255);
			g2.setColor(new Color(220, 220, 220, alpha255));
			g2.drawString(line, x, y);
		}

		if (textAlpha >= 1f) {
			g2.setFont(HINT_FONT);
			g2.setColor(new Color(90, 90, 90));
			String hint = "...";
			FontMetrics hfm = g2.getFontMetrics();
			g2.drawString(hint, centerX - hfm.stringWidth(hint) / 2, ScreenConfig.HEIGHT - 30);
		}

	}

}
