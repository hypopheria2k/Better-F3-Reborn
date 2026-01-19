package com.worador.f3hud;

import java.util.ArrayList;
import java.util.List;

public class PerformanceModule extends InfoModule {

    @Override
    public String getName() {
        return "Performance Graph";
    }

    @Override
    protected boolean isEnabledInConfig() {
        // Nutzt die zentrale Config-Option für den Graphen
        return ModConfig.modules.showPerformanceGraph;
    }

    @Override
    public List<InfoLine> getLines() {
        // Der Graph wird separat gerendert, daher keine Textzeilen
        return new ArrayList<>();
    }

    @Override
    public int getHeight() {
        // 60px für den Graphen + 15px Padding für das nächste Modul
        return 75;
    }

    @Override
    public int getMaxLineWidth() {
        // Entspricht der festen Breite des Graphen
        return 200;
    }
} 
