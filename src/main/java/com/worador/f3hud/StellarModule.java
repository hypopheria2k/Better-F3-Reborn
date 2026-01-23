package com.worador.f3hud;

import net.minecraftforge.fml.common.Loader;
import java.util.ArrayList;
import java.util.List;

public class StellarModule extends InfoModule {

    // Performance Throttling: Update once per second (20 ticks) using tick-based caching
    private static final int UPDATE_INTERVAL_TICKS = 20;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override
    public String getName() {
        return "StellarCore";
    }

    @Override
    protected boolean isEnabledInConfig() {
        // Strict Gating: Check config first, before any expensive operations
        return Loader.isModLoaded("stellar_core") && ModConfig.modules.showStellar;
    }

    @Override
    public List<InfoLine> getLines() {
        // Strict Gating: Early exit if disabled
        if (!isEnabledInConfig() || mc.player == null || mc.world == null) {
            return new ArrayList<>();
        }

        int currentTick = mc.player.ticksExisted;
        
        // Only update if enough ticks have passed or if it's the first call
        if (cachedLines.isEmpty() || (currentTick - lastUpdateTick) >= UPDATE_INTERVAL_TICKS) {
            cachedLines = new ArrayList<>();
            
            // Expensive operations inside the refresh block
            cachedLines.addAll(com.worador.f3hud.compat.StellarCompat.getStellarLines(mc.world));
            
            lastUpdateTick = currentTick;
        }
        
        return cachedLines;
    }
}
