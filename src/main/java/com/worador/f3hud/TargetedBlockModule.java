package com.worador.f3hud;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TargetedBlockModule extends InfoModule {

    @Override
    public String getName() {
        return "Targeted Block";
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

            // 1. Position (Kompakter)
            lines.add(new InfoLine("Looking at: ", String.format("%d, %d, %d", pos.getX(), pos.getY(), pos.getZ()), ModConfig.colors.colorChunk));

            // 2. Block Name & ID
            String blockId = Block.REGISTRY.getNameForObject(block).toString();
            lines.add(new InfoLine("Block: ", blockId, 0xFFFFFF));

            // 3. Block States (Das Fehlende Herzstück!)
            // Zeigt z.B. variant=spruce, facing=north, etc.
            for (Map.Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet()) {
                String propertyName = entry.getKey().getName();
                String valueName = entry.getValue().toString();
                lines.add(new InfoLine("  " + propertyName + ": ", valueName, 0xAAAAAA));
            }

        } else {
            lines.add(new InfoLine("Targeted Block: ", "None", 0x555555));
        }

        return lines;
    }
} 
