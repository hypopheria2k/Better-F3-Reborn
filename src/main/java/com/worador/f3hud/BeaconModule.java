package com.worador.f3hud;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;

public class BeaconModule extends InfoModule {

    @Override
    public String getName() {
        return "Beacon Range";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showBeaconRange;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null) return lines;

        TileEntityBeacon beacon = findNearestActiveBeacon();
        if (beacon != null) {
            BlockPos bPos = beacon.getPos();

            // Horizontale Distanz (Quadratisch in 1.12.2)
            double diffX = Math.abs(mc.player.posX - (bPos.getX() + 0.5));
            double diffZ = Math.abs(mc.player.posZ - (bPos.getZ() + 0.5));
            double horizDist = Math.max(diffX, diffZ);

            int levels = beacon.getLevels();
            double maxRange = (levels * 10) + 10;

            // Vertikale Prüfung: In 1.12.2 unendlich nach oben, aber maxRange nach unten
            boolean inVerticalRange = mc.player.posY >= (bPos.getY() - maxRange);

            if (horizDist <= maxRange && inVerticalRange) {
                double percent = (1.0 - (horizDist / maxRange)) * 100.0;
                percent = Math.max(0, Math.min(100, percent));

                String bar = generateBar(percent);
                int color = 0x55FFFF; // Aqua
                if (percent < 20) color = 0xFF5555; // Rot bei kritischer Distanz

                lines.add(new InfoLine("Beacon Range: ", String.format("%s %.0f%%", bar, percent), color));
            }
        }
        return lines;
    }

    private TileEntityBeacon findNearestActiveBeacon() {
        TileEntityBeacon nearest = null;
        double minDistSq = Double.MAX_VALUE;

        for (TileEntity te : mc.world.loadedTileEntityList) {
            if (te instanceof TileEntityBeacon) {
                TileEntityBeacon b = (TileEntityBeacon) te;
                if (b.getLevels() > 0) {
                    double dSq = mc.player.getDistanceSqToCenter(b.getPos());
                    if (dSq < minDistSq) {
                        minDistSq = dSq;
                        nearest = b;
                    }
                }
            }
        }
        return nearest;
    }

    private String generateBar(double percent) {
        int totalBars = 10;
        int filledBars = (int) Math.round(percent / 10.0);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) sb.append("|");
            else sb.append("-");
        }
        sb.append("]");
        return sb.toString();
    }
}