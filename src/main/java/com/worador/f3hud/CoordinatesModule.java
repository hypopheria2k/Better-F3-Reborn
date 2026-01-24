package com.worador.f3hud;

import net.minecraft.util.math.MathHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CoordinatesModule extends InfoModule {
    private double lastX, lastY, lastZ;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override public String getName() { return "Coordinates"; }
    @Override protected boolean isEnabledInConfig() { return ModConfig.modules.showCoordinates; }

    @Override
    public List<InfoLine> getLines() {
        if (!isEnabledInConfig() || mc.player == null) return new ArrayList<>();

        // Performance: Nur bei tatsächlicher Bewegung neu berechnen
        if (mc.player.posX == lastX && mc.player.posY == lastY && mc.player.posZ == lastZ && !cachedLines.isEmpty()) {
            return cachedLines;
        }

        this.lastX = mc.player.posX;
        this.lastY = mc.player.posY;
        this.lastZ = mc.player.posZ;

        // WICHTIG: Liste komplett neu initialisieren
        List<InfoLine> lines = new ArrayList<>();

        // 1. XYZ Zeile - Präzise 3 Nachkommastellen (wie gewünscht)
        // Nutzt minY für die exakte Standhöhe
        double displayY = mc.player.getEntityBoundingBox().minY;
        String xyzCoords = String.format(Locale.US, "%.3f / %.3f / %.3f", lastX, displayY, lastZ);
        lines.add(new InfoLine("XYZ: ", xyzCoords, ModConfig.colors.colorX));

        // 2. Chunk Zeile - IDs
        int chunkX = MathHelper.floor(lastX) >> 4;
        int chunkZ = MathHelper.floor(lastZ) >> 4;
        lines.add(new InfoLine("Chunk ID: ", chunkX + " " + chunkZ, ModConfig.colors.colorChunk));

        this.cachedLines = lines;
        return lines;
    }
}