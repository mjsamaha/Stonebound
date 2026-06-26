package com.lobsterchops.stonebound.game.ui.features.dialogue;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.lobsterchops.stonebound.game.config.ScreenConfig;
import com.lobsterchops.stonebound.game.ui.core.Overlay;


public class DialogueOverlay extends Overlay {

    private String speakerName = "";
    private String dialogueText = "";

    private static final Font FONT = new Font("Arial", Font.PLAIN, 14);

    public void setDialogue(String speaker, String text) {
        this.speakerName  = speaker;
        this.dialogueText = text;
    }

    @Override public void update() {}

    @Override
    public void render(Graphics2D g2) {
        if (!visible) return;
        int boxH  = 100;
        int boxY  = ScreenConfig.HEIGHT - boxH - 10;
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(10, boxY, ScreenConfig.WIDTH - 20, boxH, 12, 12);
        g2.setColor(Color.WHITE);
        g2.setFont(FONT.deriveFont(Font.BOLD));
        g2.drawString(speakerName, 20, boxY + 20);
        g2.setFont(FONT);
        g2.drawString(dialogueText, 20, boxY + 44);
    }
}
