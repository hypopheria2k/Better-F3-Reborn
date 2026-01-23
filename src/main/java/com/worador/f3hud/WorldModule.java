package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldModule extends InfoModule {

    // Performance Throttling: Update once per second (20 ticks) using tick-based caching
    private static final int UPDATE_INTERVAL_TICKS = 20;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    // Caching für Slime Chunks, damit wir nicht jeden Frame rechnen
    private int lastChunkX = Integer.MAX_VALUE;
    private int lastChunkZ = Integer.MAX_VALUE;
    private boolean lastIsSlime = false;

    @Override
    public String getName() {
        return "World";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showWorld;
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
            BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

            if (mc.isSingleplayer() && mc.getIntegratedServer() != null) {
                long[] times = mc.getIntegratedServer().tickTimeArray;
                long sum = 0;
                for (long t : times) sum += t;
                float tickTime = (float)(sum / (double)times.length) * 1.0E-6F;
                float tps = (tickTime > 0) ? Math.min(20.0F, 1000.0F / tickTime) : 20.0F;
                int tpsColor = tps > 18 ? 0x55FF55 : (tps > 12 ? 0xFFFF55 : 0xFF5555);

                cachedLines.add(new InfoLine("Server: ", String.format("%.1f TPS (%.2fms)", tps, tickTime), tpsColor));
            }

            cachedLines.add(new InfoLine("Biome: ", mc.world.getBiome(pos).getBiomeName(), ModConfig.colors.colorBiome));

            float diff = mc.world.getDifficultyForLocation(pos).getAdditionalDifficulty();
            long days = mc.world.getWorldTime() / 24000L;
            cachedLines.add(new InfoLine("Local Difficulty: ", String.format("%.2f (Day %d)", diff, days), 0xFFFFFF));
            
            lastUpdateTick = currentTick;
        }
        
        return cachedLines;
    }

    private boolean checkSlimeChunk(int x, int z) {
        Random rnd = new Random(mc.world.getSeed() + (long) (x * x * 4987142) + (long) (x * 5947611) + (long) (z * z * 4392871L) + (long) (z * 389711) ^ 987234911L);
        return rnd.nextInt(10) == 0;
    }

    private String getFacingString(float yaw) {
        int i = MathHelper.floor((double)(yaw * 4.0F / 360.0F) + 0.5D) & 3;
        String[] directions = {"South (+Z)", "West (-X)", "North (-Z)", "East (+X)"};
        return directions[i];
    }
}
