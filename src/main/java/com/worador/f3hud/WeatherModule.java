package com.worador.f3hud;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.WorldInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherModule extends InfoModule {

    @Override
    public String getName() {
        return "Weather";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showWeather;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (!isEnabledInConfig() || mc.player == null || mc.world == null) return lines;

        BlockPos pos = mc.player.getPosition();
        Biome biome = mc.world.getBiome(pos);
        WorldInfo info = mc.world.getWorldInfo();

        // 1. Temperatur (Celsius reicht völlig aus, keine Fahrenheit-Leichen im Code)
        float temp = biome.getTemperature(pos);
        float celsius = temp * 30.0f;
        lines.add(new InfoLine("Temp: ", String.format(Locale.US, "%.1f°C", celsius), 0xFFFF55));

        // 2. Wetter & Gewitter zusammengefasst
        boolean isRaining = mc.world.isRaining();
        boolean isThundering = mc.world.isThundering();
        boolean canSnow = biome.getEnableSnow() || temp < 0.15F;

        if (isRaining) {
            String type = isThundering ? "Storm" : (canSnow ? "Snow" : "Rain");
            int color = isThundering ? 0xFFD700 : (canSnow ? 0xFFFFFF : 0x5555FF);

            // Wir nehmen den längeren Timer (meist ThunderTime, falls aktiv)
            int timeLeft = isThundering ? info.getThunderTime() : info.getRainTime();

            lines.add(new InfoLine(type + ": ", "Ends in " + formatTime(timeLeft), color));
        } else {
            // Vorhersage
            int nextRain = info.getRainTime();
            if (nextRain > 0 && nextRain < 24000) { // Nur anzeigen, wenn es in den nächsten 20 Min passiert
                lines.add(new InfoLine("Next Rain: ", formatTime(nextRain), 0x55FF55));
            } else {
                lines.add(new InfoLine("Weather: ", "Clear", 0x55FF55));
            }
        }

        return lines;
    }

    private String formatTime(int ticks) {
        int totalSeconds = Math.max(0, ticks) / 20;
        int m = totalSeconds / 60;
        int s = totalSeconds % 60;
        return m + "m " + String.format(Locale.US, "%02ds", s);
    }
}