package com.worador.f3hud.util;

import com.worador.f3hud.ModConfig;
import net.minecraft.client.gui.Gui;

public class RenderUtils {
    public static void drawComponentBackground(int x, int y, int width, int height, float animProgress) {
        if (!ModConfig.animation.showTextBackground) return;

        int bgColor = 0x000000; // Fallback
        try {
            String hex = ModConfig.animation.textBackgroundColor.replace("0x", "");
            bgColor = Integer.parseInt(hex, 16);
        } catch (NumberFormatException e) {
            // Logge den Fehler optional, aber crashe nicht
        }

        int alpha = (int) (animProgress * ModConfig.animation.textBackgroundAlpha) << 24;
        int finalColor = alpha | (bgColor & 0xFFFFFF);

        Gui.drawRect(x - 2, y - 1, x + width + 2, y + height - 1, finalColor);
    }
}