package com.worador.f3hud;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TravelModule extends InfoModule {

    @Override
    public String getName() {
        return "Travel";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showTravelModule;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null) return lines;

        int dim = mc.world.provider.getDimension();

        // Nether-Berechnungen (Ziele in der jeweils anderen Dimension)
        if (dim == 0) {
            // In der Oberwelt: Zeige wo man im Nether landet
            String coords = String.format(Locale.US, "X:%.0f Z:%.0f", mc.player.posX / 8.0, mc.player.posZ / 8.0);
            lines.add(new InfoLine("Nether Link: ", coords, 0xAA00AA));
        } else if (dim == -1) {
            // Im Nether: Zeige wo man in der Oberwelt landet
            String coords = String.format(Locale.US, "X:%.0f Z:%.0f", mc.player.posX * 8.0, mc.player.posZ * 8.0);
            lines.add(new InfoLine("OW Link: ", coords, 0x55FFFF));
        }

        return lines;
    }
}