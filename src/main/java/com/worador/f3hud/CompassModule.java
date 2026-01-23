package com.worador.f3hud;

import java.util.ArrayList;
import java.util.List;

public class CompassModule extends InfoModule {

    // Performance Throttling: Update every 2 ticks (tenth of a second) for high responsiveness
    private static final int UPDATE_INTERVAL_TICKS = 2;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override
    public String getName() { return "Compass"; }

    @Override
    protected boolean isEnabledInConfig() { return ModConfig.modules.showCompass; }

    @Override
    public List<InfoLine> getLines() {
        // Strict Gating: Early exit if disabled
        if (!isEnabledInConfig() || mc.player == null) {
            return new ArrayList<>();
        }

        int currentTick = mc.player.ticksExisted;
        
        // Only update if enough ticks have passed or if it's the first call
        if (cachedLines.isEmpty() || (currentTick - lastUpdateTick) >= UPDATE_INTERVAL_TICKS) {
            cachedLines = new ArrayList<>(); // Keine Textzeilen, da wir grafisch rendern
            lastUpdateTick = currentTick;
        }
        
        return cachedLines;
    }

    @Override
    public int getHeight() {
        return 17; // Höhe des Balkens
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
