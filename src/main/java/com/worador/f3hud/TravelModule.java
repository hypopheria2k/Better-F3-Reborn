package com.worador.f3hud;

import net.minecraft.block.BlockPortal;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TravelModule extends InfoModule {
    @Override public String getName() { return "Travel & Portals"; }
    @Override protected boolean isEnabledInConfig() { return ModConfig.modules.showTravelModule; }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null) return lines;

        int dim = mc.world.provider.getDimension();

        // Nether Links
        if (dim == 0) {
            lines.add(new InfoLine("Nether Link: ", String.format(Locale.US, "X:%.1f Z:%.1f", mc.player.posX / 8.0, mc.player.posZ / 8.0), 0xAA00AA));
        } else if (dim == -1) {
            lines.add(new InfoLine("Overworld Link: ", String.format(Locale.US, "X:%.1f Z:%.1f", mc.player.posX * 8.0, mc.player.posZ * 8.0), 0x55FFFF));
        }

        // Portal Finder (nur alle 10 Ticks suchen, da teuer)
        if (mc.player.ticksExisted % 10 == 0) {
            BlockPos portal = findPortal(new BlockPos(mc.player), 64);
            if (portal != null) {
                double dist = Math.sqrt(mc.player.getDistanceSq(portal));
                lines.add(new InfoLine("Nearest Portal: ", String.format(Locale.US, "%.1fm", dist), 0x55FFFF));
            }
        }
        return lines;
    }

    private BlockPos findPortal(BlockPos start, int r) {
        for (int x = -r; x <= r; x += 2)
            for (int z = -r; z <= r; z += 2)
                for (int y = -8; y <= 8; y++)
                    if (mc.world.getBlockState(start.add(x, y, z)).getBlock() instanceof BlockPortal) return start.add(x, y, z);
        return null;
    }
}