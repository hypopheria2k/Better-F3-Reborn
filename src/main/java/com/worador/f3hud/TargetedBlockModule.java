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

    @Override public String getName() { return "Targeted Block"; }
    @Override protected boolean isEnabledInConfig() { return ModConfig.modules.showTargetedBlock; }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (!isEnabledInConfig() || mc.player == null || mc.world == null) return lines;

        RayTraceResult rt = mc.objectMouseOver;
        if (rt == null || rt.typeOfHit == RayTraceResult.Type.MISS) {
            lines.add(new InfoLine("Targeted: ", "None", 0x555555));
            return lines;
        }

        // 1. Entity Check (Items)
        if (rt.typeOfHit == RayTraceResult.Type.ENTITY && rt.entityHit instanceof EntityItem) {
            EntityItem item = (EntityItem) rt.entityHit;
            // Nutzt Obfuscated Name für 1.12.2 (age)
            int age = ReflectionHelper.getPrivateValue(EntityItem.class, item, "field_70292_b", "age");
            int remaining = Math.max(0, (6000 - age) / 20);
            lines.add(new InfoLine("Despawn in: ", remaining + "s", 0xFFAA00));
            lines.add(new InfoLine("Item: ", item.getItem().getDisplayName(), 0xFFFFFF));
        }

        // 2. Block Check
        if (rt.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = rt.getBlockPos();
            IBlockState state = mc.world.getBlockState(pos);
            Block block = state.getBlock();

            lines.add(new InfoLine("Block: ", Block.REGISTRY.getNameForObject(block).toString(), 0xFFFFFF));
            lines.add(new InfoLine("At: ", pos.getX() + ", " + pos.getY() + ", " + pos.getZ(), 0xAAAAAA));

            // Properties & Growth
            for (Map.Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet()) {
                String name = entry.getKey().getName();
                String value = entry.getValue().toString();

                if ("age".equals(name) && entry.getValue() instanceof Integer) {
                    int curAge = (Integer) entry.getValue();
                    Integer maxAge = getMaxAgeForBlock(block);
                    if (maxAge != null) {
                        String info = curAge + "/" + maxAge;
                        if (curAge < maxAge) {
                            // Schätzung: 1.12.2 Durchschnitt ca. 5 Min pro Stufe bei Weizen/etc.
                            int minLeft = (maxAge - curAge) * 5;
                            info += " (~" + minLeft + "m left)";
                        }
                        lines.add(new InfoLine("Growth: ", info, 0x55FF55));
                        continue;
                    }
                }
                lines.add(new InfoLine("  " + name + ": ", value, 0x888888));
            }

            // Bookshelves für Enchanting Table
            if (block == Blocks.ENCHANTING_TABLE) {
                int shelves = countBookshelves(pos);
                lines.add(new InfoLine("Bookshelves: ", shelves + "/15", shelves >= 15 ? 0x55FF55 : 0xFFAA00));
            }
        }

        return lines;
    }

    private int countBookshelves(BlockPos tablePos) {
        int count = 0;
        for (int zi = -1; zi <= 1; ++zi) {
            for (int xi = -1; xi <= 1; ++xi) {
                if ((zi != 0 || xi != 0) && mc.world.isAirBlock(tablePos.add(xi, 0, zi)) && mc.world.isAirBlock(tablePos.add(xi, 1, zi))) {
                    for (int yi = 0; yi <= 1; ++yi) {
                        if (mc.world.getBlockState(tablePos.add(xi * 2, yi, zi * 2)).getBlock() == Blocks.BOOKSHELF) count++;
                        if (xi != 0 && zi != 0) {
                            if (mc.world.getBlockState(tablePos.add(xi * 2, yi, zi)).getBlock() == Blocks.BOOKSHELF) count++;
                            if (mc.world.getBlockState(tablePos.add(xi, yi, zi * 2)).getBlock() == Blocks.BOOKSHELF) count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private Integer getMaxAgeForBlock(Block block) {
        if (block instanceof net.minecraft.block.BlockCrops) return ((net.minecraft.block.BlockCrops)block).getMaxAge();
        if (block == Blocks.NETHER_WART) return 3;
        if (block == Blocks.COCOA) return 2;
        if (block == Blocks.CACTUS || block == Blocks.REEDS) return 15;
        return null;
    }
}