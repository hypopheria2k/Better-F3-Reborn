package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.MathHelper;

public class RegionModule extends InfoModule {

    @Override
    public String getName() {
        return "Region";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showRegion;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null) return lines;

        // Mathematisch korrekte Region-Berechnung für r.x.z.mca
        int regX = MathHelper.floor(mc.player.posX / 512.0D);
        int regZ = MathHelper.floor(mc.player.posZ / 512.0D);

        // Lokale Chunk-ID (0-31)
        int chunkX = MathHelper.floor(mc.player.posX / 16.0D) & 31;
        int chunkZ = MathHelper.floor(mc.player.posZ / 16.0D) & 31;
        int sectionY = MathHelper.floor(mc.player.posY / 16.0D);

        // Nutze %d für Integers, um das ".0" zu vermeiden
        String regionFile = String.format(java.util.Locale.US, "r.%d.%d.mca", regX, regZ);
        String localPos = String.format(java.util.Locale.US, "Chunk [%d, %d] in Section %d", chunkX, chunkZ, sectionY);

        lines.add(new InfoLine("Region: ", regionFile, ModConfig.colors.colorDefault));
        lines.add(new InfoLine("Local: ", localPos, 0xAAAAAA));

        return lines;
    }
} 
