package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.WorldInfo;
import java.util.ArrayList;
import java.util.List;

public class WeatherModule extends InfoModule {

    @Override
    public String getName() {
        return "Weather Forecast";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showWeather;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        
        // Strict Gating: Check config first, before any expensive operations
        if (!isEnabledInConfig() || mc.player == null || mc.world == null) {
            return lines;
        }

        BlockPos pos = mc.player.getPosition();
        Biome biome = mc.world.getBiome(pos);

        // Singleplayer-Hack: Wir holen die WorldInfo direkt vom integrierten Server
        WorldInfo info = mc.world.getWorldInfo();
        if (mc.isSingleplayer() && mc.getIntegratedServer() != null) {
            info = mc.getIntegratedServer().getWorld(mc.player.dimension).getWorldInfo();
        }

        float temp = biome.getTemperature(pos);
        boolean canRain = biome.canRain();
        boolean canSnow = biome.getEnableSnow() || temp < 0.15F;

        // 1. Temperatur-Konvertierung: Interner Wert * 30.0 = Celsius
        float celsius = temp * 30.0f;
        String tempDisplay;
        
        if (ModConfig.modules.showFahrenheit) {
            // Konvertiere zu Fahrenheit: C * 1.8 + 32
            float fahrenheit = celsius * 1.8f + 32.0f;
            tempDisplay = String.format("%.1f°C / %.1f°F", celsius, fahrenheit);
        } else {
            tempDisplay = String.format("%.1f°C", celsius);
        }

        lines.add(new InfoLine("Temperature: ", tempDisplay, 0xFFFF55));

        // 2. Wetterstatus & Vorhersage
        if (mc.world.isRaining()) {
            String currentType = canSnow ? "Snowing" : "Raining";
            int color = canSnow ? 0xFFFFFF : 0x5555FF;
            lines.add(new InfoLine("Current: ", currentType, color));

            // Wie lange regnet es noch?
            int stopTime = info.getRainTime();
            if (stopTime > 0) {
                lines.add(new InfoLine("Ends in: ", formatTime(stopTime), 0xAAAAAA));
            }
        } else {
            // Wann fängt es an zu regen?
            int nextRain = info.getRainTime();
            String weatherType = canSnow ? "Snow" : (canRain ? "Rain" : "None");

            if (!canRain && !canSnow) {
                lines.add(new InfoLine("Weather: ", "Arid (Dry)", 0xFFAA00));
            } else if (nextRain > 0) {
                lines.add(new InfoLine("Next " + weatherType + ": ", formatTime(nextRain), 0x55FF55));
            } else {
                lines.add(new InfoLine("Weather: ", "Clear", 0x55FF55));
            }
        }

        // 3. Storm Forecast (Full weather forecast restoration)
        if (mc.world.isThundering()) {
            lines.add(new InfoLine("Storm: ", "Active", 0xFFD700));
            int stormTime = info.getThunderTime();
            if (stormTime > 0) {
                lines.add(new InfoLine("Storm ends in: ", formatTime(stormTime), 0xAAAAAA));
            }
        } else if (info.isThundering()) {
            lines.add(new InfoLine("Storm: ", "Incoming", 0xFF4500));
            int nextStorm = info.getThunderTime();
            if (nextStorm > 0) {
                lines.add(new InfoLine("Storm in: ", formatTime(nextStorm), 0x55FF55));
            }
        } else {
            lines.add(new InfoLine("Storm: ", "None", 0x55FF55));
        }

        return lines;
    }

    private String formatTime(int ticks) {
        int totalSeconds = Math.max(0, ticks) / 20;
        int m = totalSeconds / 60;
        int s = totalSeconds % 60;
        return String.format("%dm %02ds", m, s);
    }
}
