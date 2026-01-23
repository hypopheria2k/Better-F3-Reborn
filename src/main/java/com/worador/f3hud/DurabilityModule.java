package com.worador.f3hud;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.List;

public class DurabilityModule extends InfoModule {
    private static final int UPDATE_INTERVAL_TICKS = 5;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override
    public String getName() { return "Durability"; }

    @Override
    protected boolean isEnabledInConfig() { return ModConfig.modules.showDurability; }

    @Override
    public List<InfoLine> getLines() {
        if (!isEnabledInConfig() || mc.player == null) return new ArrayList<>();

        int currentTick = mc.player.ticksExisted;
        if (cachedLines.isEmpty() || (currentTick - lastUpdateTick) >= UPDATE_INTERVAL_TICKS) {
            cachedLines = new ArrayList<>();

            // 1. Hands (Main & Off)
            addDurabilityLine(cachedLines, mc.player.getHeldItemMainhand(), "Main: ");
            addDurabilityLine(cachedLines, mc.player.getHeldItemOffhand(), "Off: ");

            // 2. Armor (Iteriere sicher durch alle 4 Slots)
            NonNullList<ItemStack> armor = mc.player.inventory.armorInventory;
            String[] armorNames = {"Boots", "Legs", "Chest", "Helm"};

            for (int i = 3; i >= 0; i--) {
                ItemStack stack = armor.get(i);
                if (!stack.isEmpty()) {
                    addDurabilityLine(cachedLines, stack, armorNames[i] + ": ");
                }
            }
            lastUpdateTick = currentTick;
        }
        return cachedLines;
    }

    private void addDurabilityLine(List<InfoLine> lines, ItemStack stack, String prefix) {
        if (!stack.isEmpty() && stack.isItemStackDamageable()) {
            int maxDur = stack.getMaxDamage();
            int currentDur = maxDur - stack.getItemDamage();
            double percent = (currentDur * 100.0) / maxDur;

            // FIX: Wir zeigen IMMER alles an, wenn das Modul an ist.
            // Das "Ausblenden bei > 90%" war der Grund für den verschwundenen Helm.

            int color = 0x55FF55; // GRÜN
            String style = "";

            if (percent <= 15.0) {
                color = 0xFF5555; // ROT
                style = TextFormatting.BOLD.toString();
            } else if (percent <= 40.0) {
                color = 0xFFAA00; // GOLD
            } else if (percent <= 75.0) {
                color = 0xFFFF55; // GELB
            }

            String bar = getDurabilityBar(percent);
            String info = String.format("%s %d/%d (%.0f%%)", bar, currentDur, maxDur, percent);
            lines.add(new InfoLine(style + prefix, info, color));
        }
    }

    private String getDurabilityBar(double percent) {
        int barLength = 5;
        int filled = (int) Math.round(percent / 20.0);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            sb.append(i < filled ? "|" : ".");
        }
        sb.append("]");
        return sb.toString();
    }
}