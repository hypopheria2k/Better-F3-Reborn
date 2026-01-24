package com.worador.f3hud;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import java.util.ArrayList;
import java.util.List;

public class ExplosionModule extends InfoModule {

    private static final String[] CREEPER_TIME = new String[] {"timeSinceIgnited", "field_70833_d"};
    private static final String[] CREEPER_MAX = new String[] {"fuseTime", "field_70834_e"};

    @Override
    public String getName() {
        return "Explosion Timer";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showExplosion;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null) return lines;

        // Radius von 10 Blöcken für mehr Sicherheit (Beruf/Familie-Sicherer-Modus)
        double radius = 10.0;
        AxisAlignedBB area = mc.player.getEntityBoundingBox().grow(radius);
        List<Entity> nearbyEntities = mc.world.getEntitiesWithinAABB(Entity.class, area);

        for (Entity entity : nearbyEntities) {
            // 1. TNT Primed
            if (entity instanceof EntityTNTPrimed) {
                EntityTNTPrimed tnt = (EntityTNTPrimed) entity;
                int fuse = tnt.getFuse();
                double dist = mc.player.getDistance(tnt);

                // Farbe: Rot unter 1 Sekunde (20 Ticks)
                int color = (fuse < 20) ? 0xFF5555 : 0xFFFF55;
                lines.add(new InfoLine("TNT Detonation: ", formatTime(fuse) + String.format(" (%.1fm)", dist), color));
            }

            // 2. Creeper (nur wenn er im Zündvorgang ist)
            if (entity instanceof EntityCreeper) {
                EntityCreeper creeper = (EntityCreeper) entity;
                // getCreeperState > 0 bedeutet, der Creeper zischt/zündet
                if (creeper.getCreeperState() > 0) {
                    try {
                        int current = ReflectionHelper.getPrivateValue(EntityCreeper.class, creeper, CREEPER_TIME);
                        int max = ReflectionHelper.getPrivateValue(EntityCreeper.class, creeper, CREEPER_MAX);
                        int remaining = max - current;
                        double dist = mc.player.getDistance(creeper);

                        // Fette rote Warnung für Creeper
                        lines.add(new InfoLine("CREEPER FUSE: ", TextFormatting.BOLD + formatTime(remaining) + String.format(" (%.1fm)", dist), 0xFF5555));
                    } catch (Exception ignored) {
                        // Falls Reflection fehlschlägt, zumindest eine Warnung ohne Zeit
                        lines.add(new InfoLine("CREEPER FUSE: ", "!!! DANGER !!!", 0xFF5555));
                    }
                }
            }
        }
        return lines;
    }

    private String formatTime(int ticks) {
        double seconds = Math.max(0, ticks) / 20.0;
        return String.format("%.2fs", seconds);
    }
}