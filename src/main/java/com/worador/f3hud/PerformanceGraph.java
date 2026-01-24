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

        // 1. Universelle Range-Ermittlung
        int maxInHistory = 0;
        for (int f : fpsHistory) {
            if (f > maxInHistory) maxInHistory = f;
        }

        // Wir setzen ein Basis-Maximum von 60, aber wenn der Spieler mehr hat,
        // passt sich der Graph automatisch nach oben an.
        float displayMax = Math.max(60.0f, maxInHistory + 10);
        float displayMin = 0; // Für die universelle Vergleichbarkeit starten wir bei 0

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.glLineWidth(2.0f);

        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);

        float xStep = (float) width / (MAX_SAMPLES - 1);
        float xOffset = x + width - ((fpsHistory.size() - 1) * xStep);

        int i = 0;
        for (int fps : fpsHistory) {
            float renderX = xOffset + (i * xStep);

            // Universelle Skalierung: FPS im Verhältnis zum dynamischen Maximum
            float ratio = (float) fps / displayMax;
            // -15 für Padding oben (Textplatz), -5 für Padding unten
            float renderY = y + height - (ratio * (height - 20)) - 5;

            int color = getDynamicColor(fps);
            int r = (color >> 16) & 0xFF, g = (color >> 8) & 0xFF, b = color & 0xFF;
            int a = (int)(animProgress * 255);

            buffer.pos(renderX, renderY, 0).color(r, g, b, a).endVertex();
            i++;
        }
        tessellator.draw();
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