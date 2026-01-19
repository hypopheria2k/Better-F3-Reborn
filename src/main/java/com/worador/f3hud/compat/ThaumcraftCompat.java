package com.worador.f3hud.compat;

import com.worador.f3hud.InfoModule.InfoLine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.EnumHand;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

import java.util.ArrayList;
import java.util.List;

public class ThaumcraftCompat {

    public static List<InfoLine> getThaumcraftLines(EntityPlayer player, World world) {
        List<InfoLine> lines = new ArrayList<>();
        if (player == null || world == null) return lines;

        // 1. Aura (Vis & Flux)
        try {
            BlockPos pos = player.getPosition();
            float vis = AuraHelper.getVis(world, pos);
            lines.add(new InfoLine("Aura Vis: ", String.format("%.2f", vis), 0x9345AA));

            float flux = AuraHelper.getFlux(world, pos);
            if (flux > 0.1f) {
                lines.add(new InfoLine("Aura Flux: ", String.format("%.2f", flux), 0xFF00FF));
            }
        } catch (Exception e) {}

        // 2. Warp Status
        try {
            IPlayerWarp warpCap = player.getCapability(ThaumcraftCapabilities.WARP, null);
            if (warpCap != null) {
                int totalWarp = warpCap.get(IPlayerWarp.EnumWarpType.PERMANENT) +
                        warpCap.get(IPlayerWarp.EnumWarpType.NORMAL) +
                        warpCap.get(IPlayerWarp.EnumWarpType.TEMPORARY);
                if (totalWarp > 0) {
                    lines.add(new InfoLine("Warp Level: ", String.valueOf(totalWarp), 0x4B0082));
                }
            }
        } catch (Exception e) {}

        // 3. Item Aspekte (Direkt aus den NBT-Daten lesen)
        try {
            ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
            if (!stack.isEmpty()) {
                AspectList aspects = new AspectList();
                // Lädt die Aspekte direkt aus dem Item-NBT
                aspects.readFromNBT(stack.getTagCompound());

                if (aspects.size() > 0) {
                    for (Aspect aspect : aspects.getAspects()) {
                        if (aspect != null) {
                            int amount = aspects.getAmount(aspect);
                            lines.add(new InfoLine("Aspect (" + aspect.getName() + "): ",
                                    String.valueOf(amount), aspect.getColor()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Falls das Item keine Aspekte hat
        }

        return lines;
    }
} 
