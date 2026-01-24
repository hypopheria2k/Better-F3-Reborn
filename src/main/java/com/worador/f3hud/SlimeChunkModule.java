package com.worador.f3hud;

import net.minecraft.util.math.MathHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SlimeChunkModule extends InfoModule {
    private long lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();
    private final Random slimeRand = new Random();

    @Override public String getName() { return "Slime Chunk"; }
    @Override protected boolean isEnabledInConfig() { return ModConfig.modules.showSlimeChunk; }

    @Override
    public List<InfoLine> getLines() {
        if (!isEnabledInConfig() || mc.player == null || mc.world == null) return new ArrayList<>();

        // Slimes spawnen nur in der Overworld (Dimension 0)
        if (mc.world.provider.getDimension() != 0) return new ArrayList<>();

        // Tick-Caching
        if (mc.player.ticksExisted - lastUpdateTick < 10 && !cachedLines.isEmpty()) {
            return cachedLines;
        }

        List<InfoLine> lines = new ArrayList<>();
        long seed = mc.world.getSeed();
        int chunkX = mc.player.chunkCoordX;
        int chunkZ = mc.player.chunkCoordZ;

        if (isSlimeChunk(seed, chunkX, chunkZ)) {
            // NUTZT JETZT DIE FARBE AUS DER CONFIG
            lines.add(new InfoLine("Slime Chunk: ", "YES (Y < 40)", ModConfig.colors.colorSlimeChunk));
        } else {
            // Suche im 5x5 Radius
            double minDistanceSq = Double.MAX_VALUE;
            int foundX = 0, foundZ = 0;

            for (int x = -5; x <= 5; x++) {
                for (int z = -5; z <= 5; z++) {
                    if (isSlimeChunk(seed, chunkX + x, chunkZ + z)) {
                        double tX = ((chunkX + x) << 4) + 8;
                        double tZ = ((chunkZ + z) << 4) + 8;
                        double distSq = mc.player.getDistanceSq(tX, mc.player.posY, tZ);

                        if (distSq < minDistanceSq) {
                            minDistanceSq = distSq;
                            foundX = (int) tX;
                            foundZ = (int) tZ;
                        }
                    }
                }
            }

            if (minDistanceSq != Double.MAX_VALUE) {
                double actualDist = Math.sqrt(minDistanceSq);
                String dir = getRelativeDirection(foundX, foundZ);

                // Wenn nah (< 24 Blöcke), nehmen wir die Slime-Farbe, sonst ein dezentes Grau
                int color = (actualDist < 24) ? ModConfig.colors.colorSlimeChunk : 0xAAAAAA;
                lines.add(new InfoLine("Next Slime: ", String.format(Locale.US, "%.1fm (%s)", actualDist, dir), color));
            } else {
                lines.add(new InfoLine("Slime Chunk: ", "None nearby", 0xFF5555));
            }
        }

        this.cachedLines = lines;
        this.lastUpdateTick = mc.player.ticksExisted;
        return lines;
    }

    private String getRelativeDirection(double targetX, double targetZ) {
        double diffX = targetX - mc.player.posX;
        double diffZ = targetZ - mc.player.posZ;
        float angle = (float) (MathHelper.atan2(diffX, diffZ) * (180D / Math.PI));
        // Wichtig: In 1.12.2 ist rotationYaw manchmal invertiert, wrapDegrees korrigiert das
        float relativeAngle = MathHelper.wrapDegrees(angle - mc.player.rotationYaw);

        if (relativeAngle > -22.5 && relativeAngle <= 22.5) return "Ahead";
        if (relativeAngle > 22.5 && relativeAngle <= 67.5) return "Left-Ahead";
        if (relativeAngle > 67.5 && relativeAngle <= 112.5) return "Left";
        if (relativeAngle > 112.5 && relativeAngle <= 157.5) return "Left-Behind";
        if (relativeAngle > 157.5 || relativeAngle <= -157.5) return "Behind";
        if (relativeAngle > -157.5 && relativeAngle <= -112.5) return "Right-Behind";
        if (relativeAngle > -112.5 && relativeAngle <= -67.5) return "Right";
        return "Right-Ahead";
    }

    private boolean isSlimeChunk(long seed, int x, int z) {
        // Mathematisch korrekte Slime-Chunk-Formel für 1.12.2
        slimeRand.setSeed(seed + (long) (x * x * 0x4c1906) + (long) (x * 0x5ac0db) + (long) (z * z) * 0x4307a7L + (long) (z * 0x5f24f) ^ 0x3ad8025fL);
        return slimeRand.nextInt(10) == 0;
    }
}