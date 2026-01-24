package com.worador.f3hud;

import net.minecraft.util.math.MathHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RotationModule extends InfoModule {

    @Override public String getName() { return "Rotation"; }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showRotation;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (!isEnabledInConfig() || mc.player == null) return lines;

        float yaw = MathHelper.wrapDegrees(mc.player.rotationYaw);
        float pitch = MathHelper.wrapDegrees(mc.player.rotationPitch);

        // Facing wurde entfernt, da das WorldModule das bereits übernimmt!

        // 1. Rohdaten Yaw / Pitch
        int pitchColor = (Math.abs(pitch) < 0.5f) ? 0xFFAA00 : ModConfig.colors.colorRotation;
        String rotation = String.format(Locale.US, "Yaw: %.1f / Pitch: %.1f", yaw, pitch);
        lines.add(new InfoLine("Rotation: ", rotation, pitchColor));

        return lines;
    }
}