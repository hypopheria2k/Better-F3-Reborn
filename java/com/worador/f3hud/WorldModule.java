package com.worador.f3hud;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldModule extends InfoModule {
    
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
        
        // Chunks & Sections
        lines.add(new InfoLine("Chunk Sections: ", mc.renderGlobal.getDebugInfoRenders(), ModConfig.colors.colorChunk));
        
        // Facing Direction
        String facing = getFacingString();
        lines.add(new InfoLine("Facing: ", facing, ModConfig.colors.colorDefault));
        
        // Light
        BlockPos eyePos = pos.up();
        int lightSky = mc.world.getLightFor(EnumSkyBlock.SKY, eyePos);
        int lightBlock = mc.world.getLightFor(EnumSkyBlock.BLOCK, eyePos);
        int lightTotal = mc.world.getLight(eyePos);
        lines.add(new InfoLine("Light: Total: ", lightTotal + " Sky: " + lightSky + " Block: " + lightBlock, ModConfig.colors.colorLight));
        
        // Light Server (falls auf Server)
        if (!mc.isSingleplayer()) {
            lines.add(new InfoLine("Light (Server): ", "Sky: " + lightSky + " Block: " + lightBlock, ModConfig.colors.colorLight));
        }
        
        // Biome (mit Biom-Farbe!)
        Biome biome = mc.world.getBiome(pos);
        int biomeColor = getBiomeColor(biome);
        lines.add(new InfoLine("Biome: ", biome.getBiomeName(), biomeColor));
        
        // Time of Day (NEU!)
        String timeOfDay = getTimeOfDay();
        lines.add(new InfoLine("Time: ", timeOfDay, ModConfig.colors.colorDefault));
        
        // Days Played
        long worldTime = mc.world.getWorldTime();
        long days = worldTime / 24000L;
        lines.add(new InfoLine("Days Played: ", String.valueOf(days), ModConfig.colors.colorDefault));
        
        // Weather (NEU!)
        String weather = getWeather();
        lines.add(new InfoLine("Weather: ", weather, ModConfig.colors.colorDefault));
        
        // Slime Chunk
        Chunk chunk = mc.world.getChunkFromBlockCoords(pos);
        boolean isSlime = isSlimeChunk(chunk.x, chunk.z);
        lines.add(new InfoLine("Slime Chunk: ", String.valueOf(isSlime), isSlime ? ModConfig.colors.colorBiome : 0xFF5555));
        
        return lines;
    }
    
    private boolean isSlimeChunk(int x, int z) {
        Random rnd = new Random(mc.world.getSeed() + (long) (x * x * 4987142) + (long) (x * 5947611) + (long) (z * z * 4392871L) + (long) (z * 389711) ^ 987234911L);
        return rnd.nextInt(10) == 0;
    }
    
    private String getFacingString() {
        float yaw = mc.player.rotationYaw % 360;
        if (yaw < 0) yaw += 360;
        int facing = MathHelper.floor((double) (yaw * 4.0F / 360.0F) + 0.5D) & 3;
        String[] directions = {"North (Towards negative Z)", "East (Towards positive X)", 
                               "South (Towards positive Z)", "West (Towards negative X)"};
        return directions[facing];
    }
    
    // NEU: Time of Day mit Uhrzeit
    private String getTimeOfDay() {
        long worldTime = mc.world.getWorldTime();
        long dayTime = worldTime % 24000L;
        
        // Minecraft Zeit → echte Zeit
        // 0 = 6:00, 6000 = 12:00, 12000 = 18:00, 18000 = 0:00
        int hours = (int) ((dayTime / 1000 + 6) % 24);
        int minutes = (int) ((dayTime % 1000) * 60 / 1000);
        
        return String.format("Day %d, %02d:%02d", (worldTime / 24000L), hours, minutes);
    }
    
    // NEU: Weather Status
    private String getWeather() {
        if (mc.world.isThundering()) {
            return "Thunder";
        } else if (mc.world.isRaining()) {
            return "Rain";
        } else {
            return "Clear";
        }
    }
    
    // NEU: Biome Colors
    private int getBiomeColor(Biome biome) {
        String biomeName = biome.getBiomeName().toLowerCase();
        
        // Wasser-Biome (Blau/Cyan)
        if (biomeName.contains("ocean") || biomeName.contains("river")) {
            return 0x5555FF;
        }
        // Wüsten (Gelb)
        else if (biomeName.contains("desert") || biomeName.contains("mesa") || biomeName.contains("savanna")) {
            return 0xFFFF55;
        }
        // Schnee/Eis (Hellblau/Weiß)
        else if (biomeName.contains("ice") || biomeName.contains("frozen") || biomeName.contains("cold") || biomeName.contains("taiga")) {
            return 0xAAFFFF;
        }
        // Dschungel (Dunkelgrün)
        else if (biomeName.contains("jungle")) {
            return 0x00AA00;
        }
        // Sumpf (Dunkelgrün/Braun)
        else if (biomeName.contains("swamp")) {
            return 0x557744;
        }
        // Pilz-Inseln (Lila)
        else if (biomeName.contains("mushroom")) {
            return 0xFF55FF;
        }
        // Berge (Grau)
        else if (biomeName.contains("mountain") || biomeName.contains("hill") || biomeName.contains("peak")) {
            return 0x888888;
        }
        // Nether (Rot)
        else if (biomeName.contains("nether") || biomeName.contains("hell")) {
            return 0xFF5555;
        }
        // End (Lila/Magenta)
        else if (biomeName.contains("end") || biomeName.contains("sky")) {
            return 0xAA55AA;
        }
        // Wald/Plains (Grün) - Default
        else {
            return 0x55FF55;
        }
    }
}