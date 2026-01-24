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

    /**
     * Wendet eines der drei Presets an.
     * 0 = General, 1 = PvP/Survival, 2 = Technical
     */
    public static void applyPreset(int type) {
        // Erstmal alles auf einen definierten Zustand bringen
        setAllModules(false);

        switch (type) {
            case 0: // GENERAL (Gameplay & Immersion)
                modules.showFPS = true;
                modules.showCoordinates = true;
                modules.showBiome = true;
                modules.showLightLevel = true;
                modules.showDimension = true;
                modules.showGrowth = true;
                modules.showHealthAndHunger = true;
                modules.showHealthStats = true;
                modules.showTime = true;
                modules.showWeather = true;
                break;

            case 1: // PVP / SURVIVAL (Effizienz)
                modules.showFPS = true;
                modules.showCoordinates = true;
                modules.showSpeedometer = true;
                modules.showPotion = true;
                modules.showDurability = true;
                modules.showMobAggro = true;
                modules.showAggroModule = true;
                modules.showItemDespawn = true;
                modules.showOxygen = true;
                modules.showHealthAndHunger = true;
                modules.showHealthStats = true;
                break;

            case 2: // TECHNICAL (Debugging & Technik)
                modules.showFPS = true;
                modules.showCoordinates = true;
                modules.showChunkPos = true;
                modules.showRotation = true;
                modules.showWorld = true;
                modules.showRegion = true;
                modules.showDetailedChunks = true;
                modules.showTargetedBlock = true;
                modules.showEntityStats = true;
                modules.showEntities = true;
                modules.showPerformanceGraph = true;
                modules.showSystem = true;
                modules.showServer = true;
                modules.showMachineProgress = true;
                break;
        }
        // Änderungen permanent speichern
        ConfigManager.sync("betterf3reborn", Config.Type.INSTANCE);
    }

    /**
     * Hilfsmethode um alle Module auf einmal zu schalten
     */
    private static void setAllModules(boolean state) {
        modules.showFPS = state;
        modules.showCoordinates = state;
        modules.showBiome = state;
        modules.showLightLevel = state;
        modules.showDimension = state;
        modules.showVersion = state;
        modules.showCompass = state;
        modules.showSlimeChunk = state;
        modules.showSlimeDistance = state;
        modules.showSpeedometer = state;
        modules.showWeather = state;
        modules.showTime = state;
        modules.showChunkPos = state;
        modules.showDurability = state;
        modules.showGrowth = state;
        modules.showPotion = state;
        modules.showOxygen = state;
        modules.showMobAggro = state;
        modules.showAggroModule = state;
        modules.showVillagerTrade = state;
        modules.showVillagerStatus = state;
        modules.showItemDespawn = state;
        modules.showExplosion = state;
        modules.showHealthAndHunger = state;
        modules.showHealthStats = state;
        modules.showBeacon = state;
        modules.showBeaconRange = state;
        modules.showMachineProgress = state;
        modules.showRotation = state;
        modules.showWorld = state;
        modules.showRegion = state;
        modules.showDetailedChunks = state;
        modules.showTargetedBlock = state;
        modules.showEntityStats = state;
        modules.showEntities = state;
        modules.showPerformanceGraph = state;
        modules.showSystem = state;
        modules.showServer = state;
        modules.showTravelModule = state;
        modules.showHealthBars = state;
        modules.showAstral = state;
        modules.showAstralSorcery = state;
        modules.showStellar = state;
        modules.showBotania = state;
        modules.showBloodMagic = state;
        modules.showThaumcraft = state;
        modules.showEnd = state;
    }

    public static class Modules {
        // Kategorie: General Info
        @Config.LangKey("f3hud.config.modules.showFPS")
        public boolean showFPS = true;

        @Config.LangKey("f3hud.config.modules.showCoordinates")
        public boolean showCoordinates = true;

        @Config.LangKey("f3hud.config.modules.showBiome")
        public boolean showBiome = true;

        @Config.LangKey("f3hud.config.modules.showLightLevel")
        public boolean showLightLevel = true;

        @Config.LangKey("f3hud.config.modules.showDimension")
        public boolean showDimension = true;

        @Config.LangKey("f3hud.config.modules.showVersion")
        public boolean showVersion = true;

        // Kategorie: World & Navigation
        @Config.LangKey("f3hud.config.modules.showCompass")
        public boolean showCompass = true;

        @Config.LangKey("f3hud.config.modules.showSlimeChunk")
        public boolean showSlimeChunk = true;
        public boolean showSlimeDistance = true;

        @Config.LangKey("f3hud.config.modules.showSpeedometer")
        public boolean showSpeedometer = true;

        @Config.LangKey("f3hud.config.modules.showWeather")
        public boolean showWeather = true;

        @Config.LangKey("f3hud.config.modules.showTime")
        public boolean showTime = true;

        @Config.LangKey("f3hud.config.modules.showChunkPos")
        public boolean showChunkPos = false;

        // Kategorie: Gameplay Helpers
        @Config.LangKey("f3hud.config.modules.showDurability")
        public boolean showDurability = true;

        @Config.LangKey("f3hud.config.modules.showGrowth")
        public boolean showGrowth = true;

        @Config.LangKey("f3hud.config.modules.showPotion")
        public boolean showPotion = true;

        @Config.LangKey("f3hud.config.modules.showOxygen")
        public boolean showOxygen = true;

        @Config.LangKey("f3hud.config.modules.showMobAggro")
        public boolean showMobAggro = true;

        @Config.Comment("Alias for MobAggro (Fixed Compiler Error)")
        public boolean showAggroModule = true;

        @Config.LangKey("f3hud.config.modules.showVillagerTrade")
        public boolean showVillagerTrade = true;
        public boolean showVillagerStatus = true;

        @Config.LangKey("f3hud.config.modules.showItemDespawn")
        public boolean showItemDespawn = true;

        @Config.LangKey("f3hud.config.modules.showExplosion")
        public boolean showExplosion = true;

        @Config.LangKey("f3hud.config.modules.showHealthAndHunger")
        public boolean showHealthAndHunger = true;

        @Config.Comment("Fixt Compiler Error in HealthAndHungerModule")
        public boolean showHealthStats = true;

        @Config.LangKey("f3hud.config.modules.showBeacon")
        public boolean showBeacon = true;

        @Config.Comment("Toggle Beacon range display logic")
        public boolean showBeaconRange = true;

        @Config.LangKey("f3hud.config.modules.showMachineProgress")
        public boolean showMachineProgress = true;

        // Kategorie: Technical/Debug (Default: false)
        @Config.LangKey("f3hud.config.modules.showRotation")
        public boolean showRotation = false;

        @Config.LangKey("f3hud.config.modules.showWorld")
        public boolean showWorld = false;

        @Config.LangKey("f3hud.config.modules.showRegion")
        public boolean showRegion = false;

        @Config.LangKey("f3hud.config.modules.showDetailedChunks")
        public boolean showDetailedChunks = false;

        @Config.LangKey("f3hud.config.modules.showTargetedBlock")
        public boolean showTargetedBlock = false;

        @Config.LangKey("f3hud.config.modules.showEntityStats")
        public boolean showEntityStats = false;

        @Config.Comment("Fixt Compiler Error in EntitiesModule")
        public boolean showEntities = true;

        @Config.LangKey("f3hud.config.modules.showPerformanceGraph")
        public boolean showPerformanceGraph = false;

        @Config.LangKey("f3hud.config.modules.showSystem")
        public boolean showSystem = true;

        @Config.LangKey("f3hud.config.modules.showServer")
        public boolean showServer = true;

        @Config.LangKey("f3hud.config.modules.showTravelModule")
        public boolean showTravelModule = true;

        @Config.LangKey("f3hud.config.modules.showHealthBars")
        public boolean showHealthBars = false;

        @Config.Comment("Show notifications in chat when toggling modules")
        public boolean showChatMessages = true;

        // Mod-Support (Standardized)
        @Config.LangKey("f3hud.config.modules.showAstral")
        public boolean showAstral = true;

        @Config.Comment("Fixt Compiler Error in AstralModule")
        public boolean showAstralSorcery = true;

        @Config.LangKey("f3hud.config.modules.showStellar")
        public boolean showStellar = true;

        @Config.LangKey("f3hud.config.modules.showBotania")
        public boolean showBotania = true;

        @Config.LangKey("f3hud.config.modules.showBloodMagic")
        public boolean showBloodMagic = true;

        @Config.LangKey("f3hud.config.modules.showThaumcraft")
        public boolean showThaumcraft = true;

        @Config.LangKey("f3hud.config.modules.showEnd")
        public boolean showEnd = true;
    }

    public static class Colors {
        @Config.LangKey("f3hud.config.colors.colorFPS") public int colorFPS = 0xAAFF55;
        @Config.LangKey("f3hud.config.colors.colorX") public int colorX = 0xF0F0F0;
        @Config.LangKey("f3hud.config.colors.colorY") public int colorY = 0xF0F0F0;
        @Config.LangKey("f3hud.config.colors.colorZ") public int colorZ = 0xF0F0F0;
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

        @Config.LangKey("f3hud.config.colors.colorCompass") public int colorCompass = 0xFFFF55;
        @Config.LangKey("f3hud.config.colors.colorRegion") public int colorRegion = 0xFFAA55;
        @Config.LangKey("f3hud.config.colors.colorTargetedBlock") public int colorTargetedBlock = 0xFF55FF;
        @Config.LangKey("f3hud.config.colors.colorEntityStats") public int colorEntityStats = 0x55FFFF;
        @Config.LangKey("f3hud.config.colors.colorEntities") public int colorEntities = 0x55FF55;
        @Config.LangKey("f3hud.config.colors.colorPerformance") public int colorPerformance = 0xFFAA00;
        @Config.LangKey("f3hud.config.colors.colorVersion") public int colorVersion = 0xAAAAAA;
        @Config.LangKey("f3hud.config.colors.colorServer") public int colorServer = 0x5555FF;

        @Config.LangKey("f3hud.config.colors.colorSlimeChunk") public int colorSlimeChunk = 0x77FF77;
        @Config.LangKey("f3hud.config.colors.colorEnd") public int colorEnd = 0x5555FF;
        @Config.LangKey("f3hud.config.colors.colorOxygen") public int colorOxygen = 0x55FFFF;
        @Config.LangKey("f3hud.config.colors.colorDurability") public int colorDurability = 0xFF5555;
        @Config.LangKey("f3hud.config.colors.colorPotion") public int colorPotion = 0xFF55FF;
        @Config.LangKey("f3hud.config.colors.colorBeacon") public int colorBeacon = 0xFFFF55;
        @Config.LangKey("f3hud.config.colors.colorWeather") public int colorWeather = 0x5555FF;
        @Config.LangKey("f3hud.config.colors.colorGrowth") public int colorGrowth = 0x55FF55;
        @Config.LangKey("f3hud.config.colors.colorSpeedometer") public int colorSpeedometer = 0xFFAA00;
        @Config.LangKey("f3hud.config.colors.colorVillagerTrade") public int colorVillagerTrade = 0xFFAA55;
        @Config.LangKey("f3hud.config.colors.colorItemDespawn") public int colorItemDespawn = 0xAAAAAA;
        @Config.LangKey("f3hud.config.colors.colorHealthAndHunger") public int colorHealthAndHunger = 0xFF5555;
        @Config.LangKey("f3hud.config.colors.colorMobAggro") public int colorMobAggro = 0xFF0000;
        @Config.LangKey("f3hud.config.colors.colorMachineProgress") public int colorMachineProgress = 0xAAAAAA;
        @Config.LangKey("f3hud.config.colors.colorExplosion") public int colorExplosion = 0xFF0000;
        @Config.LangKey("f3hud.config.colors.colorStellar") public int colorStellar = 0x5555FF;
    }

    public static class Position {
        public double userScale = 0.8;
        public int leftX = 8;
        public int leftY = 8;
        public int rightX = 8;
        public int rightY = 8;
        public int compassYOffset = 40;
    }

    public static class Animation {
        public boolean enableAnimation = true;
        public double animationSpeed = 0.1;
        public int slideDistance = 50;
        public int backgroundAlpha = 100;
        public boolean showTextBackground = true;
        public int textBackgroundAlpha = 100;
        public int textBackgroundColor = 0x000000;
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