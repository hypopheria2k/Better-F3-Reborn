package com.worador.f3hud;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.util.ArrayList;
import java.util.List;

public class TargetedBlockModule extends InfoModule {
    
    @Override
    public String getName() {
        return "TargetedBlock";
    }
    
    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showTargetedBlock;
    }
    
    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        
        if (mc.player == null || mc.world == null) return lines;
        
        RayTraceResult rayTrace = mc.objectMouseOver;
        
        if (rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = rayTrace.getBlockPos();
            IBlockState state = mc.world.getBlockState(pos);
            Block block = state.getBlock();
            
            // Targeted Block Position
            String blockPosStr = pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
            lines.add(new InfoLine("Targeted Block: ", blockPosStr, ModConfig.colors.colorChunk));
            
            // Block ID
            String blockId = Block.REGISTRY.getNameForObject(block).toString();
            lines.add(new InfoLine("Block ID: ", blockId, ModConfig.colors.colorChunk));
            
        } else {
            lines.add(new InfoLine("Targeted Block: ", "None", 0x555555));
        }
        
        return lines;
    }
}