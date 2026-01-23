package com.worador.f3hud;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.List;

public class SpeedometerModule extends InfoModule {

    // Performance Throttling: Update every 2 ticks (tenth of a second) for high responsiveness
    private static final int UPDATE_INTERVAL_TICKS = 2;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    private double maxSpeed = 0.0;
    private long lastMaxSpeedReset = 0;

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
        // Strict Gating: Early exit if disabled
        if (!isEnabledInConfig() || mc.player == null) {
            return new ArrayList<>();
        }

        int currentTick = mc.player.ticksExisted;
        
        // Only update if enough ticks have passed or if it's the first call
        if (cachedLines.isEmpty() || (currentTick - lastUpdateTick) >= UPDATE_INTERVAL_TICKS) {
            cachedLines = new ArrayList<>();
            
            // Expensive operations inside the refresh block
            // 1. Gesamte Geschwindigkeit (3D-Vektor)
            double motionX = mc.player.motionX;
            double motionY = mc.player.motionY;
            double motionZ = mc.player.motionZ;

            double speedMs = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ) * 20.0;

            // 2. Horizontale Geschwindigkeit (Wichtig für Sprinten/Reiten)
            double horizontalSpeed = MathHelper.sqrt(motionX * motionX + motionZ * motionZ) * 20.0;

            // Top Speed Logik (Reset alle 30 Sek wenn man steht, oder manuell)
            if (speedMs > maxSpeed) maxSpeed = speedMs;
            if (speedMs < 0.1 && System.currentTimeMillis() - lastMaxSpeedReset > 30000) {
                maxSpeed = 0;
                lastMaxSpeedReset = System.currentTimeMillis();
            }

            // Fix: Set exact 0.00 bps and ETA to N/A when not moving
            if (motionX == 0.0 && motionZ == 0.0) {
                speedMs = 0.0;
                cachedLines.add(new InfoLine("Speed: ", "0.00 bps", 0xFFFFFF));
                cachedLines.add(new InfoLine("ETA: ", "N/A", 0xAAAAAA));
            } else {
                // Anzeige Hauptwert
                int mainColor = (speedMs > 10.0) ? 0x55FF55 : 0xFFFFFF; // Grün wenn schnell
                cachedLines.add(new InfoLine("Speed: ", String.format("%.2f bps", speedMs), mainColor));
            }

            // ZUSATZ: Horizontale Trennung (Effizienz beim Laufen)
            cachedLines.add(new InfoLine("Horizontal: ", String.format("%.2f bps", horizontalSpeed), 0xAAAAAA));

            // 3. Elytra Spezial-Telemetrie
            if (mc.player.isElytraFlying()) {
                float pitch = mc.player.rotationPitch;

                // Sweet Spot Indikator
                String flightStatus = "Flying";
                int flightColor = 0x55FFFF;

                if (pitch >= -1.0f && pitch <= 0.0f) {
                    flightStatus = "OPTIMAL GLIDE";
                    flightColor = 0xFFAA00;
                } else if (pitch > 30.0f) {
                    flightStatus = "DIVING";
                    flightColor = 0xFF5555;
                }

                cachedLines.add(new InfoLine("Glide: ", flightStatus, flightColor));
                cachedLines.add(new InfoLine("Vertical: ", String.format("%.2f bps", motionY * 20.0), 0xAAAAAA));
            }

            // 4. Max Speed (Rekord-Anzeige)
            if (maxSpeed > 0.1) {
                cachedLines.add(new InfoLine("Top Speed: ", String.format("%.2f bps", maxSpeed), 0xFFFF55));
            }
            
            lastUpdateTick = currentTick;
        }
        
        return cachedLines;
    }
}
