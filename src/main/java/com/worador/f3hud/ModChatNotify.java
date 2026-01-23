package com.worador.f3hud;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class ModChatNotify {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        // Version wird direkt aus der Hauptklasse gezogen
        String message = "§b§l" + F3HudMod.NAME + " §7loaded. §8(Version: " + F3HudMod.VERSION + ")\n" +
                "§7To configure, use §6§lSTRG+C §7for the Ingame-Menu.";

        event.player.sendMessage(new TextComponentString(message));
    }
}