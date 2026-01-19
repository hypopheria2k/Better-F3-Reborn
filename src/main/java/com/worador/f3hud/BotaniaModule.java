package com.worador.f3hud;

import net.minecraftforge.fml.common.Loader;
import java.util.ArrayList;
import java.util.List;
// DER IMPORT OBEN MUSS WEG!

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

        // Der Check schützt den Zugriff.
        // Wir rufen die Compat-Klasse über den vollen Pfad auf,
        // damit Java sie nicht beim Laden dieser Klasse laden muss.
        if (mc.player != null && Loader.isModLoaded("botania")) {
            lines.addAll(com.worador.f3hud.compat.BotaniaCompat.getManaLines(mc.player, mc.world));
        }

        return lines;
    }
} 
