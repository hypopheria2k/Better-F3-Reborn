package com.worador.f3hud;

import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.List;

public class VersionModule extends InfoModule {

    @Override
    public String getName() {
        return "Version Info";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showVersion;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null) return lines;

        // 1. Build-Zeile: [Version] 2.2.0 | Beta1
        // Label in Grau, Nummer in Grün (0x55FF55), Status in Cyan (0x55FFFF)
        String versionLabel = TextFormatting.GRAY + "[" + TextFormatting.WHITE + "v" + TextFormatting.GRAY + "] ";
        String versionValue = TextFormatting.GREEN + "2.2.0 " + TextFormatting.GRAY + "| " + TextFormatting.AQUA + "Beta1";
        lines.add(new InfoLine(versionLabel, versionValue, 0x55FF55));

        // 2. Warn-Zeile: [!] Notice: May contain bugs
        // Icon in Rot (0xFF5555), Text in hellem Rot/Rosa
        String warningLabel = TextFormatting.RED + "[!] " + TextFormatting.WHITE + "Notice: ";
        String warningText = TextFormatting.RED + "BETA " + TextFormatting.GRAY + "- " + "May contain bugs";
        lines.add(new InfoLine(warningLabel, warningText, 0xFF5555));

        return lines;
    }

    @Override
    public int getHeight() {
        return 11 * 2 + 2;
    }
}