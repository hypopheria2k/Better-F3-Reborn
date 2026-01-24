package com.worador.f3hud;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EntityStatsModule extends InfoModule {

    @Override
    public String getName() { return "EntityStats"; }

    @Override
    protected boolean isEnabledInConfig() { return ModConfig.modules.showEntityStats; }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (!isEnabledInConfig() || mc.player == null) return lines;

        // 1. Priorität: Reittier | 2. Priorität: Angeschautes Ziel
        Entity target = (mc.player.getRidingEntity() != null) ? mc.player.getRidingEntity() : getTargetEntity();

        if (target instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) target;
            String type = target.getName();

            // HP Anzeige: Klar und ohne Balken-Müll
            float hp = living.getHealth();
            float max = living.getMaxHealth();
            int hColor = (hp <= max * 0.25) ? 0xFF5555 : 0x55FF55;
            lines.add(new InfoLine(type + " HP: ", String.format(Locale.US, "%.1f/%.1f", hp, max), hColor));

            // Pferde-Spezial-Stats (Speed & Jump)
            if (target instanceof AbstractHorse) {
                AbstractHorse horse = (AbstractHorse) target;

                // Speed in m/s (Standard Minecraft Faktor 43.17)
                double speed = horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 43.17;
                int sColor = (speed > 13.0) ? 0xFFAA00 : 0xFFFFFF; // Gold für Top-Pferde
                lines.add(new InfoLine("Speed: ", String.format(Locale.US, "%.2f m/s", speed), sColor));

                // Sprunghöhe (Vereinfacht für Performance)
                double jumpStr = horse.getHorseJumpStrength();
                double jumpHeight = 5.275 * Math.pow(jumpStr, 2); // Sehr genaue Annäherung für 1.12.2
                int jColor = (jumpHeight > 4.5) ? 0xFFAA00 : 0xFFFFFF;
                lines.add(new InfoLine("Jump: ", String.format(Locale.US, "%.1f Blocks", jumpHeight), jColor));

                // Status (Zähmung)
                if (!horse.isTame()) {
                    lines.add(new InfoLine("Status: ", "WILD", 0xFF5555));
                }
            }
        }
        return lines;
    }

    private Entity getTargetEntity() {
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
            return mc.objectMouseOver.entityHit;
        }
        return null;
    }
}