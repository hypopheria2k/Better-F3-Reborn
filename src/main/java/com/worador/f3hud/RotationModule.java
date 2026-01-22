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

        float yaw = MathHelper.wrapDegrees(mc.player.rotationYaw);
        float pitch = MathHelper.wrapDegrees(mc.player.rotationPitch);

        // 1. Präzise 8-Wege Himmelsrichtung
        String preciseDir = getPreciseDirection(yaw);
        int dirColor = 0x55FFFF; // Cyan für gute Lesbarkeit
        lines.add(new InfoLine("Direction: ", preciseDir, dirColor));

        // 2. Vanilla Facing & Achsen-Vektor
        EnumFacing facing = mc.player.getHorizontalFacing();
        String axisSign = getAxisSign(facing);
        lines.add(new InfoLine("Facing: ", facing.name().toUpperCase() + " (" + axisSign + ")", ModConfig.colors.colorRotation));

        // 3. Rohdaten Yaw / Pitch
        // Pitch Färbung: Gold wenn man perfekt horizontal schaut (0.0)
        int pitchColor = (Math.abs(pitch) < 0.5f) ? 0xFFAA00 : ModConfig.colors.colorRotation;
        String rotation = String.format(Locale.US, "Yaw: %.1f / Pitch: %.1f", yaw, pitch);
        lines.add(new InfoLine("Rotation: ", rotation, pitchColor));

        return lines;
    }

    private String getPreciseDirection(float yaw) {
        // Mappt Yaw auf 8 Richtungen (N, NE, E, SE, S, SW, W, NW)
        float y = (yaw + 180 + 22.5f) % 360;
        if (y < 45) return "North";
        if (y < 90) return "North-East";
        if (y < 135) return "East";
        if (y < 180) return "South-East";
        if (y < 225) return "South";
        if (y < 270) return "South-West";
        if (y < 315) return "West";
        return "North-West";
    }

    private String getAxisSign(EnumFacing facing) {
        switch (facing) {
            case NORTH: return "-Z";
            case SOUTH: return "+Z";
            case WEST:  return "-X";
            case EAST:  return "+X";
            default:    return "";
        }
    }

    @Override
    public int getHeight() {
        return 11 * 3 + 2; // 3 Zeilen
    }
}