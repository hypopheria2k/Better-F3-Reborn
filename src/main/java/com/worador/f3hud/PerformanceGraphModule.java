package com.worador.f3hud;

import java.util.List;
import java.util.ArrayList;

public class PerformanceGraphModule extends InfoModule {
    @Override
    public String getName() {
        return "Performance Graph";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showPerformanceGraph;
    }

    @Override
    public List<InfoLine> getLines() {
        return new ArrayList<>(); // Der Graph hat keinen Text, also leere Liste
    }

    @Override
    public int getHeight() {
        return 60; // Die feste Höhe, die dein Graph einnimmt
    }
}