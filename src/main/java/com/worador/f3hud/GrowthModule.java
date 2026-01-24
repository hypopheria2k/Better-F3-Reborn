package com.worador.f3hud;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import java.util.ArrayList;
import java.util.List;

public class GrowthModule extends InfoModule {

    private static final int UPDATE_INTERVAL_TICKS = 10;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override public String getName() { return "Growth"; }
    @Override protected boolean isEnabledInConfig() { return ModConfig.modules.showGrowth; }

    @Override
    public List<InfoLine> getLines() {
        if (!isEnabledInConfig() || mc.player == null || mc.world == null) return new ArrayList<>();

        int currentTick = mc.player.ticksExisted;
        if (!cachedLines.isEmpty() && (currentTick - lastUpdateTick) < UPDATE_INTERVAL_TICKS) {
            return cachedLines;
        }

        List<InfoLine> lines = new ArrayList<>();
        RayTraceResult rt = mc.objectMouseOver;

        if (rt != null) {
            // 1. Mobs (Babys)
            if (rt.typeOfHit == RayTraceResult.Type.ENTITY && rt.entityHit instanceof EntityAgeable) {
                EntityAgeable baby = (EntityAgeable) rt.entityHit;
                int ageTicks = baby.getGrowingAge();
                if (ageTicks < 0) {
                    float progress = ((24000.0f + ageTicks) / 24000.0f) * 100.0f;
                    lines.add(new InfoLine("Baby: ", String.format("%.1f%%", progress), 0x55FFFF));
                    lines.add(new InfoLine("Adult in: ", formatTime(-ageTicks), 0xFFAA00));
                }
            }
            // 2. Crops (Pflanzen)
            else if (rt.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = rt.getBlockPos();
                IBlockState state = mc.world.getBlockState(pos);

                if (state.getBlock() instanceof BlockCrops) {
                    BlockCrops crop = (BlockCrops) state.getBlock();

                    // FIX für protected access: Wir suchen die Age-Property direkt im State
                    int age = 0;
                    int maxAge = crop.getMaxAge();

                    for (IProperty<?> prop : state.getProperties().keySet()) {
                        if (prop.getName().equals("age") && prop instanceof PropertyInteger) {
                            age = (Integer) state.getValue(prop);
                            break;
                        }
                    }

                    float progress = ((float) age / maxAge) * 100.0f;

                    if (age < maxAge) {
                        lines.add(new InfoLine("Growth: ", String.format("%.0f%% (%d/%d)", progress, age, maxAge), 0x55FF55));

                        int light = mc.world.getLight(pos.up());
                        if (light <= 7) {
                            lines.add(new InfoLine("Light: ", light + " (Too Dark!)", 0xFF5555));
                        }

                        // ETA Schätzung (basierend auf Standard Random Ticks)
                        // Durchschnittliche Zeit für eine Stufe bei optimalen Bedingungen (~12 Min gesamt)
                        int totalRemainingTicks = (maxAge - age) * 1800;
                        lines.add(new InfoLine("Ready in: ", "~" + formatTime(totalRemainingTicks), 0xAAAAAA));
                    } else {
                        lines.add(new InfoLine("Status: ", "READY", 0xFF55FF));
                    }
                }
            }
        }

        this.cachedLines = lines;
        this.lastUpdateTick = currentTick;
        return lines;
    }

    private String formatTime(int ticks) {
        int seconds = ticks / 20;
        if (seconds < 60) return seconds + "s";
        int mins = seconds / 60;
        return mins + "m " + (seconds % 60) + "s";
    }
}