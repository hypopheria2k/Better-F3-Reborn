package com.worador.f3hud;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BeaconModule extends InfoModule {

    private long lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

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
        if (!isEnabledInConfig() || mc.player == null || mc.world == null) return new ArrayList<>();

        // Performance: Scanne nur alle 20 Ticks (1 Sekunde) nach Beacons
        if (mc.player.ticksExisted - lastUpdateTick < 20 && !cachedLines.isEmpty()) {
            return cachedLines;
        }

        List<InfoLine> lines = new ArrayList<>();
        TileEntityBeacon beacon = findNearestActiveBeacon();

        if (beacon != null) {
            BlockPos bPos = beacon.getPos();
            int levels = beacon.getLevels();
            double maxRange = (levels * 10) + 10;

            // Minecraft 1.12.2 Beacon Logik: Quadratische Box, kein Radius!
            double diffX = Math.abs(mc.player.posX - (bPos.getX() + 0.5));
            double diffZ = Math.abs(mc.player.posZ - (bPos.getZ() + 0.5));
            double horizDist = Math.max(diffX, diffZ);

            // Vertikal: Unendlich nach oben, maxRange nach unten
            boolean inVerticalRange = mc.player.posY >= (bPos.getY() - maxRange);

            if (horizDist <= maxRange && inVerticalRange) {
                // Berechnung der verbleibenden "Sicherheitsmarge" bis zum Rand
                double remaining = maxRange - horizDist;
                double percent = (remaining / maxRange) * 100.0;

                int color = 0x55FFFF; // Aqua
                if (percent < 15) color = 0xFF5555; // Rot, wenn man fast aus der Range läuft

                // KEINE BALKEN: Saubere Textanzeige
                // Format: "Beacon: 85% (12m left)"
                String info = String.format(Locale.US, "%.0f%% (%.1fm left)", percent, remaining);
                lines.add(new InfoLine("Beacon: ", info, color));
            }
        }

        this.cachedLines = lines;
        this.lastUpdateTick = mc.player.ticksExisted;
        return lines;
    }

    private TileEntityBeacon findNearestActiveBeacon() {
        TileEntityBeacon nearest = null;
        double minDistSq = 4096; // Max 64 Blöcke Scan-Radius für Performance

        // Wir nutzen die geladene Liste, begrenzen aber die Distanz-Prüfung
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
}