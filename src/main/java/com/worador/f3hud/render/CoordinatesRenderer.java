package com.worador.f3hud.render;

import com.worador.f3hud.InfoModule;
import com.worador.f3hud.ModConfig;
import com.worador.f3hud.util.RenderUtils;
import net.minecraft.client.Minecraft;
import java.util.List;

public class CoordinatesRenderer implements IModuleRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(InfoModule module, int x, int y, float animProgress) {
        List<InfoModule.InfoLine> lines = module.getLines();
        if (lines == null || lines.isEmpty()) return;

        // 1. Breite dynamisch berechnen
        int maxWidth = 0;
        for (InfoModule.InfoLine line : lines) {
            maxWidth = Math.max(maxWidth, mc.fontRenderer.getStringWidth(line.label + line.value));
        }

        // XYZ Zeile für die Breite berücksichtigen
        String xS = String.format("%.5f", mc.player.posX);
        String yS = String.format("%.5f", mc.player.getEntityBoundingBox().minY);
        String zS = String.format("%.5f", mc.player.posZ);
        int xyzWidth = mc.fontRenderer.getStringWidth("XYZ: " + xS + "  " + yS + "  " + zS);
        maxWidth = Math.max(maxWidth, xyzWidth);

        // 2. Hintergrund zeichnen (Dynamische Höhe: Zeilenanzahl + 1 für die XYZ-Zeile)
        int totalHeight = (lines.size() + 1) * 11;
        RenderUtils.drawComponentBackground(x, y, maxWidth, totalHeight, animProgress);

        // 3. Inhalt zeichnen
        // Block-Koordinaten
        if (lines.size() >= 1) {
            drawRow(lines.get(0), x, y);
            y += 11;
        }

        // Präzise XYZ-Koordinaten
        drawXYZ(x, y);
        y += 11;

        // Restliche Zeilen (Chunk-Coords, etc.)
        for (int i = 2; i < lines.size(); i++) {
            drawRow(lines.get(i), x, y);
            y += 11;
        }
    }

    private void drawRow(InfoModule.InfoLine line, int x, int y) {
        String fullText = line.label + line.value;
        mc.fontRenderer.drawStringWithShadow(fullText, x, y, line.color);
    }

    private void drawXYZ(int x, int y) {
        if (mc.player == null) return;

        String xS = String.format("%.5f", mc.player.posX);
        String yS = String.format("%.5f", mc.player.getEntityBoundingBox().minY);
        String zS = String.format("%.5f", mc.player.posZ);
        String prefix = "XYZ: ";

        int curX = x;

        mc.fontRenderer.drawStringWithShadow(prefix, curX, y, ModConfig.colors.colorDefault);
        curX += mc.fontRenderer.getStringWidth(prefix);

        mc.fontRenderer.drawStringWithShadow(xS + " ", curX, y, ModConfig.colors.colorX);
        curX += mc.fontRenderer.getStringWidth(xS + " ");

        mc.fontRenderer.drawStringWithShadow(yS + " ", curX, y, ModConfig.colors.colorY);
        curX += mc.fontRenderer.getStringWidth(yS + " ");

        mc.fontRenderer.drawStringWithShadow(zS, curX, y, ModConfig.colors.colorZ);
    }
}