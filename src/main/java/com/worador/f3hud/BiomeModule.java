package com.worador.f3hud;

import java.util.ArrayList;
import java.util.List;

public class BiomeModule extends InfoModule {
    @Override public String getName() { return "Biome"; }
    @Override protected boolean isEnabledInConfig() { return ModConfig.modules.showBiome; }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        // Wenn die Config an ist, geben wir eine Dummy-Zeile für den Editor zurück
        // Im WorldModule wird das Biome dann tatsächlich gerendert.
        if (isEnabledInConfig()) {
            // "ForceOpen" ist der Flag, wenn du im Editor-Menü bist
            if (ModConfig.forceOpen) {
                lines.add(new InfoLine("Biome: ", "Editor-Preview", ModConfig.colors.colorBiome));
            }
        }
        return lines;
    }
}