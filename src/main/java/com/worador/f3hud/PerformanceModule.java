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
        // Nutzt deine bestehende Config-Option
        return ModConfig.modules.showPerformanceGraph;
    }

    @Override
    public List<InfoLine> getLines() {
        // Da der Graph direkt via GraphRenderer gezeichnet wird,
        // braucht dieses Modul selbst keine Textzeilen.
        return new ArrayList<>();
    }

    @Override
    public int getHeight() {
        // Der Graph im Renderer ist 60px hoch + Padding.
        // Wir reservieren 75px, damit nachfolgende Module einen sauberen Abstand haben.
        return 75;
    }

    @Override
    public int getMaxLineWidth() {
        // Unser Graph ist auf 200px Breite festgesetzt.
        return 200;
    }
}