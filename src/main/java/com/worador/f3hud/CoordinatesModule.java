package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CoordinatesModule extends InfoModule {
    private static final int UPDATE_INTERVAL_TICKS = 2; // Schneller für flüssige Bewegung
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override public String getName() { return "Coordinates"; }
    @Override protected boolean isEnabledInConfig() { return ModConfig.modules.showCoordinates; }

    @Override
    public List<InfoLine> getLines() {
        if (!isEnabledInConfig() || mc.player == null) return new ArrayList<>();
        int currentTick = mc.player.ticksExisted;

        if (cachedLines.isEmpty() || (currentTick - lastUpdateTick) >= UPDATE_INTERVAL_TICKS) {
            cachedLines = new ArrayList<>();
            BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

            // XYZ
            int yColor = ModConfig.colors.colorY;
            if (mc.player.posY >= 10.0 && mc.player.posY <= 13.0) yColor = 0x55FFFF;
            else if (mc.player.posY < 5.0) yColor = 0xAA00AA;

            cachedLines.add(new InfoLine("X: ", String.format(Locale.US, "%.1f ", mc.player.posX), ModConfig.colors.colorX));
            cachedLines.add(new InfoLine("Y: ", String.format(Locale.US, "%.1f ", mc.player.posY), yColor));
            cachedLines.add(new InfoLine("Z: ", String.format(Locale.US, "%.1f", mc.player.posZ), ModConfig.colors.colorZ));

            // Block & Chunk
            cachedLines.add(new InfoLine("Block: ", String.format("X:%d Y:%d Z:%d", blockPos.getX(), blockPos.getY(), blockPos.getZ()), ModConfig.colors.colorBlock));
            cachedLines.add(new InfoLine("Chunk: ", String.format("ID: %d, %d | Pos: %d %d %d", blockPos.getX() >> 4, blockPos.getZ() >> 4, blockPos.getX() & 15, blockPos.getY() & 15, blockPos.getZ() & 15), ModConfig.colors.colorChunk));

            lastUpdateTick = currentTick;
        }
        return cachedLines;
    }
}