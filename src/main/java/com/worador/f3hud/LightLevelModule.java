package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import java.util.ArrayList;
import java.util.List;

public class LightLevelModule extends InfoModule {
    @Override public String getName() { return "Light Level"; }
    @Override protected boolean isEnabledInConfig() { return ModConfig.modules.showLightLevel; }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null) return lines;

        BlockPos pos = new BlockPos(mc.player);
        int sky = mc.world.getLightFor(EnumSkyBlock.SKY, pos);
        int block = mc.world.getLightFor(EnumSkyBlock.BLOCK, pos);
        int total = Math.max(sky, block);

        // Anzeige des Rohwerts
        int lightColor = (total < 8) ? 0xFF5555 : ModConfig.colors.colorLight;
        lines.add(new InfoLine("Light: ", total + " (Sky: " + sky + ", Block: " + block + ")", lightColor));

        // Real Light (Sicherheits-Check)
        int realSky = Math.max(0, sky - mc.world.calculateSkylightSubtracted(1.0F));
        int realTotal = Math.max(realSky, block);
        lines.add(new InfoLine("Safety: ", realTotal + (realTotal <= 7 ? " [DANGER]" : " [SAFE]"), (realTotal <= 7 ? 0xFF5555 : 0x55FF55)));

        return lines;
    }
}