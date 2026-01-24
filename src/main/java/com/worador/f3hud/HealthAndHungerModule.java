package com.worador.f3hud;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        // 1. Rüstung & Toughness (Kompakt in einer Zeile)
        int armorValue = player.getTotalArmorValue();
        double toughness = player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue();

        String armorText = String.valueOf(armorValue);
        if (toughness > 0) armorText += " (+" + (int)toughness + "T)";
        lines.add(new InfoLine("Armor: ", armorText, 0x55FFFF));

        // 2. Gesundheit & Absorption
        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        float absorption = player.getAbsorptionAmount();

        int hColor = (health <= 6.0f) ? 0xFF5555 : (health < maxHealth ? 0xFFFF55 : 0x55FF55);
        String healthText = String.format(Locale.US, "%.1f/%.1f", health, maxHealth);
        if (absorption > 0) healthText += TextFormatting.GOLD + String.format(" (+%.1f)", absorption);

        lines.add(new InfoLine("HP: ", healthText, hColor));

        // 3. Hunger & Sättigung
        int food = stats.getFoodLevel();
        float saturation = stats.getSaturationLevel();
        lines.add(new InfoLine("Food: ", String.format(Locale.US, "%d/20 (Sat: %.1f)", food, saturation), 0xFFAA00));

        // 4. Regeneration (ETA)
        if (health < maxHealth && food >= 18) {
            try {
                int timer = ReflectionHelper.getPrivateValue(FoodStats.class, stats, FOOD_TIMER);
                boolean isFast = (food == 20 && saturation > 0);
                int maxTicks = isFast ? 10 : 80;

                // Wir berechnen die Sekunden bis zum nächsten halben Herz
                float nextHeartIn = (maxTicks - timer) / 20.0f;
                String healMode = isFast ? "Fast" : "Normal";

                lines.add(new InfoLine("Heal: ", String.format(Locale.US, "%s (Next in %.1fs)", healMode, nextHeartIn), 0x55FF55));
            } catch (Exception e) {
                // Keine Anzeige bei Reflection-Fehler
            }
        } else if (health < maxHealth && food < 18) {
            lines.add(new InfoLine("Heal: ", "Stalled (Hungry)", 0xFF5555));
        }

        return lines;
    }
}