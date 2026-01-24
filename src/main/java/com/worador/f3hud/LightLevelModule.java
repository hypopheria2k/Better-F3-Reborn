package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import java.util.ArrayList;
import java.util.List;

public class LightLevelModule extends InfoModule {
    private long lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override public String getName() { return "Light Level"; }
    @Override protected boolean isEnabledInConfig() { return ModConfig.modules.showLightLevel; }

    @Override
    public List<InfoLine> getLines() {
        if (!isEnabledInConfig() || mc.player == null || mc.world == null) return new ArrayList<>();

        // Tick-Caching: Licht muss nicht jeden Frame berechnet werden (alle 5 Ticks reicht völlig)
        if (mc.player.ticksExisted - lastUpdateTick < 5 && !cachedLines.isEmpty()) {
            return cachedLines;
        }

        List<InfoLine> lines = new ArrayList<>();
        BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        int sky = mc.world.getLightFor(EnumSkyBlock.SKY, pos);
        int block = mc.world.getLightFor(EnumSkyBlock.BLOCK, pos);

        // "Real Light" (Berücksichtigt Tageszeit/Nacht/Regen für Mob-Spawns)
        int realSky = Math.max(0, sky - mc.world.calculateSkylightSubtracted(1.0F));
        int total = Math.max(realSky, block);

        // Kompakte Anzeige: Nur eine Zeile, die alles sagt
        // Farbe wird ROT wenn Monster spawnen können (total <= 7)
        String status = (total <= 7) ? "§c[DANGER]" : "§a[SAFE]";
        String valueInfo = String.format("%d (S:%d B:%d)", total, sky, block);

        lines.add(new InfoLine("Light: ", valueInfo + " " + status, ModConfig.colors.colorLight));

        this.cachedLines = lines;
        this.lastUpdateTick = mc.player.ticksExisted;
        return lines;
    }
}