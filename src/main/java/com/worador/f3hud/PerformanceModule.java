package com.worador.f3hud;

import com.worador.f3hud.InfoModule;
import com.worador.f3hud.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class PerformanceModule extends InfoModule {

    @Override
    public String getName() {
        return "Performance";
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();

        if (!ModConfig.modules.showPerformance) return lines;

        MinecraftServer server = Minecraft.getMinecraft().getIntegratedServer();

        if (server != null) {
            double mspt = mean(server.tickTimeArray) * 1.0E-6D;
            double tps = Math.min(20.0D, 1000.0D / mspt);

            // Farblogik für TPS (Stabilität)
            int tpsColor = (tps > 18.5) ? 0x00FF00 : (tps > 15.0 ? 0xFFFF00 : 0xFF0000);

            // Farblogik für MSPT (CPU Auslastung)
            int msptColor;
            if (mspt < 30.0) {
                msptColor = 0x00FF00; // Grün: Optimal
            } else if (mspt < 45.0) {
                msptColor = 0xFFFF00; // Gelb: Ok, aber Last steigt
            } else {
                msptColor = 0xFF0000; // Rot: Schlecht (Server-Lag)
            }

            lines.add(new InfoLine("Server TPS: ", String.format("%.2f", tps), tpsColor));
            lines.add(new InfoLine("Tick Time: ", String.format("%.2f ms", mspt), msptColor));
        } else {
            lines.add(new InfoLine("Server: ", "Multiplayer", ModConfig.colors.colorDefault));
        }

        return lines;
    }

    private double mean(long[] values) {
        long sum = 0L;
        for (long v : values) sum += v;
        return (double) sum / values.length;
    }
}