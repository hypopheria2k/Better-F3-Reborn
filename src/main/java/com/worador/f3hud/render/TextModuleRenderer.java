package com.worador.f3hud.render;

import com.worador.f3hud.InfoModule;
import com.worador.f3hud.util.RenderUtils;
import net.minecraft.client.Minecraft;

import java.util.List;

public class TextModuleRenderer implements IModuleRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(InfoModule module, int x, int y, float animProgress) {
        List<InfoModule.InfoLine> lines = module.getLines();
        if (lines == null || lines.isEmpty()) return;

        // 1. Breite für den Hintergrund berechnen
        int maxWidth = 0;
        for (InfoModule.InfoLine line : lines) {
            String full = (line.label != null ? line.label : "") + (line.value != null ? line.value : "");
            maxWidth = Math.max(maxWidth, mc.fontRenderer.getStringWidth(full));
        }

        // 2. Hintergrund über zentrale Utility zeichnen (inkl. F3+J Check & Crash-Schutz)
        RenderUtils.drawComponentBackground(x, y, maxWidth, lines.size() * 11, animProgress);

        // 3. Text zeichnen
        for (InfoModule.InfoLine line : lines) {
            String label = line.label != null ? line.label : "";
            String value = line.value != null ? line.value : "";

            mc.fontRenderer.drawStringWithShadow(label + value, x, y, line.color);
            y += 11;
        }
    }
}