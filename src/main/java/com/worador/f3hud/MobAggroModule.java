package com.worador.f3hud;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MobAggroModule extends InfoModule {

    private long lastUpdateTick = -1;
    private List<InfoLine> cachedLines = new ArrayList<>();

    @Override
    public String getName() { return "Mob Aggro"; }

    @Override
    protected boolean isEnabledInConfig() { return ModConfig.modules.showMobAggro; }

    @Override
    public List<InfoLine> getLines() {
        if (!isEnabledInConfig() || mc.player == null || mc.world == null) return new ArrayList<>();

        if (mc.player.ticksExisted - lastUpdateTick < 5 && !cachedLines.isEmpty()) {
            return cachedLines;
        }

        List<InfoLine> lines = new ArrayList<>();
        double radius = 16.0; // Radius verringert für weniger "Noise"
        AxisAlignedBB area = mc.player.getEntityBoundingBox().grow(radius);

        List<EntityMob> mobs = mc.world.getEntitiesWithinAABB(EntityMob.class, area);
        List<AggroEntry> activeThreats = new ArrayList<>();

        for (EntityMob mob : mobs) {
            double dist = mc.player.getDistance(mob);
            // Prüfung: Targetet mich oder ist nah dran & sichtbar
            boolean isTargetingMe = mob.getAttackTarget() != null && mob.getAttackTarget().getUniqueID().equals(mc.player.getUniqueID());

            if (isTargetingMe || (dist < 8.0 && mc.player.canEntityBeSeen(mob))) {
                activeThreats.add(new AggroEntry(mob, dist));
            }
        }

        activeThreats.sort(Comparator.comparingDouble(e -> e.distance));

        if (activeThreats.isEmpty()) {
            lines.add(new InfoLine("Threats: ", "None", 0x55FF55));
        } else {
            // Auf 2 Mobs begrenzt, um Platz zu sparen
            for (int i = 0; i < Math.min(activeThreats.size(), 2); i++) {
                AggroEntry entry = activeThreats.get(i);
                int color = getDistanceColor(entry.distance);
                String name = entry.mob.getDisplayName().getUnformattedText();

                // KÜRZUNG: Nur HP oder % anzeigen, nicht beides
                String health = String.format("%.0fH", entry.mob.getHealth());

                String prefix = "";
                if (entry.mob instanceof EntityCreeper && ((EntityCreeper) entry.mob).getCreeperState() > 0) {
                    prefix = TextFormatting.RED + "!!! "; // Stark gekürzt
                    color = 0xFF5555;
                } else if (entry.distance < 6.0) {
                    prefix = TextFormatting.GOLD + "! ";
                }

                // Kompakt: ! Creeper: 20H (4.2m)
                lines.add(new InfoLine(
                        prefix + name + ": ",
                        String.format(Locale.US, "%s (%.1fm)", health, entry.distance),
                        color
                ));
            }

            if (activeThreats.size() > 2) {
                lines.add(new InfoLine("More: ", "+" + (activeThreats.size() - 2) + " mobs", 0xAAAAAA));
            }
        }

        this.cachedLines = lines;
        this.lastUpdateTick = mc.player.ticksExisted;
        return lines;
    }

    private int getDistanceColor(double dist) {
        if (dist < 5.0) return 0xFF5555;
        if (dist < 10.0) return 0xFFAA00;
        return 0xFFFF55;
    }

    private static class AggroEntry {
        final EntityMob mob;
        final double distance;
        AggroEntry(EntityMob m, double d) { this.mob = m; this.distance = d; }
    }
}