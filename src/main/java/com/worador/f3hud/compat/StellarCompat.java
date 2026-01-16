package com.worador.f3hud.compat;

import com.worador.f3hud.InfoModule.InfoLine;
import net.minecraft.world.World;
import java.util.ArrayList;
import java.util.List;

public class StellarCompat {

    public static List<InfoLine> getStellarLines(World world) {
        List<InfoLine> lines = new ArrayList<>();

        // Dein funktionierender Code:
        long worldTime = world.getWorldTime();
        long day = worldTime / 24000;

        long hours = (worldTime / 1000 + 6) % 24;
        long minutes = (worldTime % 1000) * 60 / 1000;

        String timeString = String.format("%02d:%02d", hours, minutes);

        lines.add(new InfoLine("Stellar Time: ", timeString, 0x55FF55));
        lines.add(new InfoLine("Stellar Day: ", String.valueOf(day), 0xAAAAAA));

        int phase = world.getMoonPhase();
        lines.add(new InfoLine("Moon Phase: ", getMoonPhaseName(phase), 0x55FFFF));

        return lines;
    }

    private static String getMoonPhaseName(int phase) {
        String[] phases = {"Full Moon", "Waning Gibbous", "Last Quarter", "Waning Crescent",
                "New Moon", "Waxing Crescent", "First Quarter", "Waxing Gibbous"};
        return (phase >= 0 && phase < 8) ? phases[phase] : "Unknown";
    }
}