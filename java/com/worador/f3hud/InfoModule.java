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
}