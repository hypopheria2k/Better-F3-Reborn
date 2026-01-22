package com.worador.f3hud;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import java.util.ArrayList;
import java.util.List;

public class HealthAndHungerModule extends InfoModule {

    private static final String[] FOOD_TIMER = new String[]{"foodTimer", "field_75123_d"};

    @Override
    public String getName() {
        return "Health & Hunger";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showHealthStats;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null) return lines;

        EntityPlayer player = mc.player;
        FoodStats stats = player.getFoodStats();

        if (mc.isSingleplayer() && mc.getIntegratedServer() != null) {
            EntityPlayer serverPlayer = mc.getIntegratedServer().getWorld(player.dimension).getPlayerEntityByUUID(player.getUniqueID());
            if (serverPlayer != null) stats = serverPlayer.getFoodStats();
        }

        // --- 1. Aufgewertete Armor & Toughness Anzeige ---
        int armorValue = player.getTotalArmorValue();
        // Toughness ist in 1.12.2 entscheidend für den Schutz gegen hohen Schaden (z.B. Creeper)
        double toughness = player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue();
        double armorPercent = armorValue * 4.0;

        int armorColor = 0x55FFFF;
        if (armorValue >= 20) armorColor = 0xFFAA00;
        else if (armorValue < 10) armorColor = 0xAAAAAA;

        String armorInfo = String.format("%d/20 %s (%.0f%%)", armorValue, getArmorBar(armorValue), armorPercent);
        if (toughness > 0) {
            armorInfo += TextFormatting.BLUE + String.format(" +%.0fT", toughness);
        }
        lines.add(new InfoLine("Armor: ", armorInfo, armorColor));

        // --- 2. Health & Absorption ---
        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        float absorption = player.getAbsorptionAmount();

        int hColor = (health <= 6.0f) ? 0xFF5555 : (health < maxHealth ? 0xFFFF55 : 0x55FF55);
        String healthText = String.format("%.1f/%.1f", health, maxHealth);

        // Gelbe Herzen separat anzeigen, falls vorhanden
        if (absorption > 0) {
            healthText += TextFormatting.GOLD + String.format(" (+%.1f)", absorption);
        }
        if (health < maxHealth) {
            healthText += TextFormatting.GRAY + String.format(" (-%.1f)", maxHealth - health);
        }
        lines.add(new InfoLine("HP: ", healthText, hColor));

        // --- 3. Hunger & Saturation ---
        int food = stats.getFoodLevel();
        float saturation = stats.getSaturationLevel();
        lines.add(new InfoLine("Food: ", food + "/20 " + getSaturationBar(saturation), 0xFFAA00));

        // --- 4. High-Precision Regeneration (Bestehende Logik) ---
        if (health < maxHealth) {
            if (food >= 18) {
                try {
                    int timer = ReflectionHelper.getPrivateValue(FoodStats.class, stats, FOOD_TIMER);
                    boolean isFast = (food == 20 && saturation > 0);
                    int maxTicks = isFast ? 10 : 80;
                    int percent = (int) (((float)timer / maxTicks) * 100);

                    String mode = isFast ? TextFormatting.LIGHT_PURPLE + "FAST" : TextFormatting.AQUA + "NORM";
                    float remainingSec = ((maxHealth - health) * (maxTicks / 20.0f)) - (timer / 20.0f);

                    lines.add(new InfoLine("Heal: ", getProgressBar(percent) + " " + mode, 0x55FF55));
                    lines.add(new InfoLine("Time: ", String.format("ETA: %.1fs", Math.max(0, remainingSec)), 0xAAAAAA));
                } catch (Exception e) {
                    lines.add(new InfoLine("Heal: ", "Syncing...", 0xAAAAAA));
                }
            } else {
                lines.add(new InfoLine("Heal: ", TextFormatting.RED + "STALLED", 0xFF5555));
            }
        }

        return lines;
    }

    private String getArmorBar(int armor) {
        int filled = armor / 2;
        StringBuilder sb = new StringBuilder(TextFormatting.GRAY + "[");
        for(int i=0; i<10; i++) {
            if (i < filled) sb.append(TextFormatting.WHITE + "■");
            else sb.append(TextFormatting.DARK_GRAY + "□");
        }
        return sb.append(TextFormatting.GRAY + "]").toString();
    }

    private String getProgressBar(int percent) {
        int filled = Math.min(10, percent / 10);
        return "[" + "||||||||||".substring(0, filled) + "----------".substring(filled) + "]";
    }

    private String getSaturationBar(float sat) {
        int iconCount = (int) (sat / 2);
        StringBuilder sb = new StringBuilder(TextFormatting.GOLD + "(");
        for(int i=0; i<10; i++) sb.append(i < iconCount ? "●" : "○");
        return sb.append(")").toString();
    }
}