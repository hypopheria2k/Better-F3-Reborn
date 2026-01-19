package com.worador.f3hud.compat;

import com.worador.f3hud.InfoModule.InfoLine;
import net.minecraft.world.World;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AstralCompat {

    public static List<InfoLine> getAstralLines(World world) {
        List<InfoLine> lines = new ArrayList<>();

        try {
            Class<?> cSkyHandlerClass = Class.forName("hellfirepvp.astralsorcery.common.constellation.distribution.ConstellationSkyHandler");
            Object cSkyHandlerInstance = cSkyHandlerClass.getMethod("getInstance").invoke(null);

            // 1. STARLIGHT & MONDPHASE (Soll immer sichtbar sein)
            Method getDist = cSkyHandlerClass.getMethod("getCurrentDaytimeDistribution", World.class);
            float starlight = (Float) getDist.invoke(cSkyHandlerInstance, world);

            String moonPhase = getMoonPhaseName(world.getMoonPhase());
            lines.add(new InfoLine("Astral: ", String.format("%.0f%% (%s)", starlight * 100, moonPhase), 0x00A9FF));

            // 2. WELT-HANDLER
            Object worldHandler = cSkyHandlerClass.getMethod("getWorldHandler", World.class).invoke(cSkyHandlerInstance, world);

            if (worldHandler != null) {
                // Eclipses (Sollen auch immer sichtbar sein, wenn sie passieren)
                try {
                    if (worldHandler.getClass().getField("solarEclipse").getBoolean(worldHandler))
                        lines.add(new InfoLine("Event: ", "SOLAR ECLIPSE", 0xFF0000));
                    if (worldHandler.getClass().getField("lunarEclipse").getBoolean(worldHandler))
                        lines.add(new InfoLine("Event: ", "LUNAR ECLIPSE", 0x9900FF));
                } catch (Exception ignored) {}

                // --- HIER IST DIE ÄNDERUNG ---
                // Wir prüfen die Weltzeit: 12500 ist Sonnenuntergang, 23500 ist Sonnenaufgang
                long time = world.getWorldTime() % 24000;
                boolean isNight = (time >= 12500 && time <= 23500);

                // Aktive Konstellationen NUR anzeigen, wenn es Nacht ist
                if (isNight) {
                    Method getActive = worldHandler.getClass().getMethod("getActiveConstellations");
                    Collection<?> activeList = (Collection<?>) getActive.invoke(worldHandler);

                    if (activeList != null && !activeList.isEmpty()) {
                        for (Object constellation : activeList) {
                            Method getNameMethod = constellation.getClass().getMethod("getUnlocalizedName");
                            String fullName = (String) getNameMethod.invoke(constellation);
                            String cleanName = fullName.substring(fullName.lastIndexOf('.') + 1);
                            cleanName = cleanName.substring(0, 1).toUpperCase() + cleanName.substring(1);

                            int color = getConstellationColor(cleanName.toLowerCase());
                            lines.add(new InfoLine(" ★ ", cleanName, color));
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Falls Reflection fehlschlägt
        }

        return lines;
    }

    // ... (getMoonPhaseName und getConstellationColor bleiben gleich wie vorher)
    private static String getMoonPhaseName(int phase) {
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

    private static int getConstellationColor(String name) {
        switch (name) {
            case "discedia": return 0xBE0000;
            case "aevitas":  return 0x00BE00;
            case "vicio":    return 0x00BEBE;
            case "armara":   return 0xBEBEBE;
            case "evorsio":  return 0xBE00BE;
            case "lucerna":  return 0xFFEE00;
            case "mineralis":return 0x91552B;
            case "horologium":return 0x4F5970;
            case "octans":   return 0x0040FF;
            case "bootes":   return 0x336333;
            case "fornax":   return 0xFF7300;
            case "pelotrio": return 0xFFE082;
            default: return 0xFFAA00;
        }
    }
} 
