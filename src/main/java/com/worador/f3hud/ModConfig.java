package com.worador.f3hud;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = "betterf3reborn", name = "betterf3reborn", category = "")
public class ModConfig {

    // WICHTIG: Die @Config.Ignore Annotation verhindert, dass Forge
    // versucht, diese Variable in die .cfg Datei zu schreiben.
    @Config.Ignore
    public static boolean forceOpen = false;

    @Config.Comment("Module Settings - Enable/Disable different info modules")
    @Config.Name("Modules")
    public static Modules modules = new Modules();

    @Config.Comment("Color Settings - Customize colors (RGB Hex format: 0xRRGGBB)")
    @Config.Name("Colors")
    public static Colors colors = new Colors();

    @Config.Comment("Position Settings")
    @Config.Name("Position")
    public static Position position = new Position();

    @Config.Comment("Animation Settings")
    @Config.Name("Animation")
    public static Animation animation = new Animation();

    public static class Modules {
        @Config.Comment("Show chat notifications when toggling modules")
        public boolean showChatMessages = false;

        @Config.Comment("Show FPS line at the top")
        public boolean showFPS = true;

        @Config.Comment("Show Coordinates (XYZ, Block, Chunk)")
        public boolean showCoordinates = true;

        @Config.Comment("Show Rotation (Yaw, Pitch)")
        public boolean showRotation = true;

        @Config.Comment("Show World Info (Chunks, Facing, Light, Biome, Days, Slime)")
        public boolean showWorld = true;

        @Config.Comment("Show Dimension")
        public boolean showDimension = true;

        @Config.Comment("Show Region File")
        public boolean showRegion = true;

        @Config.Comment("Show Targeted Block")
        public boolean showTargetedBlock = true;

        @Config.Comment("Show Server Info")
        public boolean showServer = true;

        @Config.Comment("Show Entities Info (Particles, Monsters, Creatures)")
        public boolean showEntities = true;

        @Config.Comment("Show System Info (Right side: Java, Memory, CPU, GPU)")
        public boolean showSystem = true;

        @Config.Comment("Show Performance Graph (FPS over time)")
        public boolean showPerformanceGraph = false;

        @Config.Comment("Show Compass")
        public boolean showCompass = true;
    }

    public static class Colors {
        public int colorFPS = 0xFFFF55;
        public int colorX = 0xFF5555;
        public int colorY = 0x55FF55;
        public int colorZ = 0x5555FF;
        public int colorChunk = 0x55FFFF;
        public int colorBlock = 0xFF5555;
        public int colorLight = 0xFFFF55;
        public int colorBiome = 0xFFFF55;
        public int colorDimension = 0x55FF55;
        public int colorMonsters = 0xFF5555;
        public int colorCreatures = 0x55FF55;
        public int colorSystem = 0x55FFFF;
        public int colorDefault = 0xFFFFFF;
    }

    public static class Position {
        public double scale = 0.75;
        public int leftOffsetX = 4;
        public int leftOffsetY = 4;
        public int rightOffsetX = 4;
        public int rightOffsetY = 4;
    }

    public static class Animation {
        public boolean enableAnimation = true;
        public double animationSpeed = 0.1;
        public int slideDistance = 50;
        public int backgroundAlpha = 160;
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