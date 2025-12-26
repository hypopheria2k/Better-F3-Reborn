package com.worador.f3hud;

import net.minecraft.client.multiplayer.ServerData;

import java.util.ArrayList;
import java.util.List;

public class ServerModule extends InfoModule {
    
    @Override
    public String getName() {
        return "Server";
    }
    
    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showServer;
    }
    
    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        
        if (mc.isSingleplayer()) {
            lines.add(new InfoLine("Integrated Server ", "at 27 ms ticks", ModConfig.colors.colorDefault));
        } else {
            ServerData serverData = mc.getCurrentServerData();
            if (serverData != null) {
                lines.add(new InfoLine("Server: ", serverData.serverIP, ModConfig.colors.colorDefault));
            }
        }
        
        // Packets
        if (mc.player != null && mc.player.connection != null) {
            lines.add(new InfoLine("Packets Sent: ", "?", ModConfig.colors.colorDefault));
            lines.add(new InfoLine("Packets Received: ", "?", ModConfig.colors.colorDefault));
        }
        
        return lines;
    }
}