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

    // Performance Throttling: Update once per second (20 ticks) using tick-based caching
    private static final int UPDATE_INTERVAL_TICKS = 20;
    private int lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

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
        // Strict Gating: Early exit if disabled
        if (!isEnabledInConfig() || mc.player == null || mc.world == null) {
            return new ArrayList<>();
        }

        int currentTick = mc.player.ticksExisted;
        
        // Only update if enough ticks have passed or if it's the first call
        if (cachedLines.isEmpty() || (currentTick - lastUpdateTick) >= UPDATE_INTERVAL_TICKS) {
            cachedLines = new ArrayList<>();
            
            // Expensive operations inside the refresh block
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                if (mc.objectMouseOver.entityHit instanceof EntityVillager) {
                    EntityVillager villager = (EntityVillager) mc.objectMouseOver.entityHit;

                    // 1. Profession & Level (Wichtig für die Übersicht)
                    String profName = villager.getProfessionForge().getRegistryName().getResourcePath();
                    // careerLevel zeigt an, wie weit der Villager freigeschaltet ist
                    int level = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, "careerLevel", "field_175562_bv");
                    cachedLines.add(new InfoLine("Villager: ", profName + " (Lvl " + level + ")", 0xAAAAAA));

                    // 2. Handels-Logik
                    MerchantRecipeList recipes = villager.getRecipes(mc.player);
                    if (recipes != null) {
                        int lockedCount = 0;
                        for (MerchantRecipe recipe : recipes) {
                            if (recipe.isRecipeDisabled()) lockedCount++;
                        }

                        if (lockedCount > 0) {
                            int color = (lockedCount == recipes.size()) ? 0xFF5555 : 0xFFFF55;
                            cachedLines.add(new InfoLine("Locked: ", lockedCount + "/" + recipes.size(), color));
                        } else {
                            cachedLines.add(new InfoLine("Trades: ", "Ready", 0x55FF55));
                        }
                    }

                    // 3. Willingness (Bereitschaft zur Paarung/Zucht)
                    try {
                        boolean isWilling = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, IS_WILLING_FIELDS);
                        if (isWilling) {
                            cachedLines.add(new InfoLine("Willing: ", "Yes", 0xFF55FF));
                        }
                    } catch (Exception ignored) {}
                }
            }
            
            lastUpdateTick = currentTick;
        }
        
        return cachedLines;
    }
}
