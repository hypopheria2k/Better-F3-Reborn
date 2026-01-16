package com.worador.f3hud.compat;

import com.worador.f3hud.InfoModule;
import net.minecraftforge.fml.common.Loader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AstralCompat extends InfoModule {

    @Override
    public String getName() { return "Astral Sorcery"; }

    @Override
    protected boolean isEnabledInConfig() { return Loader.isModLoaded("astralsorcery"); }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (!isEnabledInConfig() || mc.world == null) return lines;

        try {
            Class<?> cSkyHandlerClass = Class.forName("hellfirepvp.astralsorcery.common.constellation.distribution.ConstellationSkyHandler");
            Object cSkyHandlerInstance = cSkyHandlerClass.getMethod("getInstance").invoke(null);

            // 1. STARLIGHT & MONDPHASE
            Method getDist = cSkyHandlerClass.getMethod("getCurrentDaytimeDistribution", net.minecraft.world.World.class);
            float starlight = (Float) getDist.invoke(cSkyHandlerInstance, mc.world);

            String moonPhase = getMoonPhaseName(mc.world.getMoonPhase());
            lines.add(new InfoLine("Astral: ", String.format("%.0f%% (%s)", starlight * 100, moonPhase), 0x00A9FF));

            // 2. WELT-HANDLER FÜR EVENTS UND KONSTELLATIONEN
            Object worldHandler = cSkyHandlerClass.getMethod("getWorldHandler", net.minecraft.world.World.class).invoke(cSkyHandlerInstance, mc.world);

            if (worldHandler != null) {
                // Eclipses
                try {
                    if (worldHandler.getClass().getField("solarEclipse").getBoolean(worldHandler))
                        lines.add(new InfoLine("Event: ", "SOLAR ECLIPSE", 0xFF0000));
                    if (worldHandler.getClass().getField("lunarEclipse").getBoolean(worldHandler))
                        lines.add(new InfoLine("Event: ", "LUNAR ECLIPSE", 0x9900FF));
                } catch (Exception ignored) {}

                // Aktive Konstellationen mit Farben
                Method getActive = worldHandler.getClass().getMethod("getActiveConstellations");
                Collection<?> activeList = (Collection<?>) getActive.invoke(worldHandler);

                if (activeList != null && !activeList.isEmpty()) {
                    for (Object constellation : activeList) {
                        Method getNameMethod = constellation.getClass().getMethod("getUnlocalizedName");
                        String fullName = (String) getNameMethod.invoke(constellation);
                        String cleanName = fullName.substring(fullName.lastIndexOf('.') + 1);
                        cleanName = cleanName.substring(0, 1).toUpperCase() + cleanName.substring(1);

                        // Farbe basierend auf dem Namen zuweisen
                        int color = getConstellationColor(cleanName.toLowerCase());
                        lines.add(new InfoLine(" ★ ", cleanName, color));
                    }
                }
            }
        } catch (Exception e) {}

        return lines;
    }

    private String getMoonPhaseName(int phase) {
        switch (phase) {
            case 0: return "Full Moon";
            case 1: return "Waning Gibbous";
            case 2: return "Third Quarter";
            case 3: return "Waning Crescent";
            case 4: return "New Moon";
            case 5: return "Waxing Crescent";
            case 6: return "First Quarter";
            case 7: return "Waxing Gibbous";
            default: return "Unknown";
        }
    }

    private int getConstellationColor(String name) {
        switch (name) {
            // Bright (Tier 1)
            case "discedia": return 0xBE0000; // Rot
            case "aevitas":  return 0x00BE00; // Grün
            case "vicio":    return 0x00BEBE; // Hellblau
            case "armara":   return 0xBEBEBE; // Weiß/Grau
            case "evorsio":  return 0xBE00BE; // Lila
            // Dim (Tier 2)
            case "lucerna":  return 0xFFEE00; // Gelb
            case "mineralis":return 0x91552B; // Braun
            case "horologium":return 0x4F5970; // Blaugrau
            case "octans":   return 0x0040FF; // Tiefblau
            case "bootes":   return 0x336333; // Dunkelgrün
            case "fornax":   return 0xFF7300; // Orange
            case "pelotrio": return 0xFFE082; // Blassgelb
            default: return 0xFFAA00; // Standard Orange
        }
    }
}