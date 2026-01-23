package com.worador.f3hud;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.List;

public class ChunkPosModule extends InfoModule {

    // Performance Throttling: Update every 5 ticks (quarter second) for responsive display
    private static final int UPDATE_INTERVAL_TICKS = 5;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override
    public String getName() {
        return "Chunk & Position";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showChunkPos;
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
            BlockPos pos = mc.player.getPosition();

            // 1. Blickrichtung (Facing) - Essentiell für präzises Bauen
            EnumFacing facing = mc.player.getHorizontalFacing();
            String axis = facing.getAxis().getName().toUpperCase();
            cachedLines.add(new InfoLine("Facing: ", String.format("%s (%s)", facing.name(), axis), 0x55FFFF));

            // 2. Absolute Koordinaten
            cachedLines.add(new InfoLine("Pos: ", String.format("X: %d Y: %d Z: %d", pos.getX(), pos.getY(), pos.getZ()), 0xFFFFFF));

            // 3. Chunk-Analyse (Relative Position 0-15)
            int relX = pos.getX() & 15;
            int relZ = pos.getZ() & 15;

            // Grafische Anzeige [ . . . ] wo man im Chunk steht (vereinfachtes 3x3 Raster)
            String visualPos = getChunkGrid(relX, relZ);

            boolean onBorder = (relX == 0 || relX == 15 || relZ == 0 || relZ == 15);
            int relColor = onBorder ? 0xFFAA00 : 0x55FF55;

            cachedLines.add(new InfoLine("Chunk-Rel: ", String.format("%s [%d, %d]", visualPos, relX, relZ), relColor));

            // 4. Chunk-ID & Slime-Vorschau (kleiner Bonus)
            int chunkX = pos.getX() >> 4;
            int chunkZ = pos.getZ() >> 4;
            cachedLines.add(new InfoLine("Chunk-ID: ", String.format("[%d, %d]", chunkX, chunkZ), 0xAAAAAA));
            
            lastUpdateTick = currentTick;
        }
        
        return cachedLines;
    }

    /**
     * Erstellt ein kleines visuelles Raster, um die Position im Chunk anzuzeigen
     */
    private String getChunkGrid(int relX, int relZ) {
        // Wir teilen den 16x16 Chunk in ein 3x3 Raster ein
        String[][] grid = {
                {"TL", "TM", "TR"},
                {"ML", "MM", "MR"},
                {"BL", "BM", "BR"}
        };

        int gridX = (relX < 5) ? 0 : (relX < 11 ? 1 : 2);
        int gridZ = (relZ < 5) ? 0 : (relZ < 11 ? 1 : 2);

        // Spezialzeichen für die Darstellung
        StringBuilder sb = new StringBuilder("[");
        for (int z = 0; z < 3; z++) {
            for (int x = 0; x < 3; x++) {
                if (x == gridX && z == gridZ) sb.append("X"); // Deine Position
                else sb.append(".");
            }
            if (z < 2) sb.append("|");
        }
        sb.append("]");
        return sb.toString();
    }
}
