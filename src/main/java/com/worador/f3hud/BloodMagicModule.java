package com.worador.f3hud;

import net.minecraftforge.fml.common.Loader;
import java.util.ArrayList;
import java.util.List;

public class BloodMagicModule extends InfoModule {
    @Override
    public String getName() { return "BloodMagic"; }

    @Override
    protected boolean isEnabledInConfig() { return ModConfig.modules.showBloodMagic; }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        // WICHTIG: Den Import von BloodMagicCompat oben entfernen
        // und den Aufruf über den vollen Pfad oder eine Proxy-Methode machen.
        if (mc.player != null && Loader.isModLoaded("bloodmagic")) {
            lines.addAll(com.worador.f3hud.compat.BloodMagicCompat.getBloodMagicLines(mc.player, mc.world));
        }
        return lines;
    }
}