package com.worador.f3hud;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "betterf3reborn", name = "Better F3 Reborn", version = "1.0.0")
public class ModConfig {
    
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
    
    // MODULE SETTINGS
    public static class Modules {
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
    }
    
    // COLOR SETTINGS
    public static class Colors {
        @Config.Comment("Color for FPS text")
        @Config.RangeInt(min = 0x000000, max = 0xFFFFFF)
        public int colorFPS = 0xFFFF55;
        
        @Config.Comment("Color for X coordinate")
        @Config.RangeInt(min = 0x000000, max = 0xFFFFFF)
        public int colorX = 0xFF5555;
        
        @Config.Comment("Color for Y coordinate")
        @Config.RangeInt(min = 0x000000, max = 0xFFFFFF)
        public int colorY = 0x55FF55;
        
        @Config.Comment("Color for Z coordinate")
        @Config.RangeInt(min = 0x000000, max = 0xFFFFFF)
        public int colorZ = 0x5555FF;
        
        @Config.Comment("Color for Chunk info (Cyan)")
        @Config.RangeInt(min = 0x000000, max = 0xFFFFFF)
        public int colorChunk = 0x55FFFF;
        
        @Config.Comment("Color for Block coordinates (Red)")
        @Config.RangeInt(min = 0x000000, max = 0xFFFFFF)
        public int colorBlock = 0xFF5555;
        
        @Config.Comment("Color for Light info (Yellow)")
        @Config.RangeInt(min = 0x000000, max = 0xFFFFFF)
        public int colorLight = 0xFFFF55;
        
        @Config.Comment("Color for Biome (Yellow/Green)")
        @Config.RangeInt(min = 0x000000, max = 0xFFFFFF)
        public int colorBiome = 0xFFFF55;
        
        @Config.Comment("Color for Dimension (Green)")
        @Config.RangeInt(min = 0x000000, max = 0xFFFFFF)
        public int colorDimension = 0x55FF55;
        
        @Config.Comment("Color for Monsters (Red)")
        @Config.RangeInt(min = 0x000000, max = 0xFFFFFF)
        public int colorMonsters = 0xFF5555;
        
        @Config.Comment("Color for Creatures (Green)")
        @Config.RangeInt(min = 0x000000, max = 0xFFFFFF)
        public int colorCreatures = 0x55FF55;
        
        @Config.Comment("Color for System info (Cyan)")
        @Config.RangeInt(min = 0x000000, max = 0xFFFFFF)
        public int colorSystem = 0x55FFFF;
        
        @Config.Comment("Color for general white text")
        @Config.RangeInt(min = 0x000000, max = 0xFFFFFF)
        public int colorDefault = 0xFFFFFF;
    }
    
    // POSITION SETTINGS
    public static class Position {
        @Config.Comment("Scale of the HUD (0.5 = 50%, 1.0 = 100%, 1.5 = 150%)")
        @Config.RangeDouble(min = 0.5, max = 2.0)
        public double scale = 0.75;
        
        @Config.Comment("Left side X offset (pixels)")
        @Config.RangeInt(min = 0, max = 500)
        public int leftOffsetX = 4;
        
        @Config.Comment("Left side Y offset (pixels)")
        @Config.RangeInt(min = 0, max = 500)
        public int leftOffsetY = 4;
        
        @Config.Comment("Right side X offset (pixels from right edge)")
        @Config.RangeInt(min = 0, max = 500)
        public int rightOffsetX = 4;
        
        @Config.Comment("Right side Y offset (pixels)")
        @Config.RangeInt(min = 0, max = 500)
        public int rightOffsetY = 4;
    }
    
    // ANIMATION SETTINGS
    public static class Animation {
        @Config.Comment("Enable slide-in animation")
        public boolean enableAnimation = true;
        
        @Config.Comment("Animation speed (0.05 = slow, 0.2 = fast)")
        @Config.RangeDouble(min = 0.01, max = 0.5)
        public double animationSpeed = 0.1;
        
        @Config.Comment("Slide distance in pixels")
        @Config.RangeInt(min = 0, max = 200)
        public int slideDistance = 50;
        
        @Config.Comment("Background transparency (0 = invisible, 255 = opaque)")
        @Config.RangeInt(min = 0, max = 255)
        public int backgroundAlpha = 160;
    }
    
    // Event Handler für Config-Änderungen
    @Mod.EventBusSubscriber(modid = "f3hud")
    public static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals("f3hud")) {
                ConfigManager.sync("f3hud", Config.Type.INSTANCE);
            }
        }
    }
}
