package com.worador.f3hud;
import java.util.ArrayList;
import java.util.List;

public class DimensionModule extends InfoModule {
    @Override public String getName() { return "Dimension"; }
    @Override protected boolean isEnabledInConfig() { return ModConfig.modules.showDimension; }
    @Override public List<InfoLine> getLines() { return new ArrayList<>(); } // Leer, da WorldModule das macht
}