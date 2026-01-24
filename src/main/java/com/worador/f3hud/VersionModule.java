package com.worador.f3hud;

import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.List;

public class VersionModule extends InfoModule {

    private final List<InfoLine> cachedLines = new ArrayList<>();

    public VersionModule() {
        // Wir berechnen die Zeilen nur EINMAL beim Erstellen des Moduls
        String versionLabel = TextFormatting.GRAY + "[" + TextFormatting.WHITE + "v" + TextFormatting.GRAY + "] ";
        String versionValue = TextFormatting.GREEN + "2.2.0 " + TextFormatting.GRAY + "| " + TextFormatting.AQUA + "Beta3";
        cachedLines.add(new InfoLine(versionLabel, versionValue, 0xFFFFFF)); // Weiß, da String-Formatierung intern regelt

        String warningLabel = TextFormatting.RED + "[!] " + TextFormatting.WHITE + "Notice: ";
        String warningText = TextFormatting.RED + "BETA " + TextFormatting.GRAY + "- " + "May contain bugs";
        cachedLines.add(new InfoLine(warningLabel, warningText, 0xFFFFFF));
    }

    @Override public String getName() { return "Version Info"; }
    @Override protected boolean isEnabledInConfig() { return ModConfig.modules.showVersion; }

    @Override
    public List<InfoLine> getLines() {
        // Minimalster CPU-Overhead: Nur die fertige Liste zurückgeben
        return isEnabledInConfig() ? cachedLines : new ArrayList<>();
    }

    @Override
    public int getHeight() {
        // 2 Zeilen à 9 Pixel + 2 Pixel Abstand
        return (9 * 2) + 2;
    }
}