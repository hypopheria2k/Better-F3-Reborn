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
        if (mc.player == null || mc.world == null) return lines;

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

        // 1. Lokale Temperatur (Immer aktuell)
        lines.add(new InfoLine("Biome Temp: ", String.format("%.2f", temp), 0xFFFF55));

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

        // DEBUG: Jetzt sollten hier im Singleplayer echte Zahlen stehen
        lines.add(new InfoLine("DEBUG Ticks: ", String.valueOf(info.getRainTime()), 0xAAAAAA));

        return lines;
    }

    private String formatTime(int ticks) {
        int totalSeconds = Math.max(0, ticks) / 20;
        int m = totalSeconds / 60;
        int s = totalSeconds % 60;
        return String.format("%dm %02ds", m, s);
    }
}