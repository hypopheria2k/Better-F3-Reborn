package com.worador.f3hud;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.math.RayTraceResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EntityStatsModule extends InfoModule {

    @Override
    public String getName() {
        return "EntityStats";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showEntityStats;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null) return lines;

        Entity target = getTargetEntity();
        if (!(target instanceof AbstractHorse)) return lines;

        AbstractHorse horse = (AbstractHorse) target;

        // 1. Basis-Info
        lines.add(new InfoLine("Mount: ", getHorseTypeName(horse), 0xAAAAAA));

        // 2. Health mit Realtime-Bar
        float hp = horse.getHealth();
        float maxHp = horse.getMaxHealth();
        lines.add(new InfoLine("Health: ", String.format(Locale.US, "%.1f/%.1f %s", hp, maxHp, getProgressBar(hp/maxHp * 100)), 0xFF5555));

        // 3. Speed-Analyse (Vanilla Max ist ca. 14.5 m/s)
        IAttributeInstance speedAttr = horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        double speedmS = (speedAttr != null ? speedAttr.getAttributeValue() : 0.0) * 43.17; // Korrigierter Faktor für 1.12.2
        int speedColor = 0x55FFFF;
        String speedRating = "";

        if (speedmS > 13.0) { speedColor = 0xFFAA00; speedRating = " [TOP]"; }
        else if (speedmS > 10.0) { speedColor = 0x55FF55; }

        lines.add(new InfoLine("Speed: ", String.format(Locale.US, "%.2f m/s%s", speedmS, speedRating), speedColor));

        // 4. Jump-Analyse (Vanilla Max ist ca. 5.5 Blöcke)
        double jumpStr = horse.getHorseJumpStrength();
        // Deine bewährte Formel
        double jumpHeight = -0.181758 * Math.pow(jumpStr, 3) + 3.68971 * Math.pow(jumpStr, 2) + 2.12859 * jumpStr - 0.34393;
        int jumpColor = 0xFFFF55;
        if (jumpHeight > 4.5) jumpColor = 0xFFAA00;

        lines.add(new InfoLine("Jump: ", String.format(Locale.US, "%.2f Blocks", Math.max(0, jumpHeight)), jumpColor));

        // 5. Zucht-Status (Wichtiges Zusatz-Feature)
        if (horse instanceof EntityHorse || horse instanceof EntityDonkey || horse instanceof EntityMule) {
            boolean isReproducing = horse.isInLove();
            boolean isTame = horse.isTame();
            String status = isTame ? (isReproducing ? "Ready to Breed" : "Tame") : TextFormatting.RED + "Wild";
            lines.add(new InfoLine("Status: ", status, isTame ? 0x55FF55 : 0xFF5555));
        }

        return lines;
    }

    private String getProgressBar(float percent) {
        int barLength = 5;
        int filled = Math.round(percent / 20);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            sb.append(i < filled ? "|" : ".");
        }
        sb.append("]");
        return sb.toString();
    }

    private Entity getTargetEntity() {
        if (mc.player.getRidingEntity() != null) return mc.player.getRidingEntity();
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) return mc.objectMouseOver.entityHit;
        return null;
    }

    private String getHorseTypeName(AbstractHorse horse) {
        if (horse instanceof EntityHorse) return "Horse";
        if (horse instanceof EntityDonkey) return "Donkey";
        if (horse instanceof EntityMule) return "Mule";
        if (horse instanceof EntityLlama) return "Llama";
        if (horse instanceof EntitySkeletonHorse) return "Skeleton Horse";
        if (horse instanceof EntityZombieHorse) return "Zombie Horse";
        return "Equine";
    }
}