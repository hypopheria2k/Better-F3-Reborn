package com.worador.f3hud;

import net.minecraftforge.fml.common.Loader;
import java.util.ArrayList;
import java.util.List;

public class AstralModule extends InfoModule {

    // Performance Throttling: Update once per second (20 ticks) using tick-based caching
    private static final int UPDATE_INTERVAL_TICKS = 20;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override
    public String getName() { return "Astral Sorcery"; }

    @Override
    protected boolean isEnabledInConfig() {
        // Strict Gating: Check config first, before any expensive operations
        return ModConfig.modules.showAstralSorcery;
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
            if (Loader.isModLoaded("astralsorcery")) {
                // Aufruf über vollqualifizierten Namen
                cachedLines.addAll(com.worador.f3hud.compat.AstralCompat.getAstralLines(mc.world));
            }
            
            lastUpdateTick = currentTick;
        }
        
        return cachedLines;
    }
}
