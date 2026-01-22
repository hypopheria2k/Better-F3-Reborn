package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import java.util.ArrayList;
import java.util.List;

public class DimensionModule extends InfoModule {

    @Override
    public String getName() {
        return "Dimension";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showDimension;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null) return lines;

        int dimension = mc.world.provider.getDimension();
        String dimDisplay;
        int color;

        // 1. Dimension-Analyse und Farbgebung
        switch (dimension) {
            case -1:
                dimDisplay = "The Nether";
                color = 0xFF5555; // Rot
                break;
            case 0:
                dimDisplay = "Overworld";
                color = 0x55FF55; // Grün
                break;
            case 1:
                dimDisplay = "The End";
                color = 0xFF55FF; // Magenta
                break;
            default:
                dimDisplay = "Dim: " + dimension;
                color = ModConfig.colors.colorDimension;
                break;
        }

        lines.add(new InfoLine("World: ", dimDisplay, color));

        // 2. Biom-Anzeige (Wichtig für Spawning-Regeln)
        BlockPos pos = mc.player.getPosition();
        Biome biome = mc.world.getBiome(pos);
        lines.add(new InfoLine("Biome: ", biome.getBiomeName(), 0xAAAAAA));

        // 3. Koordinaten-Link (Das technische Highlight)
        // Hilft beim Finden von Portalen
        if (dimension == 0) {
            // Zeige, wo man im Nether wäre
            int netherX = pos.getX() / 8;
            int netherZ = pos.getZ() / 8;
            lines.add(new InfoLine("Nether-Link: ", String.format("X: %d, Z: %d", netherX, netherZ), 0xFFAA00));
        } else if (dimension == -1) {
            // Zeige, wo man in der Overworld wäre
            int overX = pos.getX() * 8;
            int overZ = pos.getZ() * 8;
            lines.add(new InfoLine("Overworld-Link: ", String.format("X: %d, Z: %d", overX, overZ), 0x55FFFF));
        }

        return lines;
    }
}