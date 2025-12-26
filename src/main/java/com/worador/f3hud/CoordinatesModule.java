package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;

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
        
        if (mc.player == null) return lines;
        
        BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        
        // Block Coordinates - nutzt Config-Farbe
        String blockCoords = blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ();
        lines.add(new InfoLine("Block: ", blockCoords, ModConfig.colors.colorBlock));
        
        // XYZ (Custom rendering mit Config-Farben)
        lines.add(new InfoLine("", "", 0xFFFFFF)); 
        
        // Chunk Coordinates - nutzt Config-Farbe
        int chunkX = blockPos.getX() >> 4;
        int chunkY = blockPos.getY() >> 4;
        int chunkZ = blockPos.getZ() >> 4;
        String chunkCoords = chunkX + " " + chunkY + " " + chunkZ;
        lines.add(new InfoLine("Chunk Coordinates: ", chunkCoords, ModConfig.colors.colorChunk));
        
        // Chunk Relative Position
        int relX = blockPos.getX() & 15;
        int relY = blockPos.getY() & 15;
        int relZ = blockPos.getZ() & 15;
        String relCoords = relX + " " + relY + " " + relZ;
        lines.add(new InfoLine("Chunk Relative: ", relCoords, ModConfig.colors.colorChunk));
        
        return lines;
    }
}