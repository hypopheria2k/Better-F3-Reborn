package com.worador.f3hud;

import net.minecraftforge.fml.common.Loader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleRegistry {

    private static final List<InfoModule> LEFT = new ArrayList<>();
    private static final List<InfoModule> RIGHT = new ArrayList<>();

    public static void init() {
        LEFT.clear();
        RIGHT.clear();

        // --- LINKER BEREICH (Navigation & Welt-Basis) ---
        LEFT.clear();
        LEFT.add(new CoordinatesModule());
        LEFT.add(new SlimeChunkModule());    // Direkt unter Chunk-Daten (Feedback umgesetzt)
        LEFT.add(new ChunkPosModule());
        LEFT.add(new LightLevelModule());
        LEFT.add(new RotationModule());
        LEFT.add(new WorldModule());
        LEFT.add(new DimensionModule());
        LEFT.add(new TravelModule());
        LEFT.add(new RegionModule());
        LEFT.add(new TargetedBlockModule());
        LEFT.add(new EntitiesModule());
        LEFT.add(new EntityStatsModule());  // Hierher verschoben, passt zu Entities
        LEFT.add(new CompassModule());
        LEFT.add(new PerformanceModule());
        LEFT.add(new EndModule());

        // --- RECHTER BEREICH (Spieler-Status & Mod-Interaktion) ---
        RIGHT.clear();
        RIGHT.add(new SystemModule());
        RIGHT.add(new VersionModule());     // Technische Info als Header rechts

        // Mod-Support Bereich
        if (Loader.isModLoaded("astralsorcery")) RIGHT.add(new AstralModule());
        if (Loader.isModLoaded("stellar_core"))  RIGHT.add(new StellarModule());
        if (Loader.isModLoaded("botania"))       RIGHT.add(new BotaniaModule());
        if (Loader.isModLoaded("bloodmagic"))    RIGHT.add(new BloodMagicModule());
        if (Loader.isModLoaded("thaumcraft"))    RIGHT.add(new ThaumcraftModule());

        // Vital-Werte & Status (Kritische Infos)
        RIGHT.add(new HealthAndHungerModule()); // Höher priorisiert für bessere Sichtbarkeit
        RIGHT.add(new OxygenModule());
        RIGHT.add(new PotionModule());
        RIGHT.add(new DurabilityModule());
        RIGHT.add(new SpeedometerModule());

        // Welt-Interaktion & Automatisierung
        RIGHT.add(new WeatherModule());
        RIGHT.add(new GrowthModule());
        RIGHT.add(new VillagerTradeModule());
        RIGHT.add(new ItemDespawnModule());
        RIGHT.add(new MachineProgressModule());
        RIGHT.add(new MobAggroModule());
        RIGHT.add(new BeaconModule());
    }

    public static InfoModule getCompassModule() {
        for (InfoModule m : LEFT) {
            if (m instanceof CompassModule) return m;
        }
        return null;
    }

    public static InfoModule getGraphModule() {
        for (InfoModule m : LEFT) {
            // Geändert von PerformanceGraphModule auf PerformanceModule
            if (m instanceof PerformanceModule) return m;
        }
        return null;
    }

    public static List<InfoModule> getLeftModules() {
        return Collections.unmodifiableList(LEFT);
    }

    public static List<InfoModule> getRightModules() {
        return Collections.unmodifiableList(RIGHT);
    }
}
