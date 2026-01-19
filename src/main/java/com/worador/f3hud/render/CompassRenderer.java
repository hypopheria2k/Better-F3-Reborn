package com.worador.f3hud.render;

import com.worador.f3hud.InfoModule;
import com.worador.f3hud.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class CompassRenderer implements IModuleRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(InfoModule module, int x, int y, float animProgress) {
        int width = 120;
        int height = 14;

        // Permanent: Zeichnet den Hintergrund basierend auf der allgemeinen Transparenz.
        // F3+J (showTextBackground) wird hier ignoriert.
        int alpha = (int) (animProgress * ModConfig.animation.backgroundAlpha);
        int alphaBG = alpha << 24;

        // Hintergrundbox (schwarz mit Transparenz aus Config)
        Gui.drawRect(x - 5, y - 2, x + width + 5, y + height + 2, alphaBG | 0x000000);

        float yaw = mc.player.rotationYaw % 360;
        if (yaw < 0) yaw += 360;

        String[] allDirs = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};

        for (int i = 0; i < 16; i++) {
            float angle = i * 22.5f;
            float diff = angle - yaw;
            while (diff > 180) diff -= 360;
            while (diff < -180) diff += 360;

            if (Math.abs(diff) < 50) {
                int posX = x + (width / 2) + (int) (diff * (width / 100.0));

                if (i % 4 == 0) {
                    String name = allDirs[i / 2];
                    int color = name.equals("N") ? 0xFF5555 : 0xFFFFFF;
                    mc.fontRenderer.drawStringWithShadow(name, posX - (mc.fontRenderer.getStringWidth(name) / 2), y, color);
                } else if (i % 2 == 0) {
                    String name = allDirs[i / 2];
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(0.65, 0.65, 1.0);
                    mc.fontRenderer.drawStringWithShadow(name, (int)(posX / 0.65) - (mc.fontRenderer.getStringWidth(name)/2), (int)((y + 2) / 0.65), 0xAAAAAA);
                    GlStateManager.popMatrix();
                }
                Gui.drawRect(posX, y + 10, posX + 1, y + 11, 0x88FFFFFF);
            }
        }
        // Rote Nadel in der Mitte
        Gui.drawRect(x + (width / 2), y - 2, x + (width / 2) + 1, y + height + 2, 0xFFFF4444);
    }
} 
