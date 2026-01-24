package com.worador.f3hud;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import java.util.ArrayList;
import java.util.List;

public class VillagerTradeModule extends InfoModule {

    private static final String[] IS_WILLING_FIELDS = new String[] {"isWillingToMate", "field_175565_bs", "bs"};
    private static final String[] CAREER_LEVEL_FIELDS = new String[] {"careerLevel", "field_175562_bv", "bv"};

    private static final int UPDATE_INTERVAL_TICKS = 10;
    private int lastUpdateTick = -1;
    private int lastEntityId = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override
    public String getName() {
        return "Villager Trade";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showVillagerTrade;
    }

    @Override
    public List<InfoLine> getLines() {
        if (!isEnabledInConfig() || mc.player == null) return new ArrayList<>();

        RayTraceResult rt = mc.objectMouseOver;
        if (rt == null || rt.typeOfHit != RayTraceResult.Type.ENTITY || !(rt.entityHit instanceof EntityVillager)) {
            lastEntityId = -1;
            return new ArrayList<>();
        }

        EntityVillager villager = (EntityVillager) rt.entityHit;
        int currentTick = mc.player.ticksExisted;

        if (cachedLines.isEmpty() || (currentTick - lastUpdateTick) >= UPDATE_INTERVAL_TICKS || villager.getEntityId() != lastEntityId) {
            List<InfoLine> newLines = new ArrayList<>();

            try {
                // 1. Kopfzeile: Beruf & Level
                String profName = "Villager";
                try { profName = villager.getProfessionForge().getRegistryName().getResourcePath().toUpperCase(); } catch (Exception ignored) {}
                int level = 0;
                try { level = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, CAREER_LEVEL_FIELDS); } catch (Exception ignored) {}
                newLines.add(new InfoLine("Villager: ", profName + (level > 0 ? " (Lvl " + level + ")" : ""), ModConfig.colors.colorVillagerTrade));

                // 2. Trades auflisten
                MerchantRecipeList recipes = villager.getRecipes(mc.player);
                if (recipes != null && !recipes.isEmpty()) {
                    newLines.add(new InfoLine("§7Trades:", "", 0xFFFFFF));

                    // Wir zeigen maximal die ersten 5 Trades an, damit das HUD nicht den Bildschirm sprengt
                    int count = 0;
                    for (MerchantRecipe recipe : recipes) {
                        if (count >= 5) {
                            newLines.add(new InfoLine(" §8> ", "and more...", 0xAAAAAA));
                            break;
                        }

                        ItemStack buy1 = recipe.getItemToBuy();
                        ItemStack sell = recipe.getItemToSell();

                        String tradeColor = recipe.isRecipeDisabled() ? "§c" : "§a";
                        String buyText = buy1.getCount() + "x " + buy1.getDisplayName();
                        String sellText = sell.getCount() + "x " + sell.getDisplayName();

                        newLines.add(new InfoLine(" " + tradeColor + "-> ", buyText + " §7for " + tradeColor + sellText, 0xFFFFFF));
                        count++;
                    }
                }

                // 3. Willingness
                try {
                    if ((boolean)ReflectionHelper.getPrivateValue(EntityVillager.class, villager, IS_WILLING_FIELDS)) {
                        newLines.add(new InfoLine("Willing: ", "Yes", 0xFF55FF));
                    }
                } catch (Exception ignored) {}

            } catch (Exception e) {
                newLines.add(new InfoLine("Villager: ", "§cError reading trades", 0xFFFFFF));
            }

            cachedLines = newLines;
            lastUpdateTick = currentTick;
            lastEntityId = villager.getEntityId();
        }

        return cachedLines;
    }
}