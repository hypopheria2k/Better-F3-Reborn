package com.worador.f3hud;

import net.minecraft.block.BlockPortal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumSkyBlock;
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

        if (mc.player == null || mc.world == null) return lines;

        // Block-Position für die Ganzzahlen-Berechnung
        BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        // 1. XYZ mit Einzelfarben aus der Config (Rot, Grün, Blau)
        String xVal = String.format(Locale.US, "%.1f ", mc.player.posX);
        String yVal = String.format(Locale.US, "%.1f ", mc.player.posY);
        String zVal = String.format(Locale.US, "%.1f", mc.player.posZ);

        // Bedingte Y-Färbung
        int yColor = ModConfig.colors.colorY;
        if (mc.player.posY >= 10.0 && mc.player.posY <= 13.0) {
            yColor = 0x55FFFF; // AQUA für Diamant-Ebene
        } else if (mc.player.posY < 5.0) {
            yColor = 0xAA00AA; // DARK_PURPLE für Void-Gefahr
        }

        lines.add(new InfoLine("X: ", xVal, ModConfig.colors.colorX));
        lines.add(new InfoLine("Y: ", yVal, yColor));
        lines.add(new InfoLine("Z: ", zVal, ModConfig.colors.colorZ));

        // --- NEU: Level & Zonen Anzeige ---
        double y = mc.player.posY;
        String zoneName = "Surface";
        String nextLevel = "";
        int zoneColor = 0x55FF55;

        if (y < 5.0) {
            zoneName = "VOID RISK";
            zoneColor = 0xAA00AA;
            nextLevel = String.format("-> Safe: %.0f", 5.0 - y);
        } else if (y >= 10.0 && y <= 13.0) {
            zoneName = "DIAMOND LEVEL";
            zoneColor = 0x55FFFF;
            nextLevel = "MAX ORE";
        } else if (y < 10.0) {
            zoneName = "DEEP CAVE";
            zoneColor = 0xFFAA00;
            nextLevel = String.format("-> Diamond: %.0f", 11.0 - y);
        } else if (y < 63.0) {
            zoneName = "UNDERGROUND";
            zoneColor = 0xAAAAAA;
            nextLevel = String.format("-> Diamond: %.0f", y - 12.0);
        } else if (y > 120.0) {
            zoneName = "HIGH ALTITUDE";
            zoneColor = 0xFFFFFF;
        }
        lines.add(new InfoLine("Zone: ", zoneName + (nextLevel.isEmpty() ? "" : " [" + nextLevel + "]"), zoneColor));
        // ----------------------------------

        // 2. Block Coordinates (Optimiert)
        int bx = blockPos.getX();
        int by = blockPos.getY();
        int bz = blockPos.getZ();

        String formattedBlock = String.format("X:%d Y:%d Z:%d", bx, by, bz);
        lines.add(new InfoLine("Block: ", formattedBlock, ModConfig.colors.colorBlock));

        double subX = mc.player.posX - bx;
        double subZ = mc.player.posZ - bz;
        String subPos = String.format(Locale.US, "Pos in Block: %.2f | %.2f", subX, subZ);
        lines.add(new InfoLine("Rel: ", subPos, 0xAAAAAA));

        // 3. Chunk Coordinates (Aufgewertet)
        int chunkX = blockPos.getX() >> 4;
        int chunkZ = blockPos.getZ() >> 4;

        int relX = blockPos.getX() & 15;
        int relY = blockPos.getY() & 15;
        int relZ = blockPos.getZ() & 15;

        String chunkCoords = String.format("ID: %d, %d | Pos: %d %d %d", chunkX, chunkZ, relX, relY, relZ);
        lines.add(new InfoLine("Chunk: ", chunkCoords, ModConfig.colors.colorChunk));

        // ZUSATZ: Slime Chunk Check
        long seed = mc.world.getSeed();
        java.util.Random rand = new java.util.Random(seed +
                (long) (chunkX * chunkX * 0x4c1906) +
                (long) (chunkX * 0x5ac0db) +
                (long) (chunkZ * chunkZ) * 0x4307a7L +
                (long) (chunkZ * 0x5f24f) ^ 0x3ad8025f);
        boolean isSlimeChunk = (rand.nextInt(10) == 0);

        if (isSlimeChunk) {
            lines.add(new InfoLine("Slime: ", "YES (Spawn below Y=40)", 0x55FF55));
        } else {
            lines.add(new InfoLine("Slime: ", "No", 0xAAAAAA));
        }

        if (relX <= 1 || relX >= 14 || relZ <= 1 || relZ >= 14) {
            lines.add(new InfoLine("Border: ", "Near Chunk Border!", 0xFFAA00));
        }

        // 4. Licht-Level (Bestehend & NEU: Real Light)
        BlockPos playerPos = new BlockPos(mc.player);
        int sky = mc.world.getLightFor(EnumSkyBlock.SKY, playerPos);
        int block = mc.world.getLightFor(EnumSkyBlock.BLOCK, playerPos);
        int total = Math.max(sky, block);

        int lightColor = (total < 8) ? 0xFF5555 : ModConfig.colors.colorLight;
        lines.add(new InfoLine("Light: ", total + " (Sky: " + sky + ", Block: " + block + ")", lightColor));

        int skySubtracted = mc.world.calculateSkylightSubtracted(1.0F);
        int realSky = Math.max(0, sky - skySubtracted);
        int realTotal = Math.max(realSky, block);

        int safetyColor = (realTotal <= 7) ? 0xFF5555 : 0x55FF55;
        String safetyStatus = (realTotal <= 7) ? "DANGER" : "SAFE";
        lines.add(new InfoLine("Real Light: ", realTotal + " [" + safetyStatus + "]", safetyColor));

        // --- NEU: Portal Tracker & Direction ---
        BlockPos nearestPortal = findNearestPortal(blockPos, 128);
        if (nearestPortal != null) {
            double dist = Math.sqrt(blockPos.distanceSqToCenter(nearestPortal.getX(), blockPos.getY(), nearestPortal.getZ()));
            String arrow = getDirectionArrow(blockPos, nearestPortal);
            lines.add(new InfoLine("Portal: ", String.format(Locale.US, "%.1fm %s (%d, %d)", dist, arrow, nearestPortal.getX(), nearestPortal.getZ()), 0x55FFFF));
        }

        // 5. Nether-Koordinaten (Portal-Link mit Blink-Effekt)
        int dim = mc.world.provider.getDimension();
        if (dim == 0) { // Overworld -> Nether
            double nX = mc.player.posX / 8.0;
            double nZ = mc.player.posZ / 8.0;

            if (nearestPortal == null) {
                // Blink-Effekt Logik: Wenn auf 0.5 Blöcke genau, wechsle alle 500ms die Farbe
                int linkColor = 0xAA00AA; // Standard Lila
                boolean isPerfect = Math.abs(mc.player.posX % 8) < 0.5 && Math.abs(mc.player.posZ % 8) < 0.5;
                if (isPerfect && (System.currentTimeMillis() / 500) % 2 == 0) {
                    linkColor = 0xFFFFFF; // Weißes Blinken bei perfektem Link
                }

                lines.add(new InfoLine("Nether Link: ", String.format(Locale.US, "X:%.1f Z:%.1f", nX, nZ), linkColor));
                lines.add(new InfoLine("Portal Info: ", "Ideal for 1:8 Link", 0x55FFFF));
            }
        } else if (dim == -1) { // Nether -> Overworld
            double oX = mc.player.posX * 8.0;
            double oZ = mc.player.posZ * 8.0;

            if (nearestPortal == null) {
                lines.add(new InfoLine("Overworld Link: ", String.format(Locale.US, "X:%.1f Z:%.1f", oX, oZ), 0xAA00AA));
            }

            if (mc.player.posY >= 127.0) {
                lines.add(new InfoLine("Location: ", TextFormatting.RED + "ABOVE BEDROCK", 0xFF5555));
            } else {
                lines.add(new InfoLine("Location: ", "Nether Core", 0xFFAA00));
            }
        }

        // 6. Fallhöhe
        BlockPos rayEnd = playerPos.add(0, -100, 0);
        net.minecraft.util.math.RayTraceResult rayTrace = mc.world.rayTraceBlocks(
                new net.minecraft.util.math.Vec3d(playerPos.getX() + 0.5, playerPos.getY(), playerPos.getZ() + 0.5),
                new net.minecraft.util.math.Vec3d(rayEnd.getX() + 0.5, rayEnd.getY(), rayEnd.getZ() + 0.5)
        );

        double fallDistance = 0.0;
        if (rayTrace != null && rayTrace.typeOfHit == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
            fallDistance = playerPos.getY() - rayTrace.hitVec.y;
        }

        if (fallDistance <= 100.0) {
            int fallColor = (fallDistance > 20.0) ? 0xFF5555 : (fallDistance > 3.0 ? 0xFFFF55 : 0x55FF55);
            lines.add(new InfoLine("Fall: ", String.format(Locale.US, "%.1f", fallDistance), fallColor));
        }

        return lines;
    }

    private BlockPos findNearestPortal(BlockPos start, int radius) {
        BlockPos closest = null;
        double minDist = Double.MAX_VALUE;
        for (int x = -radius; x <= radius; x += 2) {
            for (int z = -radius; z <= radius; z += 2) {
                for (int y = -16; y <= 16; y++) {
                    BlockPos check = start.add(x, y, z);
                    if (mc.world.getBlockState(check).getBlock() instanceof BlockPortal) {
                        double d = start.distanceSq(check);
                        if (d < minDist) {
                            minDist = d;
                            closest = check;
                        }
                    }
                }
            }
        }
        return closest;
    }

    private String getDirectionArrow(BlockPos player, BlockPos target) {
        double angle = Math.atan2(target.getZ() - player.getZ(), target.getX() - player.getX()) * 180 / Math.PI;
        double yaw = mc.player.rotationYaw;
        double relativeAngle = (angle - yaw + 90) % 360;
        if (relativeAngle < 0) relativeAngle += 360;
        if (relativeAngle >= 315 || relativeAngle < 45) return "↑";
        if (relativeAngle >= 45 && relativeAngle < 135) return "→";
        if (relativeAngle >= 135 && relativeAngle < 225) return "↓";
        return "←";
    }

    @Override
    public int getHeight() {
        int count = 9;
        int dim = mc.world.provider.getDimension();
        if (dim == 0 || dim == -1) count += 2;
        count++;
        return 11 * count + 2;
    }
}