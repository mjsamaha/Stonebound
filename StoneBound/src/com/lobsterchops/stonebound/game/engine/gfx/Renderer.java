package com.lobsterchops.stonebound.game.engine.gfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.lobsterchops.stonebound.game.engine.math.Rectangle;

public class Renderer {
	 
    // ── Back buffer ──────────────────────────────────────────────────────────
 
    private final BufferedImage backBuffer;
    private final int           bufferWidth;
    private final int           bufferHeight;
 
    /** Live context, valid only between {@link #begin()} and {@link #end}. */
    private Graphics2D g2;
 
    private final Camera camera;
 
    // ── Construction ─────────────────────────────────────────────────────────
 
    /**
     * @param camera       used for world-to-screen coordinate conversion
     * @param bufferWidth  internal resolution width  (e.g. {@code ScreenConfig.WIDTH})
     * @param bufferHeight internal resolution height (e.g. {@code ScreenConfig.HEIGHT})
     */
    public Renderer(Camera camera, int bufferWidth, int bufferHeight) {
        this.camera       = camera;
        this.bufferWidth  = bufferWidth;
        this.bufferHeight = bufferHeight;
        this.backBuffer   = new BufferedImage(bufferWidth, bufferHeight,
                                              BufferedImage.TYPE_INT_ARGB);
    }
 
    // ── Frame lifecycle ───────────────────────────────────────────────────────
 
    /**
     * Opens a new frame.  Creates a fresh {@code Graphics2D} from the back
     * buffer and clears it to black.  Must be called before any draw method.
     */
    public void begin() {
        g2 = backBuffer.createGraphics();
 
        // Pixel-art quality: nearest-neighbor for images, no AA on pixels.
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
 
        // Clear to black each frame.
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, bufferWidth, bufferHeight);
    }
 
    /**
     * Closes the frame.  Blits the back buffer to the Swing surface using
     * nearest-neighbor scaling (pixel-art safe), then disposes the back-buffer
     * context.
     *
     * <p><b>Do NOT dispose {@code swingGraphics}</b> — GamePanel owns it.
     *
     * @param swingGraphics the {@code Graphics} object provided by Swing's
     *                      {@code paintComponent} — must not be null
     */
    public void end(Graphics swingGraphics) {
        if (g2 == null) return;
 
        // Dispose the back-buffer context we created in begin().
        g2.dispose();
        g2 = null;
 
        // Blit back buffer → Swing surface, nearest-neighbor upscale.
        Graphics2D sg = (Graphics2D) swingGraphics;
        sg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        sg.drawImage(backBuffer, 0, 0, null);
    }
 
    // ── World-space drawing (camera offset applied) ───────────────────────────
 
    /**
     * Draws a sprite at a world position, offset by the camera.
     *
     * @param sprite  the sprite to draw; must not be null
     * @param worldX  world X in pixels
     * @param worldY  world Y in pixels
     */
    public void drawSprite(Sprite sprite, float worldX, float worldY) {
        assertFrameOpen();
        sprite.draw(g2,
                    camera.toScreenX(worldX),
                    camera.toScreenY(worldY));
    }
 
    /**
     * Draws the current frame of an animation at a world position.
     *
     * @param animation the animation to sample; must not be null
     * @param worldX    world X in pixels
     * @param worldY    world Y in pixels
     */
    public void drawAnimation(Animation animation, float worldX, float worldY) {
        assertFrameOpen();
        drawSprite(animation.currentSprite(), worldX, worldY);
    }
 
    /**
     * Draws a tile sprite at tile grid coordinates.  Converts tile coords to
     * world pixels (tile * tileSize) then applies the camera offset.
     *
     * @param sprite   tile sprite; must not be null
     * @param tileX    tile column index
     * @param tileY    tile row index
     * @param tileSize tile size in pixels (typically {@code Chunk.SIZE * scale})
     */
    public void drawTile(Sprite sprite, int tileX, int tileY, int tileSize) {
        assertFrameOpen();
        float worldX = tileX * (float) tileSize;
        float worldY = tileY * (float) tileSize;
        sprite.draw(g2,
                    camera.toScreenX(worldX),
                    camera.toScreenY(worldY));
    }
 
    // ── Screen-space drawing (no camera offset) ───────────────────────────────
 
    /**
     * Draws a sprite at a fixed screen position, ignoring the camera.
     * Use this for HUD elements, overlays, and UI.
     */
    public void drawSpriteScreen(Sprite sprite, int x, int y) {
        assertFrameOpen();
        sprite.draw(g2, x, y);
    }
 
    /**
     * Draws a text string at a screen position.
     *
     * @param text  the string to draw; null is silently ignored
     * @param color text colour; must not be null
     * @param font  font to use; must not be null
     * @param x     left baseline x (screen space)
     * @param y     baseline y (screen space)
     */
    public void drawText(String text, Color color, Font font, int x, int y) {
        if (text == null) return;
        assertFrameOpen();
        g2.setFont(font);
        g2.setColor(color);
        g2.drawString(text, x, y);
    }
 
    /**
     * Fills a solid rectangle in screen space.
     *
     * @param color the fill colour
     * @param x     left edge
     * @param y     top edge
     * @param w     width
     * @param h     height
     */
    public void fillRect(Color color, int x, int y, int w, int h) {
        assertFrameOpen();
        g2.setColor(color);
        g2.fillRect(x, y, w, h);
    }
 
    /**
     * Draws a 1px outline rectangle in screen space.
     *
     * @param color the stroke colour
     * @param x     left edge
     * @param y     top edge
     * @param w     width
     * @param h     height
     */
    public void drawRect(Color color, int x, int y, int w, int h) {
        assertFrameOpen();
        g2.setColor(color);
        g2.drawRect(x, y, w, h);
    }
 
    /**
     * Draws a debug/collision rectangle in world space, camera-offset applied.
     * Useful for visualising entity hit-boxes.
     *
     * @param rect  world-space rectangle
     * @param color outline colour
     */
    public void drawDebugRect(Rectangle rect, Color color) {
        assertFrameOpen();
        int sx = camera.toScreenX(rect.x);
        int sy = camera.toScreenY(rect.y);
        g2.setColor(color);
        g2.drawRect(sx, sy, (int) rect.width, (int) rect.height);
    }
 
    // ── Escape hatch ─────────────────────────────────────────────────────────
 
    /**
     * Returns the raw {@code Graphics2D} context for this frame.
     *
     * <p>Intended only for subsystems (e.g. {@code ScreenManager}) that need
     * features not covered by the named draw methods (AlphaComposite, etc.).
     * Game-logic code should never call this.
     *
     * @return the current frame's {@code Graphics2D}; null if called outside a frame
     */
    public Graphics2D getRaw() {
        return g2;
    }
 
    /** Returns the camera used for world-to-screen conversion. */
    public Camera getCamera() { return camera; }
 
    /** Width of the internal back buffer (logical resolution). */
    public int getBufferWidth()  { return bufferWidth;  }
 
    /** Height of the internal back buffer (logical resolution). */
    public int getBufferHeight() { return bufferHeight; }
 
    // ── Internal ─────────────────────────────────────────────────────────────
 
    private void assertFrameOpen() {
        if (g2 == null) {
            throw new IllegalStateException(
                "Renderer.begin() must be called before any draw method.");
        }
    }
}