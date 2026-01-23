package com.worador.f3hud;

import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;

public abstract class InfoModule {
    protected final Minecraft mc = Minecraft.getMinecraft();
    public boolean enabled = true;
    
    // Performance Throttling: Update once per second (20 ticks) using tick-based caching
    private static final int UPDATE_INTERVAL_TICKS = 20;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    public abstract List<InfoLine> getLines();
    public abstract String getName();

    public boolean isEnabled() {
        return enabled && isEnabledInConfig();
    }

    protected boolean isEnabledInConfig() {
        return true;
    }

    public static class InfoLine {
        public String label;
        public String value;
        public int color;

        public InfoLine(String label, String value, int color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }
    }

    // Throttled getLines() method with tick-based caching
    public List<InfoLine> getThrottledLines() {
        if (mc.player == null) return new ArrayList<>();
        
        int currentTick = mc.player.ticksExisted;
        
        // Only update if enough ticks have passed or if it's the first call
        if (cachedLines.isEmpty() || (currentTick - lastUpdateTick) >= UPDATE_INTERVAL_TICKS) {
            cachedLines = getLines();
            lastUpdateTick = currentTick;
        }
        
        return cachedLines;
    }

    public int getHeight() {
        return getThrottledLines().size() * 11 + 5;
    }

    public int getMaxLineWidth() {
        int max = 0;
        for (InfoLine line : getThrottledLines()) {
            int w = mc.fontRenderer.getStringWidth(line.label + line.value);
            if (w > max) max = w;
        }
        return max;
    }
}
