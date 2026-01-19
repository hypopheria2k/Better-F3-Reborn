package com.worador.f3hud;

import net.minecraftforge.fml.common.Loader;
import java.util.ArrayList;
import java.util.List;

public class AstralModule extends InfoModule {

    @Override
    public String getName() { return "Astral Sorcery"; }

    @Override
    protected boolean isEnabledInConfig() {
        // Hier greifen wir auf die Config zu -> Variable wird "aktiv" (nicht mehr grau)
        return ModConfig.modules.showAstralSorcery;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();

        // Der Sicherheits-Check verhindert, dass die Compat-Klasse geladen wird, wenn die Mod fehlt
        if (Loader.isModLoaded("astralsorcery") && mc.world != null) {
            // Aufruf über vollqualifizierten Namen
            lines.addAll(com.worador.f3hud.compat.AstralCompat.getAstralLines(mc.world));
        }

        return lines;
    }
} 
