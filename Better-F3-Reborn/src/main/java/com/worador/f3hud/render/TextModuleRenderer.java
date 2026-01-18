package com.worador.f3hud.render;

import com.worador.f3hud.InfoModule;
import net.minecraft.client.Minecraft;

public class TextModuleRenderer implements IModuleRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(InfoModule module, int x, int y, float animProgress) {
        if (module.getLines() == null || module.getLines().isEmpty()) return;

        // Wir brauchen kein 'isRight' mehr für das Zeichnen,
        // da der DebugRenderer das 'x' bereits perfekt übergibt.

        for (InfoModule.InfoLine line : module.getLines()) {
            String label = line.label != null ? line.label : "";
            String value = line.value != null ? line.value : "";
            String full = label + value;

            // KEIN Gui.drawRect mehr.
            // drawStringWithShadow sorgt für perfekte Lesbarkeit auf jedem Untergrund.
            mc.fontRenderer.drawStringWithShadow(full, x, y, line.color);

            // 11 Pixel Zeilenabstand (9px Font + 2px Abstand)
            y += 11;
        }
    }
}