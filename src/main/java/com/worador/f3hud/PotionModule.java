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
        return ModConfig.modules.showPotion;
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

            // Name lokalisieren & Level hinzufügen
            String name = I18n.format(potion.getName());
            if (effect.getAmplifier() > 0) {
                name += " " + (effect.getAmplifier() + 1); // Einfache Zahlen sind im HUD cleaner als Römisch
            }

            // Dauer formatieren: M:SS
            String durationStr = formatDuration(durationTicks);
            if (effect.getIsAmbient()) durationStr = "Amb"; // Für Beacon/Ambient Effekte kürzen

            // Farbe optimieren: LiquidColor ist oft zu dunkel
            int color = potion.getLiquidColor();
            if (color == 0) color = 0xFFFFFF; // Fallback für unsichtbare Farben

            // Blinken bei weniger als 10 Sekunden
            if (durationTicks < 200 && (mc.world.getTotalWorldTime() % 20 < 10)) {
                color = 0xFF5555; // Rot-Warnung
            }

            // Markierung für schädliche Effekte
            String prefix = potion.isBadEffect() ? TextFormatting.RED + "!" + TextFormatting.RESET + " " : "";

            lines.add(new InfoLine(prefix + name + ": ", durationStr, color));
        }

        return lines;
    }

    private String formatDuration(int ticks) {
        if (ticks > 32000) return "**:**"; // Unendliche Effekte
        int seconds = ticks / 20;
        int m = seconds / 60;
        int s = seconds % 60;
        return String.format("%d:%02d", m, s);
    }
}