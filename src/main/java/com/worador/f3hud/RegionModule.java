package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class RegionModule extends InfoModule {
    
    @Override
    public String getName() {
        return "Region";
    }
    
    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showRegion;
    }
    
    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        
        if (mc.player == null) return lines;
        
        BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        
        // Region File Koordinaten
        int regionX = (pos.getX() >> 4) >> 5;
        int regionZ = (pos.getZ() >> 4) >> 5;
        
        // Lokale Chunk Position innerhalb der Region
        int localChunkX = (pos.getX() >> 4) & 31;
        int localChunkZ = (pos.getZ() >> 4) & 31;
        
        String regionFile = String.format("r.%d.%d.mca (%d, %d)", regionX, regionZ, localChunkX, localChunkZ);
        lines.add(new InfoLine("Region File: ", regionFile, ModConfig.colors.colorDefault));
        
        return lines;
    }
}