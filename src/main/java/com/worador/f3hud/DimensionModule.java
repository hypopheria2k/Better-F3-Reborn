package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import java.util.ArrayList;
import java.util.List;

public class DimensionModule extends InfoModule {

    private static final int UPDATE_INTERVAL_TICKS = 20;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

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
        if (!isEnabledInConfig() || mc.player == null || mc.world == null) {
            return new ArrayList<>();
        }

        int currentTick = mc.player.ticksExisted;

        if (cachedLines.isEmpty() || (currentTick - lastUpdateTick) >= UPDATE_INTERVAL_TICKS) {
            cachedLines = new ArrayList<>();

            int dimension = mc.world.provider.getDimension();
            String dimDisplay;
            int color;

            switch (dimension) {
                case -1:
                    dimDisplay = "The Nether";
                    color = 0xFF5555;
                    break;
                case 0:
                    dimDisplay = "Overworld";
                    color = 0x55FF55;
                    break;
                case 1:
                    dimDisplay = "The End";
                    color = 0xFF55FF;
                    break;
                default:
                    dimDisplay = "Dim: " + dimension;
                    color = ModConfig.colors.colorDimension;
                    break;
            }

            cachedLines.add(new InfoLine("World: ", dimDisplay, color));

            BlockPos pos = mc.player.getPosition();
            Biome biome = mc.world.getBiome(pos);
            cachedLines.add(new InfoLine("Biome: ", biome.getBiomeName(), 0xAAAAAA));

            lastUpdateTick = currentTick; // Wichtig: Timer zurücksetzen!
        } // Ende des If-Blocks

        return cachedLines;
    }
}