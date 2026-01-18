package com.worador.f3hud;

import java.util.ArrayList;
import java.util.List;

public class CompassModule extends InfoModule {

    @Override
    public String getName() { return "Compass"; }

    @Override
    protected boolean isEnabledInConfig() { return ModConfig.modules.showCompass; }

    @Override
    public List<InfoLine> getLines() {
        return new ArrayList<>(); // Keine Textzeilen, da wir grafisch rendern
    }

    @Override
    public int getHeight() {
        return 17; // Höhe des Balkens
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}