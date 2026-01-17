package com.worador.f3hud;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = "betterf3reborn", name = "betterf3reborn", category = "")
public class ModConfig {

    @Config.Ignore
    public static boolean forceOpen = false;

    @Config.Comment("Module Settings - Enable/Disable different info modules")
    @Config.Name("Modules")
    public static Modules modules = new Modules();

    @Config.Comment("Color Settings - Modern Palette for Shadow-Text")
    @Config.Name("Colors")
    public static Colors colors = new Colors();

    @Config.Comment("Position Settings")
    @Config.Name("Position")
    public static Position position = new Position();

    @Config.Comment("Animation Settings")
    @Config.Name("Animation")
    public static Animation animation = new Animation();

    public static class Modules {
        public boolean showChatMessages = false;
        public boolean showFPS = true;
        public boolean showCoordinates = true;
        public boolean showRotation = true;
        public boolean showWorld = true;
        public boolean showDimension = true;
        public boolean showRegion = true;
        public boolean showTargetedBlock = true;
        public boolean showEntities = true;

        @Config.Comment("Show System & Server Info (TPS, RAM, CPU, GPU)")
        public boolean showSystem = true;

        @Config.Comment("Show Performance Graph (F3 + X)")
        public boolean showPerformanceGraph = false;

        public boolean showCompass = true;

        // Mod-Support
        public boolean showAstralSorcery = true;
        public boolean showStellar = true;
        public boolean showBotania = true;
        public boolean showBloodMagic = true;
        public boolean showThaumcraft = true;
    }

    public static class Colors {
        public int colorFPS = 0xAAFF55;
        public int colorX = 0xFF4444;
        public int colorY = 0x44FF44;
        public int colorZ = 0x55AAFF;

        public int colorChunk = 0x55FFFF;
        public int colorBlock = 0xFF55FF;
        public int colorLight = 0xFFEE55;
        public int colorBiome = 0x55FFBB;
        public int colorDimension = 0xBB88FF;

        // NEU: Für das Rotation-Modul
        public int colorRotation = 0xF0F0F0;

        public int colorMonsters = 0xFF5555;
        public int colorCreatures = 0x55FF55;

        public int colorSystem = 0x55FFFF;     // Hardware-Cyan (AMD Style)
        public int colorDefault = 0xF0F0F0;

        public int colorBotania = 0x00FFCC;
        public int colorBloodMagic = 0xCC0000;
        public int colorThaumcraft = 0xAA55FF;
        public int colorAstral = 0x99CCFF;
    }

    public static class Position {
        @Config.Comment("The maximum scale for the HUD (Default 0.7-1.0)")
        public double userScale = 1.0;

        public int leftOffsetX = 4;
        public int leftOffsetY = 4;
        public int rightOffsetX = 4;
        public int rightOffsetY = 4;
    }

    public static class Animation {
        public boolean enableAnimation = true;
        @Config.RangeDouble(min = 0.01, max = 1.0)
        public double animationSpeed = 0.1;
        public int slideDistance = 50;

        @Config.Comment("Alpha for Compass & Graph background (0-255)")
        @Config.RangeInt(min = 0, max = 255)
        public int backgroundAlpha = 100; // Etwas transparenter für den modernen Look
    }

    @Mod.EventBusSubscriber(modid = "betterf3reborn")
    public static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals("betterf3reborn")) {
                ConfigManager.sync("betterf3reborn", Config.Type.INSTANCE);
            }
        }
    }
}