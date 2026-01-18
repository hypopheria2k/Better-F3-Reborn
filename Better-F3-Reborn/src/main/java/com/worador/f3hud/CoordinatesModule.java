package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CoordinatesModule extends InfoModule {

    @Override
    public String getName() {
        return "Coordinates";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showCoordinates;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();

        if (mc.player == null) return lines;

        // Block-Position für die Ganzzahlen-Berechnung
        BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        // 1. XYZ mit Einzelfarben aus der Config (Rot, Grün, Blau)
        // Wir nutzen hier leere Präfixe, um die Farben direkt hintereinander zu rendern
        String xVal = String.format(Locale.US, "%.1f ", mc.player.posX);
        String yVal = String.format(Locale.US, "%.1f ", mc.player.posY);
        String zVal = String.format(Locale.US, "%.1f", mc.player.posZ);

        lines.add(new InfoLine("X: ", xVal, ModConfig.colors.colorX));
        lines.add(new InfoLine("Y: ", yVal, ModConfig.colors.colorY));
        lines.add(new InfoLine("Z: ", zVal, ModConfig.colors.colorZ));

        // 2. Block Coordinates (Ganze Zahlen) - Nutzt Magenta (colorBlock)
        String blockCoords = blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ();
        lines.add(new InfoLine("Block: ", blockCoords, ModConfig.colors.colorBlock));

        // 3. Chunk Coordinates - Nutzt Cyan (colorChunk)
        int chunkX = blockPos.getX() >> 4;
        int chunkY = blockPos.getY() >> 4;
        int chunkZ = blockPos.getZ() >> 4;

        int relX = blockPos.getX() & 15;
        int relY = blockPos.getY() & 15;
        int relZ = blockPos.getZ() & 15;

        String chunkCoords = String.format("%d %d %d [%d %d %d]", chunkX, chunkY, chunkZ, relX, relY, relZ);
        lines.add(new InfoLine("Chunk: ", chunkCoords, ModConfig.colors.colorChunk));

        return lines;
    }

    @Override
    public int getHeight() {
        // X, Y, Z, Block, Chunk = 5 Zeilen
        return 11 * 5 + 2;
    }
}