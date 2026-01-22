package com.worador.f3hud;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.List;

public class MachineProgressModule extends InfoModule {

    @Override
    public String getName() {
        return "Machine Progress";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showMachineProgress;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null) return lines;

        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();

            // Singleplayer-Hack: Wir holen das TE direkt vom Server-Thread
            TileEntity te = mc.world.getTileEntity(pos);
            if (mc.isSingleplayer() && mc.getIntegratedServer() != null) {
                te = mc.getIntegratedServer().getWorld(mc.player.dimension).getTileEntity(pos);
            }

            // --- OFEN LOGIK ---
            if (te instanceof TileEntityFurnace) {
                TileEntityFurnace furnace = (TileEntityFurnace) te;

                int cookTime = furnace.getField(2);
                int totalCookTime = furnace.getField(3);
                int burnTime = furnace.getField(0);

                // Slots: 0 = Input, 1 = Fuel, 2 = Output
                ItemStack inputStack = furnace.getStackInSlot(0);
                ItemStack outputStack = furnace.getStackInSlot(2);

                int percent = (totalCookTime > 0) ? Math.max(0, (cookTime * 100) / totalCookTime) : 0;

                lines.add(new InfoLine("Smelting: ", getProgressBar(percent) + " " + percent + "%", 0x55FFFF));

                if (!inputStack.isEmpty()) {
                    lines.add(new InfoLine("Input: ", inputStack.getDisplayName() + " (" + inputStack.getCount() + ")", 0xAAAAAA));
                }

                // NEU: Output Anzeige (was ist fertig)
                if (!outputStack.isEmpty()) {
                    lines.add(new InfoLine("Output: ", TextFormatting.GREEN + outputStack.getDisplayName() + " x" + outputStack.getCount(), 0x55FF55));
                }

                lines.add(new InfoLine("Fuel Ticks: ", String.valueOf(burnTime), 0xFFAA00));
            }

            // --- BRAUSTAND LOGIK ---
            if (te instanceof TileEntityBrewingStand) {
                TileEntityBrewingStand stand = (TileEntityBrewingStand) te;

                int brewTime = stand.getField(0);
                int fuel = stand.getField(1);

                int percent = (brewTime > 0) ? Math.max(0, 100 - ((brewTime * 100) / 400)) : 0;
                lines.add(new InfoLine("Brewing: ", getProgressBar(percent) + " " + percent + "%", 0xFF55FF));

                // NEU: Detaillierte Tränke-Anzeige
                int potionCount = 0;
                List<String> potionNames = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    ItemStack s = stand.getStackInSlot(i);
                    if (!s.isEmpty()) {
                        potionCount++;
                        // Wir nehmen nur den ersten Namen exemplarisch oder listen die Anzahl
                        if (potionNames.isEmpty()) potionNames.add(s.getDisplayName());
                    }
                }

                String brewInfo = potionCount + " / 3";
                if (potionCount > 0 && brewTime == 0) {
                    brewInfo += " (" + potionNames.get(0) + ")";
                }

                lines.add(new InfoLine("Bottles: ", brewInfo, 0x55FFFF));
                lines.add(new InfoLine("Blaze Fuel: ", String.valueOf(fuel), 0xFFAA00));
            }
        }
        return lines;
    }

    private String getProgressBar(int percent) {
        int bars = 10;
        int filledBars = Math.max(0, Math.min(10, percent / 10));
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < bars; i++) {
            sb.append(i < filledBars ? "|" : "-");
        }
        sb.append("]");
        return sb.toString();
    }
}