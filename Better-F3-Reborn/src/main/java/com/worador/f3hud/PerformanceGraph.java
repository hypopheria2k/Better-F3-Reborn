package com.worador.f3hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import java.util.LinkedList;
import java.util.Queue;

public class PerformanceGraph {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Queue<Integer> fpsHistory = new LinkedList<>();
    private static final int MAX_SAMPLES = 100;

    private int cachedAvg = 0;
    private int cachedLow1 = 0;
    private long lastStatUpdate = 0;

    public void update() {
        int currentFPS = Math.max(1, Minecraft.getDebugFPS());
        fpsHistory.add(currentFPS);
        if (fpsHistory.size() > MAX_SAMPLES) fpsHistory.poll();

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastStatUpdate > 500 && !fpsHistory.isEmpty()) {
            calculateStats();
            lastStatUpdate = currentTime;
        }
    }

    private void calculateStats() {
        int sum = 0;
        int min = 999;
        for (int f : fpsHistory) {
            sum += f;
            if (f < min) min = f;
        }
        cachedAvg = sum / fpsHistory.size();
        cachedLow1 = min;
    }

    public void renderAt(int x, int y, int width, int height, float animProgress) {
        if (fpsHistory.isEmpty() || !ModConfig.modules.showPerformanceGraph) return;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();

        // Hintergrund: Transparentes Grau statt Schwarz
        int bgAlpha = (int)(animProgress * ModConfig.animation.backgroundAlpha);
        Gui.drawRect(x, y, x + width, y + height, (bgAlpha << 24) | 0x202020);

        drawGraphLines(x, y, width, height, animProgress);

        GlStateManager.enableTexture2D();
        mc.fontRenderer.drawStringWithShadow("FPS History", x + 3, y + 2, 0xFFFFFF);

        String stats = "Avg: " + cachedAvg + "  Min: " + cachedLow1;
        mc.fontRenderer.drawStringWithShadow(stats, x + 3, y + height - 10, 0xAAFFFF);

        GlStateManager.disableBlend();
    }

    private void drawGraphLines(int x, int y, int width, int height, float animProgress) {
        if (fpsHistory.size() < 2) return;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // DAS HIER IST DER FIX FÜR DIE MAGISCHE VERSCHWUNDENE LINIE:
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.glLineWidth(2.5f);

        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);

        float xStep = (float) width / (MAX_SAMPLES - 1);
        int currentSamples = fpsHistory.size();
        float xOffset = x + width - ((currentSamples - 1) * xStep);

        int i = 0;
        for (int fps : fpsHistory) {
            float val = Math.min(fps, 120.0f);
            float renderX = xOffset + (i * xStep);
            float renderY = y + height - (val / 120.0f * (height - 15)) - 5;

            int color = getDynamicColor(fps); // Hier nutzt er deine Grün-Gelb-Rot Logik
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;
            int a = (int)(animProgress * 255);

            buffer.pos(renderX, renderY, 0).color(r, g, b, a).endVertex();
            i++;
        }
        tessellator.draw();

        // Texturen wieder an, sonst ist der Rest vom Minecraft-HUD kaputt!
        GlStateManager.enableTexture2D();
    }

    private int getDynamicColor(int fps) {
        if (fps >= 60) return 0x55FF55; // Grün
        if (fps <= 25) return 0xFF5555; // Rot

        // Interpolation zwischen 25 (Rot) und 60 (Grün)
        if (fps < 40) {
            float f = (fps - 25) / 15.0f;
            return (255 << 16) | ((int)(85 + 170 * f) << 8) | 85;
        } else {
            float f = (fps - 40) / 20.0f;
            return ((int)(255 - 170 * f) << 16) | (255 << 8) | 85;
        }
    }
}