package com.worador.f3hud;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import com.worador.f3hud.render.RendererRegistry;

@Mod(modid = F3HudMod.MODID, name = F3HudMod.NAME, version = F3HudMod.VERSION)
public class F3HudMod {
    public static final String MODID = "betterf3reborn";
    public static final String NAME = "Better F3 Reborn";
    public static final String VERSION = "2.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // 1. Module laden
        ModuleRegistry.init();

        // 2. Den Renderer für das HUD registrieren
        MinecraftForge.EVENT_BUS.register(new DebugRenderer());

        // 3. NEU: Den KeybindHandler registrieren, damit die Tasten funktionieren
        MinecraftForge.EVENT_BUS.register(new KeybindHandler());
    }
}