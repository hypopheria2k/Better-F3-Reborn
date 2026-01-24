package com.worador.f3hud;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
        modid = F3HudMod.MODID,
        name = F3HudMod.NAME,
        version = F3HudMod.VERSION,
        guiFactory = "com.worador.f3hud.GuiFactory" // Ermöglicht den Config-Button im Mod-Menü
)
public class F3HudMod {
    public static final String MODID = "betterf3reborn";
    public static final String NAME = "Better F3 Reborn";
    public static final String VERSION = "2.2.0-Beta3"; // Version hier aktualisiert

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // 1. Module laden
        ModuleRegistry.init();

        // 2. Den Renderer für das HUD registrieren
        MinecraftForge.EVENT_BUS.register(new DebugRenderer());

        // 3. Den KeybindHandler registrieren
        MinecraftForge.EVENT_BUS.register(new KeybindHandler());

        // 4. Chat-Benachrichtigung beim Login registrieren
        // Wir nutzen eine Instanz, um sie an beiden Bussen anzumelden
        ModChatNotify chatNotify = new ModChatNotify();
        MinecraftForge.EVENT_BUS.register(chatNotify);
        net.minecraftforge.fml.common.FMLCommonHandler.instance().bus().register(chatNotify);
    }
}