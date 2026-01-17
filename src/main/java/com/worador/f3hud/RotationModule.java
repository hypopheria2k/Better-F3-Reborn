package com.worador.f3hud;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RotationModule extends InfoModule {

    @Override
    public String getName() {
        return "Rotation";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showRotation;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();

        if (mc.player == null) return lines;

        // Yaw und Pitch berechnen
        float yaw = MathHelper.wrapDegrees(mc.player.rotationYaw);
        float pitch = MathHelper.wrapDegrees(mc.player.rotationPitch);

        // Himmelsrichtung (Facing)
        EnumFacing facing = mc.player.getHorizontalFacing();
        String direction = facing.toString().toUpperCase(Locale.US);

        // Achsen-Info (z.B. "Towards positive Z")
        String axisInfo = "";
        switch (facing) {
            case NORTH: axisInfo = " (Towards negative Z)"; break;
            case SOUTH: axisInfo = " (Towards positive Z)"; break;
            case WEST:  axisInfo = " (Towards negative X)"; break;
            case EAST:  axisInfo = " (Towards positive X)"; break;
        }

        // 1. Zeile: Facing
        lines.add(new InfoLine("Facing: ", direction + axisInfo, ModConfig.colors.colorRotation));

        // 2. Zeile: Yaw / Pitch mit 1 Nachkommastelle
        String rotation = String.format(Locale.US, "Yaw: %.1f / Pitch: %.1f", yaw, pitch);
        lines.add(new InfoLine("Rotation: ", rotation, ModConfig.colors.colorRotation));

        return lines;
    }

    @Override
    public int getHeight() {
        return 11 * 2 + 2; // 2 Zeilen
    }
}