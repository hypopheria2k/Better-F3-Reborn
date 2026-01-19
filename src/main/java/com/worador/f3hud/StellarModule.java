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
        // Prüft, ob die Mod installiert ist UND ob sie in der Config aktiviert wurde
        return Loader.isModLoaded("stellar_core") && ModConfig.modules.showStellar;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();

        // Da isEnabledInConfig bereits den Mod-Check macht,
        // müssen wir hier nur noch sicherstellen, dass die Welt geladen ist.
        if (mc.world != null) {
            // Aufruf der Logik im compat-Paket
            lines.addAll(com.worador.f3hud.compat.StellarCompat.getStellarLines(mc.world));
        }

        return lines;
    }
} 
