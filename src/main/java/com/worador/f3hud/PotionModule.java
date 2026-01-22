package com.worador.f3hud;

import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PotionModule extends InfoModule {

    @Override
    public String getName() {
        return "Potions";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showPotions;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null) return lines;

        Collection<PotionEffect> effects = mc.player.getActivePotionEffects();
        if (effects.isEmpty()) return lines;

        for (PotionEffect effect : effects) {
            Potion potion = effect.getPotion();
            int durationTicks = effect.getDuration();

            // Name lokalisieren
            String name = I18n.format(potion.getName());

            // Level (z.B. II)
            if (effect.getAmplifier() > 0) {
                name += " " + I18n.format("enchantment.level." + (effect.getAmplifier() + 1));
            }

            // Dauer String
            String durationStr = Potion.getPotionDurationString(effect, 1.0F);

            // --- AUFWERTUNG: Dynamische Warnung ---
            int color = potion.getLiquidColor();

            // Wenn der Effekt weniger als 10 Sekunden (200 Ticks) läuft, lassen wir ihn blinken
            if (durationTicks < 200) {
                // Simples Blinken basierend auf der Systemzeit
                if ((System.currentTimeMillis() / 500) % 2 == 0) {
                    color = 0xFF5555; // Wechselt zu hellem Rot
                }
            }

            // Markierung für schädliche Effekte (Deutsches Verhalten: Klare Ansage)
            String prefix = "";
            if (potion.isBadEffect()) {
                prefix = TextFormatting.RED + "[!] " + TextFormatting.RESET;
            }

            lines.add(new InfoLine(prefix + name + ": ", durationStr, color));
        }

        return lines;
    }
}