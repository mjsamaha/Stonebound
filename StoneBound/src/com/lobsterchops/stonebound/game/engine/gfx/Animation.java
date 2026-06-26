package com.lobsterchops.stonebound.game.engine.gfx;

public class Animation {

    private final Sprite[] frames;
    private final int ticksPerFrame;
    private final boolean looping;

    private int currentFrame = 0;
    private int tickCount    = 0;
    private boolean finished = false;

    public Animation(Sprite[] frames, int ticksPerFrame, boolean looping) {
        if (frames == null || frames.length == 0)
            throw new IllegalArgumentException("Animation must have at least one frame.");
        this.frames        = frames;
        this.ticksPerFrame = ticksPerFrame;
        this.looping       = looping;
    }

    public void update() {
        if (finished) return;
        if (++tickCount >= ticksPerFrame) {
            tickCount = 0;
            currentFrame++;
            if (currentFrame >= frames.length) {
                if (looping) {
                    currentFrame = 0;
                } else {
                    currentFrame = frames.length - 1;
                    finished = true;
                }
            }
        }
    }

    public Sprite currentSprite() { return frames[currentFrame]; }

    public void reset() {
        currentFrame = 0;
        tickCount    = 0;
        finished     = false;
    }

    public boolean isFinished() { return finished; }
    public int frameCount()     { return frames.length; }
}
