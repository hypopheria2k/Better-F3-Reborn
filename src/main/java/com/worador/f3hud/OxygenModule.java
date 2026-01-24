package com.worador.f3hud;

import net.minecraft.block.material.Material;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OxygenModule extends InfoModule {

    @Override
    public String getName() {
        return "Oxygen";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showOxygen;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (!isEnabledInConfig() || mc.player == null) return lines;

        int air = mc.player.getAir();
        // In 1.12.2 sind 300 Ticks (15 Sek) das Maximum
        int maxAir = 300;

        // Nur anzeigen, wenn der Kopf im Wasser ist oder man gerade Luft holt
        if (mc.player.isInsideOfMaterial(Material.WATER) || air < maxAir) {

            double seconds = Math.max(0, air / 20.0);
            int percent = (int) ((air / (float) maxAir) * 100);

            // Farbe nach Dringlichkeit
            int color = 0x55FFFF; // Blau (Sicher)
            if (percent < 25) color = 0xFF5555;      // Rot (Kritisch)
            else if (percent < 60) color = 0xFFAA00; // Orange (Warnung)

            // Eine kompakte Zeile: "O2: 12.5s (80%)"
            // Das reicht völlig aus, um die Situation zu erfassen.
            String info = String.format(Locale.US, "%.1fs (%d%%)", seconds, percent);

            // Wenn der Spieler ertrinkt (Luft = 0), Text hervorheben
            if (air <= 0) {
                info = "!!! DROWNING !!!";
            }

            lines.add(new InfoLine("O2: ", info, color));
        }

        return lines;
    }
}