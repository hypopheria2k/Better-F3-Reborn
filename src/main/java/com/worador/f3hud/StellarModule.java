package com.worador.f3hud;

import net.minecraftforge.fml.common.Loader;
import java.util.ArrayList;
import java.util.List;

public class StellarModule extends InfoModule {

    @Override
    public String getName() {
        return "StellarCore";
    }

    @Override
    protected boolean isEnabledInConfig() {
        // Hier könntest du in der ModConfig noch 'showStellar' hinzufügen,
        // falls gewünscht. Aktuell prüfen wir nur, ob die Mod da ist.
        return Loader.isModLoaded("stellar_core");
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();

        if (Loader.isModLoaded("stellar_core") && mc.world != null) {
            // Aufruf der Logik im compat-Paket
            lines.addAll(com.worador.f3hud.compat.StellarCompat.getStellarLines(mc.world));
        }

        return lines;
    }
}