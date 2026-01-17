package com.worador.f3hud.render;

import com.worador.f3hud.InfoModule;
import com.worador.f3hud.ModConfig;
import net.minecraft.client.Minecraft;
import java.util.List;

public class CoordinatesRenderer implements IModuleRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(InfoModule module, int x, int y, float animProgress) {
        List<InfoModule.InfoLine> lines = module.getLines();
        if (lines == null || lines.isEmpty()) return;

        // 1. Block-Koordinaten
        if (lines.size() >= 1) {
            drawRow(lines.get(0), x, y);
            y += 11;
        }

        // 2. Präzise XYZ-Koordinaten (Die farbigen Werte)
        drawXYZ(x, y);
        y += 11;

        // 3. Chunk-Koordinaten
        if (lines.size() >= 3) {
            drawRow(lines.get(2), x, y);
            y += 11;
        }

        // 4. Chunk-Relative Position
        if (lines.size() >= 4) {
            drawRow(lines.get(3), x, y);
        }
    }

    private void drawRow(InfoModule.InfoLine line, int x, int y) {
        // KEIN Gui.drawRect mehr!
        String fullText = line.label + line.value;
        mc.fontRenderer.drawStringWithShadow(fullText, x, y, line.color);
    }

    private void drawXYZ(int x, int y) {
        if (mc.player == null) return;

        // Formatierung wie gewünscht
        String xS = String.format("%.5f", mc.player.posX);
        String yS = String.format("%.5f", mc.player.getEntityBoundingBox().minY);
        String zS = String.format("%.5f", mc.player.posZ);
        String prefix = "XYZ: ";

        // KEIN Hintergrund - wir zeichnen direkt die farbigen Segmente
        int curX = x;

        // Prefix "XYZ: " (Weiß/Default)
        mc.fontRenderer.drawStringWithShadow(prefix, curX, y, ModConfig.colors.colorDefault);
        curX += mc.fontRenderer.getStringWidth(prefix);

        // X-Wert (Rot)
        mc.fontRenderer.drawStringWithShadow(xS + " ", curX, y, ModConfig.colors.colorX);
        curX += mc.fontRenderer.getStringWidth(xS + " ");

        // Y-Wert (Grün)
        mc.fontRenderer.drawStringWithShadow(yS + " ", curX, y, ModConfig.colors.colorY);
        curX += mc.fontRenderer.getStringWidth(yS + " ");

        // Z-Wert (Blau)
        mc.fontRenderer.drawStringWithShadow(zS, curX, y, ModConfig.colors.colorZ);
    }
}