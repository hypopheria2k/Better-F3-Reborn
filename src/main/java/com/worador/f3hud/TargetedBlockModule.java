package com.worador.f3hud;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
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

        // 1. Item Despawn Timer (Wenn man ein Item anschaut)
        if (rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.ENTITY && rayTrace.entityHit instanceof EntityItem) {
            EntityItem entityItem = (EntityItem) rayTrace.entityHit;

            // 'age' ist private in EntityItem. Wir nutzen ReflectionHelper von Forge.
            // Die Obfuscated-Namen für 1.12.2 sind "field_70292_b" oder einfach "age"
            int age = ReflectionHelper.getPrivateValue(EntityItem.class, entityItem, "field_70292_b", "age");

            int remainingTicks = 6000 - age;
            int seconds = Math.max(0, remainingTicks / 20);
            lines.add(new InfoLine("Despawn in: ", seconds + "s", 0xFFAA00));
        }

        // 2. Block Informationen
        if (rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = rayTrace.getBlockPos();
            IBlockState state = mc.world.getBlockState(pos);
            Block block = state.getBlock();

            // Position
            lines.add(new InfoLine("Looking at: ", String.format("%d, %d, %d", pos.getX(), pos.getY(), pos.getZ()), ModConfig.colors.colorChunk));

            // Block Name & ID
            String blockId = Block.REGISTRY.getNameForObject(block).toString();
            lines.add(new InfoLine("Block: ", blockId, 0xFFFFFF));

            // Block States (Properties)
            for (Map.Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet()) {
                String propertyName = entry.getKey().getName();
                String valueName = entry.getValue().toString();
                lines.add(new InfoLine("  " + propertyName + ": ", valueName, 0xAAAAAA));
            }

            // Enchantment Power (Bücherregal-Check)
            if (block == Blocks.ENCHANTING_TABLE) {
                int bookshelfCount = countBookshelves(mc.world, pos);
                int color = (bookshelfCount >= 15) ? 0x55FF55 : 0xFFAA00;
                lines.add(new InfoLine("Bookshelves: ", bookshelfCount + "/15", color));
            }

        } else if (lines.isEmpty()) {
            lines.add(new InfoLine("Targeted: ", "None", 0x555555));
        }

        return lines;
    }

    private int countBookshelves(net.minecraft.world.World world, BlockPos tablePos) {
        int count = 0;
        // Scannt den Standard-Bereich für den Zaubertisch (1 Block Abstand, 2 hoch)
        for (int x = -1; x <= 1; ++x) {
            for (int z = -1; z <= 1; ++z) {
                if ((x != 0 || z != 0) && world.isAirBlock(tablePos.add(x, 0, z)) && world.isAirBlock(tablePos.add(x, 1, z))) {
                    for (int y = 0; y <= 1; ++y) {
                        if (world.getBlockState(tablePos.add(x * 2, y, z * 2)).getBlock() == Blocks.BOOKSHELF) count++;
                        if (x != 0 && z != 0) {
                            if (world.getBlockState(tablePos.add(x * 2, y, z)).getBlock() == Blocks.BOOKSHELF) count++;
                            if (world.getBlockState(tablePos.add(x, y, z * 2)).getBlock() == Blocks.BOOKSHELF) count++;
                        }
                    }
                }
            }
        }
        return count;
    }
}