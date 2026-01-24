package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;

public class ChunkPosModule extends InfoModule {

    @Override public String getName() { return "Chunk-Position"; }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showChunkPos;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (!isEnabledInConfig() || mc.player == null) return lines;

        // Relative Position im Chunk (0-15)
        int relX = (int)Math.floor(mc.player.posX) & 15;
        int relY = (int)Math.floor(mc.player.posY) & 15;
        int relZ = (int)Math.floor(mc.player.posZ) & 15;

        lines.add(new InfoLine("Chunk-Rel: ",
                String.format("%d, %d, %d", relX, relY, relZ),
                ModConfig.colors.colorChunk));

        return lines;
    }
}