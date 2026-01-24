package com.worador.f3hud;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SpeedometerModule extends InfoModule {

    private double lastPosX = 0;
    private double lastPosY = 0;
    private double lastPosZ = 0;
    private double currentSpeed = 0;
    private double horizontalSpeed = 0;
    private double maxSpeed = 0;

    @Override
    public String getName() {
        return "Speedometer";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showSpeedometer;
    }

    @Override
    public List<InfoLine> getLines() {
        if (!isEnabledInConfig() || mc.player == null) return new ArrayList<>();

        // Berechnung der tatsächlichen Distanz zwischen zwei Render-Ticks
        // Nutzt die Positionsdaten des letzten Ticks für maximale Genauigkeit
        double dX = mc.player.posX - mc.player.prevPosX;
        double dY = mc.player.posY - mc.player.prevPosY;
        double dZ = mc.player.posZ - mc.player.prevPosZ;

        // Umrechnung in Blöcke pro Sekunde (20 Ticks = 1 Sekunde)
        currentSpeed = MathHelper.sqrt(dX * dX + dY * dY + dZ * dZ) * 20.0;
        horizontalSpeed = MathHelper.sqrt(dX * dX + dZ * dZ) * 20.0;

        // Max-Speed Logik
        if (currentSpeed > maxSpeed) maxSpeed = currentSpeed;
        if (currentSpeed < 0.1 && mc.player.ticksExisted % 600 == 0) maxSpeed = 0; // Reset nach 30s Stillstand

        List<InfoLine> lines = new ArrayList<>();

        // 1. Hauptanzeige (BPS)
        int mainColor = ModConfig.colors.colorSpeedometer;
        if (currentSpeed > 10.0) mainColor = 0x55FF55; // Grün bei Sprint/Flug
        if (currentSpeed > 30.0) mainColor = 0xFF5555; // Rot bei extremem Speed (Elytra/Explosion)

        lines.add(new InfoLine("Speed: ", String.format(Locale.US, "%.2f bps", currentSpeed), mainColor));

        // 2. Horizontale Geschwindigkeit (Wichtig für normales Laufen)
        if (horizontalSpeed > 0.1) {
            lines.add(new InfoLine("Horizontal: ", String.format(Locale.US, "%.2f bps", horizontalSpeed), 0xAAAAAA));
        }

        // 3. Elytra Telemetrie (Vervollständigt)
        if (mc.player.isElytraFlying()) {
            float pitch = mc.player.rotationPitch;
            String flightStatus = TextFormatting.GRAY + "Gliding";
            int flightColor = 0x55FFFF;

            // Optimale Glide-Winkel für Minecraft 1.12.2
            if (pitch >= -1.5f && pitch <= 1.5f) {
                flightStatus = TextFormatting.GOLD + "OPTIMAL GLIDE";
                flightColor = 0xFFAA00;
            } else if (pitch > 25.0f) {
                flightStatus = TextFormatting.RED + "DIVING";
                flightColor = 0xFF5555;
            } else if (pitch < -25.0f) {
                flightStatus = TextFormatting.AQUA + "CLIMBING";
            }

            lines.add(new InfoLine("Flight: ", flightStatus, flightColor));
        }

        // 4. Max Speed (als kleine Info unten drunter)
        if (maxSpeed > 0.5) {
            lines.add(new InfoLine("Max: ", String.format(Locale.US, "%.2f bps", maxSpeed), 0x555555));
        }

        return lines;
    }
}