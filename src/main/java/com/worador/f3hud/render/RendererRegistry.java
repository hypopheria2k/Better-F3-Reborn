package com.worador.f3hud.render;

import com.worador.f3hud.*;

public class RendererRegistry {

    private static final IModuleRenderer TEXT = new TextModuleRenderer();
    private static final IModuleRenderer COMPASS = new CompassRenderer();
    private static final IModuleRenderer GRAPH = new GraphRenderer();
    // COORDS wurde gelöscht

    public static IModuleRenderer getRenderer(InfoModule module) {
        if (module instanceof CompassModule) return COMPASS;
        if (module instanceof PerformanceModule) return GRAPH;

        // Das CoordinatesModule nutzt jetzt automatisch den TEXT Renderer am Ende
        return TEXT;
    }
}