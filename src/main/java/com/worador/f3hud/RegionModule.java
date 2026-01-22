package com.worador.f3hud;

import net.minecraft.util.math.MathHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        // 1. Region-File Berechnung
        int regX = MathHelper.floor(mc.player.posX) >> 9; // 512 Blöcke = 1 Region
        int regZ = MathHelper.floor(mc.player.posZ) >> 9;

        // 2. Lokale Chunk-Position (0-31)
        int chunkX = (MathHelper.floor(mc.player.posX) >> 4) & 31;
        int chunkZ = (MathHelper.floor(mc.player.posZ) >> 4) & 31;

        // 3. Lokale Block-Position innerhalb der Region (0-511)
        int localBlockX = MathHelper.floor(mc.player.posX) & 511;
        int localBlockZ = MathHelper.floor(mc.player.posZ) & 511;

        // Anzeige: Dateiname (Deutsches Verhalten: Klar und präzise)
        String regionFile = String.format(Locale.US, "r.%d.%d.mca", regX, regZ);
        lines.add(new InfoLine("File: ", regionFile, 0x55FF55));

        // Anzeige: Chunk-Slot (Wichtig für NBT-Editoren)
        String chunkSlot = String.format(Locale.US, "Slot [%d, %d]", chunkX, chunkZ);
        lines.add(new InfoLine("Local: ", chunkSlot, 0xAAAAAA));

        // ZUSATZ: Rand-Warnung (World-Pruning Hilfe)
        // Wenn man weniger als 32 Blöcke vom Rand der Region weg ist
        if (localBlockX < 32 || localBlockX > 480 || localBlockZ < 32 || localBlockZ > 480) {
            lines.add(new InfoLine("Edge: ", "Near Region Border", 0xFFAA00));
        } else {
            // Relative Position in Blöcken anzeigen (für technische Analysen)
            lines.add(new InfoLine("Rel: ", localBlockX + " / " + localBlockZ, 0x777777));
        }

        return lines;
    }
}