package com.worador.f3hud;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DurabilityModule extends InfoModule {
    private static final int UPDATE_INTERVAL_TICKS = 10; // 0.5 Sek reicht völlig
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
            List<InfoLine> newLines = new ArrayList<>();

            // 1. Werkzeuge in den Händen
            addDurabilityLine(newLines, mc.player.getHeldItemMainhand(), "Main: ");
            addDurabilityLine(newLines, mc.player.getHeldItemOffhand(), "Off: ");

            // 2. Rüstung (Sicherer Zugriff über die Inventory-Klasse)
            NonNullList<ItemStack> armor = mc.player.inventory.armorInventory;
            for (int i = 3; i >= 0; i--) {
                ItemStack stack = armor.get(i);
                if (!stack.isEmpty() && stack.isItemStackDamageable()) {
                    // Nutzt den kurzen Namen des Items (z.B. "Diamond Helmet")
                    String shortName = stack.getItem().getItemStackDisplayName(stack);
                    if (shortName.contains(" ")) {
                        shortName = shortName.substring(shortName.lastIndexOf(" ") + 1);
                    }
                    addDurabilityLine(newLines, stack, shortName + ": ");
                }
            }
            cachedLines = newLines;
            lastUpdateTick = currentTick;
        }
        return cachedLines;
    }

    private void addDurabilityLine(List<InfoLine> lines, ItemStack stack, String prefix) {
        if (!stack.isEmpty() && stack.isItemStackDamageable()) {
            int maxDur = stack.getMaxDamage();
            int currentDur = maxDur - stack.getItemDamage();
            double percent = (currentDur * 100.0) / maxDur;

            // Dynamische Farbwahl basierend auf Zustand
            int color = 0x55FF55; // Grün (Standard)
            String style = "";

            if (percent <= 20.0) {
                color = 0xFF5555; // Rot (Kritisch)
                style = TextFormatting.BOLD.toString();
            } else if (percent <= 50.0) {
                color = 0xFFFF55; // Gelb (Abgenutzt)
            }

            // KEINE BALKEN (|||) - Nur klare Zahlen und Prozent
            // Format: "450 / 1561 (28%)"
            String info = String.format(Locale.US, "%d / %d (%d%%)", currentDur, maxDur, (int)percent);
            lines.add(new InfoLine(style + prefix, info, color));
        }
    }
}