package com.worador.f3hud;

import com.worador.f3hud.compat.BloodMagicCompat;
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
        if (mc.player != null && Loader.isModLoaded("bloodmagic")) {
            lines.addAll(BloodMagicCompat.getBloodMagicLines(mc.player, mc.world));
        }
        return lines;
    }
}