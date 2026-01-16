package com.worador.f3hud.compat;

import com.worador.f3hud.InfoModule;
import net.minecraftforge.fml.common.Loader;
import java.util.ArrayList;
import java.util.List;

public class StellarCompat extends InfoModule {

    private static final String MOD_ID = "stellar_core";

    @Override
    public String getName() {
        return "StellarCore";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return Loader.isModLoaded(MOD_ID);
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.world == null) return lines;

        // StellarCore überschreibt worldTime, daher greifen wir sie hier direkt ab
        long worldTime = mc.world.getWorldTime();
        long day = worldTime / 24000;

        // Berechnung für HH:mm (Minecraft Zeitrechnung)
        // +6 Stunden Offset, da Tick 0 = 06:00 Uhr ist
        long hours = (worldTime / 1000 + 6) % 24;
        long minutes = (worldTime % 1000) * 60 / 1000;

        String timeString = String.format("%02d:%02d", hours, minutes);

        // Anzeige im HUD
        lines.add(new InfoLine("Stellar Time: ", timeString, 0x55FF55));
        lines.add(new InfoLine("Stellar Day: ", String.valueOf(day), 0xAAAAAA));

        // Mondphase (wird von StellarCore ebenfalls beeinflusst)
        int phase = mc.world.getMoonPhase();
        lines.add(new InfoLine("Moon Phase: ", getMoonPhaseName(phase), 0x55FFFF));

        return lines;
    }

    private String getMoonPhaseName(int phase) {
        String[] phases = {"Full Moon", "Waning Gibbous", "Last Quarter", "Waning Crescent",
                "New Moon", "Waxing Crescent", "First Quarter", "Waxing Gibbous"};
        return (phase >= 0 && phase < 8) ? phases[phase] : "Unknown";
    }
}