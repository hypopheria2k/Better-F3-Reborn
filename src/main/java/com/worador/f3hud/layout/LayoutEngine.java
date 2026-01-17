package com.worador.f3hud.layout;

import com.worador.f3hud.ModuleRegistry;
import com.worador.f3hud.InfoModule;
import java.util.List;

public class LayoutEngine {
    public static float leftScale = 1.0f;
    public static float rightScale = 1.0f;

    // Muss mit dem Wert im DebugRenderer (0.8f) übereinstimmen
    private static final float FONT_SCALE = 0.8f;

    public static void update(int screenWidth) {
        // Wir berechnen die Breite basierend auf der tatsächlichen (verkleinerten) Schrift
        int leftMax = (int) (getMaxWidth(ModuleRegistry.getLeftModules()) * FONT_SCALE);
        int rightMax = (int) (getMaxWidth(ModuleRegistry.getRightModules()) * FONT_SCALE);

        // 40% vom Bildschirm für jede Seite
        float maxAllowedWidth = screenWidth * 0.40f;

        // Falls die verkleinerte Schrift immer noch zu breit ist, greift das Scaling
        float targetLeft = (leftMax > maxAllowedWidth) ? maxAllowedWidth / leftMax : 1.0f;
        float targetRight = (rightMax > maxAllowedWidth) ? maxAllowedWidth / rightMax : 1.0f;

        // Sanfte Dämpfung
        leftScale += (targetLeft - leftScale) * 0.05f;
        rightScale += (targetRight - rightScale) * 0.05f;

        // Präzisions-Fix
        if (Math.abs(leftScale - targetLeft) < 0.001f) leftScale = targetLeft;
        if (Math.abs(rightScale - targetRight) < 0.001f) rightScale = targetRight;

        // Hard-Limit nach unten
        if (leftScale < 0.5f) leftScale = 0.5f;
        if (rightScale < 0.5f) rightScale = 0.5f;
    }

    private static int getMaxWidth(List<InfoModule> modules) {
        int max = 0;
        for (InfoModule m : modules) {
            // Holt die originale Breite (Schriftgröße 1.0) aus dem Modul
            int w = m.getMaxLineWidth();
            if (w > max) max = w;
        }
        return max;
    }
}