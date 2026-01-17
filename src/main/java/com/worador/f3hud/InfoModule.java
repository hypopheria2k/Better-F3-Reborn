package com.worador.f3hud;

import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;

public abstract class InfoModule {
    protected final Minecraft mc = Minecraft.getMinecraft();
    public boolean enabled = true;
    
    // Jedes Modul gibt seine Zeilen zurück
    public abstract List<InfoLine> getLines();
    
    // Name des Moduls (für Config später)
    public abstract String getName();
    
    // Prüft ob Modul aktiviert ist (aus Config)
    public boolean isEnabled() {
        return enabled && isEnabledInConfig();
    }
    
    // Überschreibe das in Subklassen um Config zu checken
    protected boolean isEnabledInConfig() {
        return true; // Default: immer an
    }
    
    // Helper-Klasse für eine Info-Zeile
    public static class InfoLine {
        public String label;
        public String value;
        public int color;
        
        public InfoLine(String label, String value, int color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }
    }

    public int getHeight() {
        // Standard: Anzahl der Zeilen * 11 Pixel + 5 Pixel Abstand
        return getLines().size() * 11 + 5;
    }
    public int getMaxLineWidth() {
        int max = 0;
        List<InfoLine> lines = getLines(); // Einmal holen statt mehrfach getLines() aufrufen
        if (lines == null || lines.isEmpty()) return 0;

        for (InfoLine line : lines) {
            if (line == null) continue;
            int w = Minecraft.getMinecraft().fontRenderer.getStringWidth(line.label + line.value);
            if (w > max) max = w;
        }
        return max;
    }

}