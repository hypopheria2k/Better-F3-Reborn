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

        // LINKER BEREICH
        LEFT.add(new CoordinatesModule());
        LEFT.add(new RotationModule());
        LEFT.add(new WorldModule());
        LEFT.add(new DimensionModule());
        LEFT.add(new RegionModule());
        LEFT.add(new TargetedBlockModule());
        LEFT.add(new EntitiesModule());
        LEFT.add(new PerformanceModule());
        LEFT.add(new CompassModule());

        // RECHTER BEREICH
        RIGHT.add(new SystemModule());

        if (Loader.isModLoaded("astralsorcery")) RIGHT.add(new AstralModule());
        if (Loader.isModLoaded("stellar_core")) RIGHT.add(new StellarModule());
        if (Loader.isModLoaded("botania"))      RIGHT.add(new BotaniaModule());
        if (Loader.isModLoaded("bloodmagic"))   RIGHT.add(new BloodMagicModule());
        if (Loader.isModLoaded("thaumcraft"))   RIGHT.add(new ThaumcraftModule());
    }

    public static InfoModule getCompassModule() {
        for (InfoModule m : LEFT) {
            if (m instanceof CompassModule) return m;
        }
        return null;
    }

    public static InfoModule getGraphModule() {
        for (InfoModule m : LEFT) {
            if (m instanceof PerformanceGraphModule) return m;
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