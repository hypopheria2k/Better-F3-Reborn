package com.worador.f3hud;

import net.minecraft.util.math.MathHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RegionModule extends InfoModule {

    // Performance Throttling: Update once per second (20 ticks) using tick-based caching
    private static final int UPDATE_INTERVAL_TICKS = 20;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override
    public String getName() {
        return "Region";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showRegion;
    }

    @Override
    public List<InfoLine> getLines() {
        // Strict Gating: Early exit if disabled
        if (!isEnabledInConfig() || mc.player == null) {
            return new ArrayList<>();
        }

        int currentTick = mc.player.ticksExisted;
        
        // Only update if enough ticks have passed or if it's the first call
        if (cachedLines.isEmpty() || (currentTick - lastUpdateTick) >= UPDATE_INTERVAL_TICKS) {
            cachedLines = new ArrayList<>();
            
            // Expensive operations inside the refresh block
            // 1. Region-File Berechnung
            int regX = MathHelper.floor(mc.player.posX) >> 9; // 512 Blöcke = 1 Region
            int regZ = MathHelper.floor(mc.player.posZ) >> 9;

            // 2. Lokale Chunk-Position (0-31)
            int chunkX = (MathHelper.floor(mc.player.posX) >> 4) & 31;
            int chunkZ = (MathHelper.floor(mc.player.posZ) >> 4) & 31;

            // 3. Lokale Block-Position innerhalb der Region (0-511)
            int localBlockX = MathHelper.floor(mc.player.posX) & 511;
            int localBlockZ = MathHelper.floor(mc.player.posZ) & 511;

            // Anzeige: Dateiname (Deutsches Verhalten: Klar und präzise)
            String regionFile = String.format(Locale.US, "r.%d.%d.mca", regX, regZ);
            cachedLines.add(new InfoLine("File: ", regionFile, 0x55FF55));

            // Anzeige: Chunk-Slot (Wichtig für NBT-Editoren)
            String chunkSlot = String.format(Locale.US, "Slot [%d, %d]", chunkX, chunkZ);
            cachedLines.add(new InfoLine("Local: ", chunkSlot, 0xAAAAAA));

            // ZUSATZ: Rand-Warnung (World-Pruning Hilfe)
            // Wenn man weniger als 32 Blöcke vom Rand der Region weg ist
            if (localBlockX < 32 || localBlockX > 480 || localBlockZ < 32 || localBlockZ > 480) {
                cachedLines.add(new InfoLine("Edge: ", "Near Region Border", 0xFFAA00));
            } else {
                // Relative Position in Blöcken anzeigen (für technische Analysen)
                cachedLines.add(new InfoLine("Rel: ", String.format("%d / %d", localBlockX, localBlockZ), 0x777777));
            }
            
            lastUpdateTick = currentTick;
        }
        
        return cachedLines;
    }
}
