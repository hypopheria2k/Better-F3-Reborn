package com.worador.f3hud;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class CompassModule extends InfoModule {
    
    @Override
    public String getName() {
        return "Compass";
    }
    
    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showCompass;
    }
    
    @Override
    public List<InfoLine> getLines() {
        // Compass wird separat gerendert, nicht als Text-Zeile
        return new ArrayList<>();
    }
    
    // Spezielles Rendering für den Kompass
    public void renderCompass(int x, int y, float animProgress) {
        if (mc.player == null || !isEnabledInConfig()) return;
        
        GlStateManager.pushMatrix();
        
        int compassWidth = 120;
        int compassHeight = 20;
        
        // Background
        int bgAlpha = (int)(animProgress * 180) << 24;
        Gui.drawRect(x, y, x + compassWidth, y + compassHeight, bgAlpha | 0x000000);
        
        // Border
        drawHollowRect(x, y, x + compassWidth, y + compassHeight, 0xFF555555);
        
        // Player Yaw
        float yaw = mc.player.rotationYaw % 360;
        if (yaw < 0) yaw += 360;
        
        // Zeichne Kompass-Markierungen
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        int[] angles = {0, 45, 90, 135, 180, 225, 270, 315};
        
        int centerX = x + compassWidth / 2;
        int centerY = y + compassHeight / 2;
        
        for (int i = 0; i < directions.length; i++) {
            float angle = angles[i];
            float diff = angle - yaw;
            
            // Normalisiere Winkel
            while (diff > 180) diff -= 360;
            while (diff < -180) diff += 360;
            
            // Position auf dem Kompass
            float posX = centerX + (diff / 90.0f) * 30.0f; // 30 pixels pro 90°
            
            // Nur zeichnen wenn sichtbar
            if (posX >= x + 10 && posX <= x + compassWidth - 10) {
                int color;
                int size = 8;
                
                // Haupt-Richtungen hervorheben
                if (i % 2 == 0) { // N, E, S, W
                    color = 0xFFFFFF;
                    size = 10;
                    
                    // Nord rot hervorheben
                    if (i == 0) {
                        color = 0xFF5555;
                        size = 12;
                    }
                } else { // NE, SE, SW, NW
                    color = 0xAAAAAA;
                }
                
                // Text zeichnen
                String dir = directions[i];
                int textWidth = mc.fontRenderer.getStringWidth(dir);
                mc.fontRenderer.drawStringWithShadow(dir, (int)posX - textWidth / 2, centerY - 3, color);
            }
        }
        
        // Zentrale Markierung (Player Position)
        Gui.drawRect(centerX - 1, y + 2, centerX + 1, y + compassHeight - 2, 0xFFFFFFFF);
        
        // Yaw Anzeige
        String yawText = String.format("%.1f°", yaw);
        int yawWidth = mc.fontRenderer.getStringWidth(yawText);
        mc.fontRenderer.drawStringWithShadow(yawText, x + compassWidth - yawWidth - 2, y + 2, 0xFFFFFF);
        
        GlStateManager.popMatrix();
    }
    
    private void drawHollowRect(int left, int top, int right, int bottom, int color) {
        Gui.drawRect(left, top, right, top + 1, color);
        Gui.drawRect(left, bottom - 1, right, bottom, color);
        Gui.drawRect(left, top, left + 1, bottom, color);
        Gui.drawRect(right - 1, top, right, bottom, color);
    }
}