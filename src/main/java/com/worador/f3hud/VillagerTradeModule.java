package com.worador.f3hud;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import java.util.ArrayList;
import java.util.List;

public class VillagerTradeModule extends InfoModule {

    // Reflection für Willingness (ist in EntityVillager protected)
    private static final String[] IS_WILLING_FIELDS = new String[] {"isWillingToMate", "field_175565_bs"};

    @Override
    public String getName() {
        return "Villager Status";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showVillagerStatus;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null) return lines;

        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
            if (mc.objectMouseOver.entityHit instanceof EntityVillager) {
                EntityVillager villager = (EntityVillager) mc.objectMouseOver.entityHit;

                // 1. Profession & Level (Wichtig für die Übersicht)
                String profName = villager.getProfessionForge().getRegistryName().getResourcePath();
                // careerLevel zeigt an, wie weit der Villager freigeschaltet ist
                int level = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, "careerLevel", "field_175562_bv");
                lines.add(new InfoLine("Villager: ", profName + " (Lvl " + level + ")", 0xAAAAAA));

                // 2. Handels-Logik
                MerchantRecipeList recipes = villager.getRecipes(mc.player);
                if (recipes != null) {
                    int lockedCount = 0;
                    for (MerchantRecipe recipe : recipes) {
                        if (recipe.isRecipeDisabled()) lockedCount++;
                    }

                    if (lockedCount > 0) {
                        int color = (lockedCount == recipes.size()) ? 0xFF5555 : 0xFFFF55;
                        lines.add(new InfoLine("Locked: ", lockedCount + "/" + recipes.size(), color));
                    } else {
                        lines.add(new InfoLine("Trades: ", "Ready", 0x55FF55));
                    }
                }

                // 3. Willingness (Bereitschaft zur Paarung/Zucht)
                try {
                    boolean isWilling = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, IS_WILLING_FIELDS);
                    if (isWilling) {
                        lines.add(new InfoLine("Willing: ", "Yes", 0xFF55FF));
                    }
                } catch (Exception ignored) {}
            }
        }

        return lines;
    }
}