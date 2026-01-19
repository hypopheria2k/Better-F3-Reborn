package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldModule extends InfoModule {

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
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null) return lines;

        BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        // 1. TPS (Nur im Singleplayer verfügbar)
        if (mc.isSingleplayer() && mc.getIntegratedServer() != null) {
            long[] times = mc.getIntegratedServer().tickTimeArray;
            long sum = 0;
            for (long t : times) sum += t;
            float tickTime = (float)(sum / (double)times.length) * 1.0E-6F;
            float tps = (tickTime > 0) ? Math.min(20.0F, 1000.0F / tickTime) : 20.0F;
            int tpsColor = tps > 18 ? 0x55FF55 : (tps > 12 ? 0xFFFF55 : 0xFF5555);

            lines.add(new InfoLine("Server: ", Math.round(tps*10)/10.0 + " TPS (" + Math.round(tickTime*100)/100.0 + "ms)", tpsColor));
        }

        // 2. LIGHT
        Chunk chunk = mc.world.getChunkFromBlockCoords(pos);
        if (chunk != null && !chunk.isEmpty()) {
            int sky = chunk.getLightFor(EnumSkyBlock.SKY, pos);
            int block = chunk.getLightFor(EnumSkyBlock.BLOCK, pos);
            int total = Math.max(sky, block);
            lines.add(new InfoLine("Light: ", total + " (Sky: " + sky + ", Block: " + block + ")", ModConfig.colors.colorLight));

            // 3. SLIME CHUNK
            if (chunk.x != lastChunkX || chunk.z != lastChunkZ) {
                lastIsSlime = checkSlimeChunk(chunk.x, chunk.z);
                lastChunkX = chunk.x;
                lastChunkZ = chunk.z;
            }
            if (lastIsSlime) {
                lines.add(new InfoLine("Slime Chunk: ", "Yes", 0x55FF55));
            }
        }

        // 4. BIOME
        lines.add(new InfoLine("Biome: ", mc.world.getBiome(pos).getBiomeName(), ModConfig.colors.colorBiome));

        // 5. DIFFICULTY & TIME
        float diff = mc.world.getDifficultyForLocation(pos).getAdditionalDifficulty();
        long days = mc.world.getWorldTime() / 24000L;
        lines.add(new InfoLine("Local Difficulty: ", Math.round(diff*100)/100.0 + " (Day " + days + ")", 0xFFFFFF));

        return lines;
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
