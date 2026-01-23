package com.worador.f3hud;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SlimeChunkModule extends InfoModule {

    @Override
    public String getName() {
        return "Slime Chunk Proximity";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showSlimeDistance;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null) return lines;

        // Die Slime-Berechnung funktioniert nur in der Overworld korrekt
        if (mc.world.provider.getDimension() != 0) return lines;

        long seed = mc.world.getSeed();
        int currentChunkX = mc.player.chunkCoordX;
        int currentChunkZ = mc.player.chunkCoordZ;

        boolean isCurrentSlime = isSlimeChunk(seed, currentChunkX, currentChunkZ);

        double minDistance = Double.MAX_VALUE;
        double targetX = 0, targetZ = 0;
        int radius = 6; // Radius leicht erhöht für bessere Abdeckung

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (isSlimeChunk(seed, currentChunkX + x, currentChunkZ + z)) {
                    double centerX = ((currentChunkX + x) << 4) + 8.5;
                    double centerZ = ((currentChunkZ + z) << 4) + 8.5;

                    double dist = mc.player.getDistance(centerX, mc.player.posY, centerZ);
                    if (dist < minDistance) {
                        minDistance = dist;
                        targetX = centerX;
                        targetZ = centerZ;
                    }
                }
            }
        }

        // 1. Status: Aktueller Chunk mit detailliertem Höhen-Check
        if (isCurrentSlime) {
            String yStatus;
            int slimeColor;

            if (mc.player.posY < 40) {
                yStatus = " (Active Zone)";
                slimeColor = 0x55FF55; // Grün
            } else {
                // Berechne Distanz nach unten zur aktiven Zone (Y=39)
                double toGo = mc.player.posY - 39.0;
                yStatus = String.format(Locale.US, " (Too High - Dig %.1fm Down)", toGo);
                slimeColor = 0xFFFF55; // Gelb als Warnung
            }
            lines.add(new InfoLine("Slime Chunk: ", "YES" + yStatus, slimeColor));
        }

        // 2. Navigation zum nächsten Chunk
        if (minDistance != Double.MAX_VALUE) {
            // Richtung berechnen
            double diffX = targetX - mc.player.posX;
            double diffZ = targetZ - mc.player.posZ;
            String direction = getDirection(diffX, diffZ);

            // NEU: Vertikale Komponente für die Navigation hinzufügen
            String vNav = "";
            if (mc.player.posY >= 40.0) {
                double vDist = mc.player.posY - 39.0;
                vNav = String.format(Locale.US, " | ↓ %.1fm Down", vDist);
            }

            int distColor = (minDistance < 16) ? 0x55FF55 : 0xAAAAAA;
            lines.add(new InfoLine("Next Slime: ", String.format(Locale.US, "%.1fm (%s)%s", minDistance, direction, vNav), distColor));

        } else {
            lines.add(new InfoLine("Slime Chunk: ", "None in 6 Chunks", 0xFF5555));
        }

        return lines;
    }

    private String getDirection(double x, double z) {
        double angle = Math.toDegrees(Math.atan2(x, z));
        // Minecraft Winkel-Logik auf Himmelsrichtungen mappen
        if (angle > -22.5 && angle <= 22.5) return "South";
        if (angle > 22.5 && angle <= 67.5) return "South-West";
        if (angle > 67.5 && angle <= 112.5) return "West";
        if (angle > 112.5 && angle <= 157.5) return "North-West";
        if (angle > 157.5 || angle <= -157.5) return "North";
        if (angle > -157.5 && angle <= -112.5) return "North-East";
        if (angle > -112.5 && angle <= -67.5) return "East";
        return "South-East";
    }

    private boolean isSlimeChunk(long seed, int x, int z) {
        Random rnd = new Random(seed +
                (long) (x * x * 0x4c1906) +
                (long) (x * 0x5ac0db) +
                (long) (z * z) * 0x4307a7L +
                (long) (z * 0x5f24f) ^ 0x3ad8025fL);
        return rnd.nextInt(10) == 0;
    }
}
