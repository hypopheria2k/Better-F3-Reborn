package com.worador.f3hud.compat;

import com.worador.f3hud.InfoModule.InfoLine;
import com.worador.f3hud.ModConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.math.RayTraceResult;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.core.data.SoulNetwork;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BloodMagicCompat {

    public static List<InfoLine> getBloodMagicLines(EntityPlayer player, World world) {
        List<InfoLine> lines = new ArrayList<>();
        if (player == null || world == null) return lines;

        // 1. Soul Network (LP / Essence)
        try {
            // Wir nutzen den NetworkHelper, den du gefunden hast
            SoulNetwork network = NetworkHelper.getSoulNetwork(player);

            if (network != null) {
                int lp = 0;
                boolean found = false;

                // Laut deinem Code-Fund heißt es hier "getCurrentEssence"
                try {
                    lp = network.getCurrentEssence();
                    found = true;
                } catch (NoSuchMethodError | Exception e) {
                    // Fallback auf Reflection, falls der Compiler die Methode oben nicht sieht
                    try {
                        Method m = network.getClass().getMethod("getCurrentEssence");
                        lp = (int) m.invoke(network);
                        found = true;
                    } catch (Exception e2) {
                        // Letzter Versuch: getCurrentLP (alte API-Versionen)
                        try {
                            Method m = network.getClass().getMethod("getCurrentLP");
                            lp = (int) m.invoke(network);
                            found = true;
                        } catch (Exception ignored) {}
                    }
                }

                if (found) {
                    // Logik für maximale LP (Kapazität) hinzufügen
                    int maxLp = 0;
                    try {
                        // Nutzt die Methode aus dem NetworkHelper-Fund, um die Kapazität des Orbs zu ermitteln
                        maxLp = NetworkHelper.getMaximumForTier(network.getOrbTier());
                    } catch (Exception ignored) {}

                    String displayValue;
                    if (maxLp > 0) {
                        displayValue = String.format("%,d / %,d LP", lp, maxLp);
                    } else {
                        displayValue = String.format("%,d LP", lp);
                    }

                    // Wir nehmen die Farbe aus deiner Config
                    lines.add(new InfoLine("Soul Network: ", displayValue, ModConfig.colors.colorBloodMagic));
                }
            }
        } catch (Exception e) {
            // Keine Fehlermeldung, um das HUD sauber zu halten
        }

        // 2. Altar Abfrage (Raytrace)
        try {
            RayTraceResult rtr = player.rayTrace(5.0D, 1.0F);
            if (rtr != null && rtr.typeOfHit == RayTraceResult.Type.BLOCK) {
                TileEntity te = world.getTileEntity(rtr.getBlockPos());
                if (te instanceof TileAltar) {
                    TileAltar altar = (TileAltar) te;

                    int blood = altar.getCurrentBlood();
                    int capacity = altar.getCapacity();
                    int tier = altar.getTier().ordinal() + 1;

                    lines.add(new InfoLine("Blood Altar: ", "Tier " + tier, ModConfig.colors.colorBloodMagic));

                    if (capacity > 0) {
                        double pct = (double) blood / capacity * 100.0;
                        lines.add(new InfoLine("Altar Fill: ", String.format("%d / %d (%.1f%%)", blood, capacity, pct), ModConfig.colors.colorBloodMagic));
                    }
                }
            }
        } catch (Exception e) { }

        return lines;
    }
}