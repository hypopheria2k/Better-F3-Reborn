package com.worador.f3hud;

import com.worador.f3hud.compat.BotaniaCompat;
import net.minecraftforge.fml.common. Loader;
import java.util.ArrayList;
import java.util.List;

public class BotaniaModule extends InfoModule {

    @Override
    public String getName() {
        return "Botania";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showBotania;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();

        // Nur wenn Botania geladen ist
        if (mc.player != null && Loader.isModLoaded("botania")) {
            lines.addAll(BotaniaCompat.getManaLines(mc.player, mc.world));
        }

        return lines;
    }
}