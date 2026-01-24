package com.worador.f3hud;

import net.minecraft.util.math.MathHelper;
import java.util.ArrayList;
import java.util.List;

public class RegionModule extends InfoModule {

    private static final int UPDATE_INTERVAL_TICKS = 20;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override public String getName() { return "Region"; }
    @Override protected boolean isEnabledInConfig() { return ModConfig.modules.showRegion; }

    @Override
    public List<InfoLine> getLines() {
        if (!isEnabledInConfig() || mc.player == null) return new ArrayList<>();

        int currentTick = mc.player.ticksExisted;

        // Tick-Caching
        if (cachedLines.isEmpty() || (currentTick - lastUpdateTick) >= UPDATE_INTERVAL_TICKS) {
            List<InfoLine> lines = new ArrayList<>();
            int labelColor = ModConfig.colors.colorRegion; // Konsistente Farbe aus Config

            int blockX = MathHelper.floor(mc.player.posX);
            int blockZ = MathHelper.floor(mc.player.posZ);

            // 1. Region-File (>> 9 entspricht / 512)
            int regX = blockX >> 9;
            int regZ = blockZ >> 9;

            // 2. Lokale Positionen
            int chunkX = (blockX >> 4) & 31;
            int chunkZ = (blockZ >> 4) & 31;
            int localBlockX = blockX & 511;
            int localBlockZ = blockZ & 511;

            // Anzeige: Chunk-Slot (Wichtig für NBT-Editoren)
            String chunkSlot = "Slot [" + chunkX + ", " + chunkZ + "]";
            lines.add(new InfoLine("Local: ", chunkSlot, 0xAAAAAA));

            // Rand-Warnung oder Relative Position
            if (localBlockX < 32 || localBlockX > 480 || localBlockZ < 32 || localBlockZ > 480) {
                lines.add(new InfoLine("Edge: ", "Near Region Border", 0xFFAA00));
            } else {
                lines.add(new InfoLine("Rel: ", localBlockX + " / " + localBlockZ, 0x777777));
            }

            this.cachedLines = lines;
            this.lastUpdateTick = currentTick;
        }

        return cachedLines;
    }
}