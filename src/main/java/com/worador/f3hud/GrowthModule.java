package com.worador.f3hud;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import java.util.ArrayList;
import java.util.List;

public class GrowthModule extends InfoModule {

    // Performance Throttling: Update once per second (20 ticks) using tick-based caching
    private static final int UPDATE_INTERVAL_TICKS = 20;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override
    public String getName() {
        return "Growth Calculator";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showGrowth;
    }

    @Override
    public List<InfoLine> getLines() {
        // Strict Gating: Early exit if disabled
        if (!isEnabledInConfig() || mc.player == null || mc.world == null) {
            return new ArrayList<>();
        }

        int currentTick = mc.player.ticksExisted;
        
        // Only update if enough ticks have passed or if it's the first call
        if (cachedLines.isEmpty() || (currentTick - lastUpdateTick) >= UPDATE_INTERVAL_TICKS) {
            cachedLines = new ArrayList<>();
            
            // Expensive operations inside the refresh block
            // 1. Mobs (Babys) - Aufgewertet mit Fortschrittsbalken
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                Entity entity = mc.objectMouseOver.entityHit;
                if (entity instanceof EntityAgeable) {
                    EntityAgeable baby = (EntityAgeable) entity;
                    int ageTicks = baby.getGrowingAge();
                    if (ageTicks < 0) {
                        // Berechnung: Ein Baby braucht 20 Minuten (24000 Ticks) zum Wachsen
                        float progress = ((24000.0f + ageTicks) / 24000.0f) * 100.0f;
                        cachedLines.add(new InfoLine("Growth: ", getProgressBar(progress) + String.format(" %.1f%%", progress), 0x55FFFF));
                        cachedLines.add(new InfoLine("Adult in: ", formatTime(-ageTicks), 0xFFAA00));
                    }
                }
            }

            // 2. Crops (Pflanzen) - Aufgewertet mit Licht-Check und Stufen-Anzeige
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = mc.objectMouseOver.getBlockPos();
                IBlockState state = mc.world.getBlockState(pos);

                if (state.getBlock() instanceof BlockCrops) {
                    BlockCrops crop = (BlockCrops) state.getBlock();

                    int age = 0;
                    for (IProperty<?> prop : state.getProperties().keySet()) {
                        if (prop.getName().equals("age") && prop instanceof PropertyInteger) {
                            age = state.getValue((PropertyInteger) prop);
                            break;
                        }
                    }

                    int maxAge = crop.getMaxAge();
                    float progress = ((float)age / (float)maxAge) * 100.0f;

                    if (age < maxAge) {
                        // Fortschritt & Stufen
                        cachedLines.add(new InfoLine("Crop: ", getProgressBar(progress) + String.format(" %d/%d", age, maxAge), 0x55FF55));

                        // Licht-Check (Pflanzen brauchen Lichtlevel > 7 zum Wachsen)
                        int light = mc.world.getLight(pos.up());
                        int lightColor = (light > 7) ? 0x55FF55 : 0xFF5555;
                        cachedLines.add(new InfoLine("Light: ", light + (light <= 7 ? " (Too Dark!)" : " (OK)"), lightColor));

                        // Zeit-Schätzung
                        float growthChance = calculateGrowthChance(pos);
                        double avgTicksPerStage = (1.0 / growthChance) * (4096.0 / 3.0);
                        int totalRemainingTicks = (int) (avgTicksPerStage * (maxAge - age));

                        cachedLines.add(new InfoLine("Est. Time: ", "~" + formatTime(totalRemainingTicks), 0xAAAAAA));
                    } else {
                        cachedLines.add(new InfoLine("Status: ", "READY TO HARVEST", 0xFF55FF));
                    }
                }
            }
            
            lastUpdateTick = currentTick;
        }
        
        return cachedLines;
    }

    private String getProgressBar(float percent) {
        int barLength = 10;
        int filled = Math.round(percent / 10);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            sb.append(i < filled ? "|" : ".");
        }
        sb.append("]");
        return sb.toString();
    }

    private float calculateGrowthChance(BlockPos pos) {
        float f = 1.0F;
        BlockPos ground = pos.down();
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                IBlockState stateGround = mc.world.getBlockState(ground.add(i, 0, j));
                if (stateGround.getBlock() == Blocks.FARMLAND) {
                    f1 = 1.0F;
                    if (stateGround.getValue(net.minecraft.block.BlockFarmland.MOISTURE) > 0) {
                        f1 = 3.0F;
                    }
                }
                if (i != 0 || j != 0) f1 /= 4.0F;
                f += f1;
            }
        }
        return f / 2.0F;
    }

    private String formatTime(int ticks) {
        int seconds = ticks / 20;
        int mins = seconds / 60;
        int secs = seconds % 60;
        return mins > 0 ? String.format("%dm %ds", mins, secs) : String.format("%ds", secs);
    }
}
