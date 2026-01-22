package com.worador.f3hud;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.List;

public class DurabilityModule extends InfoModule {
    @Override
    public String getName() { return "Durability"; }

    @Override
    protected boolean isEnabledInConfig() { return ModConfig.modules.showDurability; }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null) return lines;

        // 1. Ausrüstung in den Händen (Main & Offhand)
        addDurabilityLine(lines, mc.player.getHeldItemMainhand(), "Main: ");
        addDurabilityLine(lines, mc.player.getHeldItemOffhand(), "Off: ");

        // 2. Rüstung (Helm bis Schuhe)
        NonNullList<ItemStack> armor = mc.player.inventory.armorInventory;
        String[] armorNames = {"Boots", "Legs", "Chest", "Helm"};

        for (int i = 3; i >= 0; i--) {
            ItemStack stack = armor.get(i);
            if (!stack.isEmpty()) {
                // Wir nutzen hier kurze Bezeichnungen für mehr Platz im HUD
                addDurabilityLine(lines, stack, armorNames[i] + ": ");
            }
        }

        return lines;
    }

    private void addDurabilityLine(List<InfoLine> lines, ItemStack stack, String prefix) {
        if (!stack.isEmpty() && stack.isItemStackDamageable()) {
            int maxDur = stack.getMaxDamage();
            int currentDur = maxDur - stack.getItemDamage();
            double percent = (currentDur * 100.0) / maxDur;

            // Deutsches Verhalten: Nur anzeigen, wenn relevant (z.B. < 90% Haltbarkeit)
            // Das hält dein HUD sauber auf deinem Manjaro-System.
            if (percent > 90.0 && !prefix.startsWith("Main")) return;

            // Farblogik & Warn-Style
            int color = 0x55FF55; // GRÜN
            String style = "";

            if (percent <= 15.0) {
                color = 0xFF5555; // ROT
                style = TextFormatting.BOLD.toString() + TextFormatting.UNDERLINE.toString();
            } else if (percent <= 40.0) {
                color = 0xFFAA00; // GOLD
            } else if (percent <= 70.0) {
                color = 0xFFFF55; // GELB
            }

            // Visueller Balken zur schnellen Erfassung
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