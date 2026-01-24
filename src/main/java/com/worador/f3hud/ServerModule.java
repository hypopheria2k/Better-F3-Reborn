package com.worador.f3hud;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerPlayClient;
import java.util.ArrayList;
import java.util.List;

public class ServerModule extends InfoModule {

    private long lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override
    public String getName() {
        // Muss exakt "Server" heißen, damit das Mapping zu "showServer" in der Config klappt
        return "Server";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showServer;
    }

    @Override
    public List<InfoLine> getLines() {
        if (!isEnabledInConfig() || mc.player == null) return new ArrayList<>();

        if (mc.player.ticksExisted - lastUpdateTick < 20 && !cachedLines.isEmpty()) {
            return cachedLines;
        }

        List<InfoLine> lines = new ArrayList<>();
        int defaultValColor = ModConfig.colors.colorServer;

        if (mc.isSingleplayer()) {
            if (mc.getIntegratedServer() != null) {
                long[] times = mc.getIntegratedServer().tickTimeArray;
                long sum = 0;
                for (long t : times) sum += t;
                double avgMspt = (double) sum / times.length * 1.0E-6D;

                // Status-Farbe für die Zahl, Label nutzt Config-Farbe
                int statusColor = avgMspt < 40 ? 0x55FF55 : (avgMspt < 50 ? 0xFFFF55 : 0xFF5555);
                lines.add(new InfoLine("Integrated Server: ", String.format("%.1f mspt", avgMspt), statusColor));
            }
        } else {
            ServerData serverData = mc.getCurrentServerData();
            if (serverData != null) {
                // IP nutzt die Standard-Farbe aus der Config
                lines.add(new InfoLine("Server: ", serverData.serverIP, defaultValColor));

                // Ping nutzt dynamische Status-Farbe
                int ping = (int) serverData.pingToServer;
                int pingColor = ping < 50 ? 0x55FF55 : (ping < 150 ? 0xFFFF55 : 0xFF5555);
                lines.add(new InfoLine("Ping: ", ping + "ms", pingColor));
            }
        }

        NetHandlerPlayClient netHandler = mc.getConnection();
        if (netHandler != null) {
            String brand = mc.player.getServerBrand();
            if (brand != null) {
                // Brand nutzt wieder die Config-Farbe
                lines.add(new InfoLine("Type: ", brand, defaultValColor));
            }
        }

        this.cachedLines = lines;
        this.lastUpdateTick = mc.player.ticksExisted;
        return lines;
    }
}