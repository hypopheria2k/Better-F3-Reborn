package com.worador.f3hud;

import java.util.ArrayList;
import java.util.List;

public class PerformanceModule extends InfoModule {
public static final PerformanceModule INSTANCE = new PerformanceModule();

    // Eine statische Instanz, damit die Daten (fpsHistory) erhalten bleiben
    private static final PerformanceGraph GRAPH = new PerformanceGraph();

    @Override
    public String getName() {
        return "Performance Graph";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showPerformanceGraph;
    }

    // WICHTIG: Die update-Logik muss getickt werden!
    public void onTick() {
        if (isEnabledInConfig()) {
            GRAPH.update();
        }
    }

    @Override
    public List<InfoLine> getLines() {
        return new ArrayList<>(); // Bleibt leer, da wir selbst zeichnen
    }

    @Override
    public int getHeight() {
        return 75; // Reserviert den Platz im HUD
    }

    @Override
    public int getMaxLineWidth() {
        return 200; // Breite des Graphen
    }

    // Wir brauchen eine Methode, die vom Renderer aufgerufen wird
    public void renderCustom(int x, int y, float animProgress) {
        // Hier rufen wir die render-Logik deiner anderen Klasse auf
        GRAPH.renderAt(x, y, getMaxLineWidth(), getHeight() - 15, animProgress);
    }
}