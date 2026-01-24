package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import java.util.ArrayList;
import java.util.List;

public class WorldModule extends InfoModule {

    private static final int UPDATE_INTERVAL_TICKS = 20;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override public String getName() { return "World"; }

    @Override
    protected boolean isEnabledInConfig() {
        // Modul bleibt aktiv, wenn einer der drei Schalter in der Config an ist
        return ModConfig.modules.showWorld || ModConfig.modules.showBiome || ModConfig.modules.showDimension;
    }

    @Override
    public List<InfoLine> getLines() {
        if (!isEnabledInConfig() || mc.player == null || mc.world == null) return new ArrayList<>();

        int currentTick = mc.player.ticksExisted;

        // Tick-Caching zur CPU-Optimierung
        if (cachedLines.isEmpty() || (currentTick - lastUpdateTick) >= UPDATE_INTERVAL_TICKS) {
            List<InfoLine> lines = new ArrayList<>();
            BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

            // 1. Dimension
            if (ModConfig.modules.showDimension) {
                int dimId = mc.world.provider.getDimension();
                String dimName;
                switch (dimId) {
                    case -1:  dimName = "The Nether"; break;
                    case 0:   dimName = "Overworld"; break;
                    case 1:   dimName = "The End"; break;
                    default:  dimName = "ID: " + dimId; break;
                }
                lines.add(new InfoLine("Dimension: ", dimName, ModConfig.colors.colorDimension));
            }

            // 2. Biome
            if (ModConfig.modules.showBiome) {
                lines.add(new InfoLine("Biome: ", mc.world.getBiome(pos).getBiomeName(), ModConfig.colors.colorBiome));
            }

            // 3. Basis World-Infos (KEIN XYZ MEHR)
            if (ModConfig.modules.showWorld) {
                // Facing (Blickrichtung)
                EnumFacing facing = mc.player.getHorizontalFacing();
                String directionInfo = facing.name().toUpperCase() + " " + getAxisSign(facing);
                lines.add(new InfoLine("Facing: ", directionInfo, ModConfig.colors.colorDefault));

                // TPS Logik für Singleplayer
                if (mc.isSingleplayer() && mc.getIntegratedServer() != null) {
                    long[] times = mc.getIntegratedServer().tickTimeArray;
                    long sum = 0;
                    for (long t : times) sum += t;
                    double avgTime = (double)sum / times.length * 1.0E-6D;
                    double tps = Math.min(20.0, 1000.0 / avgTime);

                    // Dynamische Farbe basierend auf Performance
                    int tpsColor = tps > 18 ? 0x55FF55 : (tps > 12 ? 0xFFFF55 : 0xFF5555);
                    lines.add(new InfoLine("TPS: ", String.format("%.1f (%.1fms)", tps, avgTime), tpsColor));
                }

                // Lokale Schwierigkeit und Spieltage
                float diff = mc.world.getDifficultyForLocation(pos).getAdditionalDifficulty();
                long days = mc.world.getWorldTime() / 24000L;
                lines.add(new InfoLine("Local Diff: ", String.format("%.2f (Day %d)", diff, days), ModConfig.colors.colorDefault));
            }

            this.cachedLines = lines;
            this.lastUpdateTick = currentTick;
        }

        return cachedLines;
    }

    private String getAxisSign(EnumFacing facing) {
        switch (facing) {
            case NORTH: return "(-Z)";
            case SOUTH: return "(+Z)";
            case WEST:  return "(-X)";
            case EAST:  return "(+X)";
            default:    return "";
        }
    }
}