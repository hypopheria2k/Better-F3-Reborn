package com.worador.f3hud.util;

import com.worador.f3hud.ModConfig;
import net.minecraft.client.gui.Gui;

public class RenderUtils {
    public static void drawComponentBackground(int x, int y, int width, int height, float animProgress) {
        if (!ModConfig.animation.showTextBackground) return;

        // KEIN Parsen mehr nötig! Wir nutzen direkt den int aus der Config.
        int bgColor = ModConfig.animation.textBackgroundColor;

        // Alpha-Berechnung
        int alpha = (int) (animProgress * ModConfig.animation.textBackgroundAlpha) << 24;

        // Farbe kombinieren (Bitwise OR)
        int finalColor = alpha | (bgColor & 0xFFFFFF);

        // Hintergrund zeichnen
        Gui.drawRect(x - 2, y - 1, x + width + 2, y + height - 1, finalColor);
    }
}