package com.worador.f3hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
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
    private static final int MAX_SAMPLES = 120; // 120 Frames = ca. 2 Sekunden bei 60 FPS
    
    private long lastUpdateTime = 0;
    private static final long UPDATE_INTERVAL = 50; // Update alle 50ms
    
    public void update() {
        long currentTime = System.currentTimeMillis();
        
        // Nur alle 50ms updaten für smooth graph
        if (currentTime - lastUpdateTime < UPDATE_INTERVAL) {
            return;
        }
        lastUpdateTime = currentTime;
        
        int currentFPS = Minecraft.getDebugFPS();
        fpsHistory.add(currentFPS);
        
        // Alte Samples entfernen
        if (fpsHistory.size() > MAX_SAMPLES) {
            fpsHistory.poll();
        }
    }
    
    public void render(float animProgress) {
        if (fpsHistory.isEmpty() || !ModConfig.modules.showPerformanceGraph) {
            return;
        }
        
        ScaledResolution sr = new ScaledResolution(mc);
        
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, 
                                             GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, 
                                             GlStateManager.SourceFactor.ONE, 
                                             GlStateManager.DestFactor.ZERO);
        
        // Position: Unten links
        int graphWidth = 200;
        int graphHeight = 60;
        int padding = 4;
        int x = padding;
        int y = sr.getScaledHeight() - graphHeight - padding - 20; // 20px Abstand vom Rand
        
        // Background
        int bgAlpha = (int)(animProgress * 180) << 24;
        Gui.drawRect(x, y, x + graphWidth, y + graphHeight, bgAlpha | 0x000000);
        
        // Border
        drawHollowRect(x, y, x + graphWidth, y + graphHeight, 0xFF555555);
        
        // Raster-Linien (30, 60, 120 FPS)
        drawGridLine(x, y, graphWidth, graphHeight, 30, 0x55555555);
        drawGridLine(x, y, graphWidth, graphHeight, 60, 0x55555555);
        drawGridLine(x, y, graphWidth, graphHeight, 120, 0x55555555);
        
        // FPS Graph zeichnen (IMPROVED!)
        drawGraphLine(x, y, graphWidth, graphHeight);
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        
        // Labels
        mc.fontRenderer.drawStringWithShadow("FPS", x + 2, y + 2, 0xFFFFFF);
        
        // Current FPS (groß)
        int currentFPS = Minecraft.getDebugFPS();
        String fpsText = String.valueOf(currentFPS);
        int fpsColor = getFPSColor(currentFPS);
        mc.fontRenderer.drawStringWithShadow(fpsText, x + graphWidth - mc.fontRenderer.getStringWidth(fpsText) - 2, y + 2, fpsColor);
        
        // Min/Max/Avg
        int min = fpsHistory.stream().min(Integer::compare).orElse(0);
        int max = fpsHistory.stream().max(Integer::compare).orElse(0);
        int avg = (int) fpsHistory.stream().mapToInt(Integer::intValue).average().orElse(0);
        
        String statsText = String.format("Min: %d | Avg: %d | Max: %d", min, avg, max);
        mc.fontRenderer.drawStringWithShadow(statsText, x + 2, y + graphHeight - 10, 0xAAAAAA);
        
        GlStateManager.popMatrix();
    }
    
    private void drawGraphLine(int x, int y, int width, int height) {
    if (fpsHistory.size() < 2) return;
    
    Integer[] samples = fpsHistory.toArray(new Integer[0]);
    float xStep = (float) width / MAX_SAMPLES;
    int maxFPS = Math.max(fpsHistory.stream().max(Integer::compare).orElse(120), 60);
    
    // EINFACH: Dicke Punkte statt Linien!
    for (int i = 0; i < samples.length; i++) {
        int fps = samples[i];
        int posX = (int)(x + width - (samples.length - i) * xStep);
        int posY = (int)(y + height - (fps / (float) maxFPS * (height - 4)));
        int color = getFPSColor(fps) | 0xFF000000;
        
        // 3x3 Pixel Punkt (SUPER SICHTBAR!)
        Gui.drawRect(posX - 1, posY - 1, posX + 2, posY + 2, color);
    }
}
    
    private int getFPSColor(int fps) {
        if (fps >= 60) return 0x55FF55;      // Grün
        else if (fps >= 30) return 0xFFFF55; // Gelb
        else return 0xFF5555;                // Rot
    }
    
    private void drawGridLine(int x, int y, int width, int height, int fpsValue, int color) {
        // Annahme: maxFPS = 120 für Grid
        int maxFPS = 120;
        int lineY = y + height - (int)((fpsValue / (float) maxFPS) * (height - 4));
        
        Gui.drawRect(x, lineY, x + width, lineY + 1, color);
        
        // Label
        String label = String.valueOf(fpsValue);
        mc.fontRenderer.drawString(label, x + 2, lineY - 8, 0x888888);
    }
    
    private void drawHollowRect(int left, int top, int right, int bottom, int color) {
        // Top
        Gui.drawRect(left, top, right, top + 1, color);
        // Bottom
        Gui.drawRect(left, bottom - 1, right, bottom, color);
        // Left
        Gui.drawRect(left, top, left + 1, bottom, color);
        // Right
        Gui.drawRect(right - 1, top, right, bottom, color);
    }
}