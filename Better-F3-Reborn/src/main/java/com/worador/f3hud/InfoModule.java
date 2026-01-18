package com.worador.f3hud;

import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;

public abstract class InfoModule {
    protected final Minecraft mc = Minecraft.getMinecraft();
    public boolean enabled = true;

    public abstract List<InfoLine> getLines();
    public abstract String getName();

    public boolean isEnabled() {
        return enabled && isEnabledInConfig();
    }

    protected boolean isEnabledInConfig() {
        return true;
    }

    public static class InfoLine {
        public String label;
        public String value;
        public int color;

        public InfoLine(String label, String value, int color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }
    }

    public int getHeight() {
        return getLines().size() * 11 + 5;
    }

    public int getMaxLineWidth() {
        int max = 0;
        for (InfoLine line : getLines()) {
            int w = mc.fontRenderer.getStringWidth(line.label + line.value);
            if (w > max) max = w;
        }
        return max;
    }
}