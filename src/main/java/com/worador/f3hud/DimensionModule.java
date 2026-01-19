package com.worador.f3hud;

import java.util.ArrayList;
import java.util.List;

public class DimensionModule extends InfoModule {
    
    @Override
    public String getName() {
        return "Dimension";
    }
    
    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showDimension;
    }
    
    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        
        if (mc.player == null || mc.world == null) return lines;
        
        int dimension = mc.world.provider.getDimension();
        String dimName;
        
        switch (dimension) {
            case -1:
                dimName = "minecraft:the_nether";
                break;
            case 0:
                dimName = "minecraft:overworld";
                break;
            case 1:
                dimName = "minecraft:the_end";
                break;
            default:
                dimName = "minecraft:dimension_" + dimension;
                break;
        }
        
        lines.add(new InfoLine("Dimension: ", dimName, ModConfig.colors.colorDimension));
        
        return lines;
    }
} 
