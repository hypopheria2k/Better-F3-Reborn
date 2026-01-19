package com.worador.f3hud.render;

import com.worador.f3hud.InfoModule;
import com.worador.f3hud.PerformanceGraph;
import net.minecraft.client.renderer.GlStateManager;

public class GraphRenderer implements IModuleRenderer {
    private final PerformanceGraph graph = new PerformanceGraph();

    @Override
    public void render(InfoModule module, int x, int y, float animProgress) {
        // Wir nutzen die x/y Koordinaten vom DebugRenderer,
        // damit der Graph genau dort erscheint, wo die Spalte ist.
        graph.update();
        graph.renderAt(x, y, 100, 50, animProgress);
    }
} 
