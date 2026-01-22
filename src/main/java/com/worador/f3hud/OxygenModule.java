package com.worador.f3hud;

import net.minecraft.block.material.Material;
import java.util.ArrayList;
import java.util.List;

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
        if (mc.player == null) return lines;

        int air = mc.player.getAir();
        // 300 Ticks ist das Maximum in 1.12.2
        int maxAir = 300;

        // Anzeige triggern, wenn im Wasser oder wenn die Lunge nicht voll ist
        if (mc.player.isInsideOfMaterial(Material.WATER) || air < maxAir) {
            double seconds = Math.max(0, air / 20.0);
            int percent = (int) ((air / (float) maxAir) * 100);

            // Farbe: Blau -> Gelb (< 50%) -> Rot (< 20%)
            int color;
            if (percent < 20) color = 0xFF5555;      // Kritisch
            else if (percent < 50) color = 0xFFFF55; // Warnung
            else color = 0x55FFFF;                   // Sicher

            // 1. Balken-Anzeige für schnelles Erfassen
            lines.add(new InfoLine("O2: ", getAirBar(percent) + " " + percent + "%", color));

            // 2. Zeit-Anzeige mit Status (Drowning vs. Recovering)
            String status = mc.player.isInsideOfMaterial(Material.WATER) ? "Drowning" : "Recovering";
            lines.add(new InfoLine("Air: ", String.format("%.1fs ", seconds) + "[" + status + "]", color));

            // 3. Warnung wenn Luft fast weg
            if (air <= 0) {
                lines.add(new InfoLine("WARNING: ", "NO OXYGEN!", 0xAA0000));
            }
        }

        return lines;
    }

    private String getAirBar(int percent) {
        int bars = 10;
        int filled = Math.max(0, Math.min(10, percent / 10));
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < bars; i++) {
            // Wir nutzen ein spezielles Zeichen für Luftblasen (Punkt oder vertikaler Strich)
            sb.append(i < filled ? "o" : ".");
        }
        sb.append("]");
        return sb.toString();
    }
}