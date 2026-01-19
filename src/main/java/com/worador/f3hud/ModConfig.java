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
    @Config.LangKey("f3hud.config.category.modules")
    public static Modules modules = new Modules();

    @Config.Comment("Color Settings - Modern Palette for Shadow-Text")
    @Config.Name("Colors")
    @Config.LangKey("f3hud.config.category.colors")
    public static Colors colors = new Colors();

    @Config.Comment("Position Settings")
    @Config.Name("Position")
    @Config.LangKey("f3hud.config.category.position")
    public static Position position = new Position();

    @Config.Comment("Animation Settings")
    @Config.Name("Animation")
    @Config.LangKey("f3hud.config.category.animation")
    public static Animation animation = new Animation();

    public static class Modules {
        @Config.LangKey("f3hud.config.modules.showChatMessages")
        public boolean showChatMessages = false;

        @Config.LangKey("f3hud.config.modules.showFPS")
        public boolean showFPS = true;

        @Config.LangKey("f3hud.config.modules.showCoordinates")
        public boolean showCoordinates = true;

        @Config.LangKey("f3hud.config.modules.showRotation")
        public boolean showRotation = true;

        @Config.LangKey("f3hud.config.modules.showWorld")
        public boolean showWorld = true;

        @Config.LangKey("f3hud.config.modules.showDimension")
        public boolean showDimension = true;

        @Config.LangKey("f3hud.config.modules.showRegion")
        public boolean showRegion = true;

        @Config.LangKey("f3hud.config.modules.showTargetedBlock")
        public boolean showTargetedBlock = true;

        @Config.LangKey("f3hud.config.modules.showEntities")
        public boolean showEntities = true;

        @Config.Comment("Show System & Server Info (TPS, RAM, CPU, GPU)")
        @Config.LangKey("f3hud.config.modules.showSystem")
        public boolean showSystem = true;

        @Config.Comment("Show server information like IP and MOTD.")
        @Config.LangKey("f3hud.config.modules.showServer")
        public boolean showServer = true;

        @Config.Comment("Show Performance Graph (F3 + X)")
        @Config.LangKey("f3hud.config.modules.showPerformanceGraph")
        public boolean showPerformanceGraph = false;

        @Config.LangKey("f3hud.config.modules.showCompass")
        public boolean showCompass = true;

        // Mod-Support
        public boolean showAstralSorcery = true;
        public boolean showStellar = true;
        public boolean showBotania = true;
        public boolean showBloodMagic = true;
        public boolean showThaumcraft = true;
    }

    public static class Colors {
        @Config.LangKey("f3hud.config.colors.colorFPS") public int colorFPS = 0xAAFF55;
        @Config.LangKey("f3hud.config.colors.colorX") public int colorX = 0xFF4444;
        @Config.LangKey("f3hud.config.colors.colorY") public int colorY = 0x44FF44;
        @Config.LangKey("f3hud.config.colors.colorZ") public int colorZ = 0x55AAFF;
        @Config.LangKey("f3hud.config.colors.colorChunk") public int colorChunk = 0x55FFFF;
        @Config.LangKey("f3hud.config.colors.colorBlock") public int colorBlock = 0xFF55FF;
        @Config.LangKey("f3hud.config.colors.colorLight") public int colorLight = 0xFFEE55;
        @Config.LangKey("f3hud.config.colors.colorBiome") public int colorBiome = 0x55FFBB;
        @Config.LangKey("f3hud.config.colors.colorDimension") public int colorDimension = 0xBB88FF;
        @Config.LangKey("f3hud.config.colors.colorRotation") public int colorRotation = 0xF0F0F0;
        @Config.LangKey("f3hud.config.colors.colorMonsters") public int colorMonsters = 0xFF5555;
        @Config.LangKey("f3hud.config.colors.colorCreatures") public int colorCreatures = 0x55FF55;
        @Config.LangKey("f3hud.config.colors.colorSystem") public int colorSystem = 0x55FFFF;
        @Config.LangKey("f3hud.config.colors.colorDefault") public int colorDefault = 0xF0F0F0;
        @Config.LangKey("f3hud.config.colors.colorBotania") public int colorBotania = 0x00FFCC;
        @Config.LangKey("f3hud.config.colors.colorBloodMagic") public int colorBloodMagic = 0xCC0000;
        @Config.LangKey("f3hud.config.colors.colorThaumcraft") public int colorThaumcraft = 0xAA55FF;
        @Config.LangKey("f3hud.config.colors.colorAstral") public int colorAstral = 0x99CCFF;
    }

    public static class Position {
        @Config.LangKey("f3hud.config.position.userScale")
        @Config.Comment("The overall scale of the HUD text (Default: 0.8)")
        @Config.RangeDouble(min = 0.1, max = 2.0)
        public double userScale = 0.8;

        @Config.LangKey("f3hud.config.position.leftX")
        @Config.Comment("Horizontal margin for the left side (Default: 8)")
        public int leftX = 8;

        @Config.LangKey("f3hud.config.position.leftY")
        @Config.Comment("Vertical margin for the left side (Default: 8)")
        public int leftY = 8;

        @Config.LangKey("f3hud.config.position.rightX")
        @Config.Comment("Horizontal margin for the right side (Default: 8)")
        public int rightX = 8;

        @Config.LangKey("f3hud.config.position.rightY")
        @Config.Comment("Vertical margin for the right side (Default: 8)")
        public int rightY = 8;

        @Config.Comment("Vertical offset for the Compass (Default: 40 to avoid overlap with TAN)")
        public int compassYOffset = 40;
    }

    public static class Animation {
        @Config.LangKey("f3hud.config.animation.enableAnimation")
        @Config.Comment("Enable sliding animations for HUD elements")
        public boolean enableAnimation = true;

        @Config.LangKey("f3hud.config.animation.animationSpeed")
        @Config.RangeDouble(min = 0.01, max = 1.0)
        public double animationSpeed = 0.1;

        @Config.LangKey("f3hud.config.animation.slideDistance")
        @Config.Comment("Distance for the sliding animation")
        public int slideDistance = 50;

        @Config.LangKey("f3hud.config.animation.backgroundAlpha")
        @Config.Comment("Alpha for Compass & Graph background (0-255)")
        @Config.RangeInt(min = 0, max = 255)
        public int backgroundAlpha = 100;

        @Config.Comment("Show a background box behind text modules")
        public boolean showTextBackground = true;

        @Config.Comment("Transparency of the text background (0-255)")
        @Config.RangeInt(min = 0, max = 255)
        public int textBackgroundAlpha = 100;

        @Config.Comment("Color of the text background (Hex, e.g. 0x000000 for black)")
        public String textBackgroundColor = "0x000000";
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
