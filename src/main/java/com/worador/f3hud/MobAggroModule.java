package com.worador.f3hud;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MobAggroModule extends InfoModule {

    @Override
    public String getName() {
        return "Mob Aggro";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showAggroModule;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null) return lines;

        // Erhöhte Reichweite: 35 Meter deckt ca. 2 Chunks ab
        double radius = 35.0;
        AxisAlignedBB area = mc.player.getEntityBoundingBox().grow(radius);
        List<EntityMob> mobs = mc.world.getEntitiesWithinAABB(EntityMob.class, area);

        List<AggroEntry> activeThreats = new ArrayList<>();

        for (EntityMob clientMob : mobs) {
            EntityMob targetMob = clientMob;

            // Singleplayer-Server-Zugriff für echte Aggro-Daten
            if (mc.isSingleplayer() && mc.getIntegratedServer() != null) {
                Entity serverEntity = mc.getIntegratedServer().getWorld(mc.player.dimension).getEntityFromUuid(clientMob.getUniqueID());
                if (serverEntity instanceof EntityMob) {
                    targetMob = (EntityMob) serverEntity;
                }
            }

            Entity target = targetMob.getAttackTarget();
            if (target != null && target.getUniqueID().equals(mc.player.getUniqueID())) {
                activeThreats.add(new AggroEntry(targetMob, mc.player.getDistance(targetMob)));
            }
        }

        // Sortierung: Das Monster, das am nächsten ist, steht ganz oben
        activeThreats.sort(Comparator.comparingDouble(e -> e.distance));

        if (activeThreats.isEmpty()) {
            lines.add(new InfoLine("Status: ", "Safe", 0x55FF55));
        } else {
            // Nur die 5 gefährlichsten Mobs anzeigen (Übersichtlichkeit)
            int count = 0;
            for (AggroEntry entry : activeThreats) {
                if (count >= 5) break;

                int color = getDistanceColor(entry.distance);
                String name = entry.mob.getDisplayName().getUnformattedText();

                // --- NEU: Detaillierte Health-Berechnung & Diagramm ---
                float currentHP = entry.mob.getHealth();
                float maxHP = entry.mob.getMaxHealth();
                float hpPercent = (currentHP / maxHP) * 100;

                // Erstellung des Diagramms [||||||----]
                int barLength = 10;
                int filled = Math.round(hpPercent / 10);
                StringBuilder barBuilder = new StringBuilder("[");
                for (int i = 0; i < barLength; i++) {
                    barBuilder.append(i < filled ? "|" : "-");
                }
                barBuilder.append("]");
                String healthBar = barBuilder.toString();
                // -------------------------------------------------------

                // Creeper-Spezialwarnung (Zündungs-Check)
                String warningPrefix = "";
                if (entry.mob instanceof EntityCreeper) {
                    EntityCreeper creeper = (EntityCreeper) entry.mob;
                    if (creeper.getCreeperState() > 0) { // Creeper zündet!
                        warningPrefix = TextFormatting.RED + "" + TextFormatting.BOLD + "!!! EXPLOSION !!! ";
                        color = 0xFFFFFF;
                    }
                }

                // Distanz-Prefixe
                if (warningPrefix.isEmpty()) {
                    if (entry.distance < 4.0) warningPrefix = TextFormatting.RED + "" + TextFormatting.BOLD + "(!) ";
                    else if (entry.distance < 10.0) warningPrefix = TextFormatting.GOLD + "> ";
                }

                // Anzeige: Name, Diagramm, Prozent und Absolute Zahlen
                String infoText = String.format("%s %s %.0f%% (%.1f/%.1f) %.1fm",
                        name, healthBar, hpPercent, currentHP, maxHP, entry.distance);

                lines.add(new InfoLine(
                        warningPrefix + "AGGRO",
                        infoText,
                        color
                ));
                count++;
            }

            // Info, wenn mehr Mobs da sind
            if (activeThreats.size() > 5) {
                lines.add(new InfoLine("Threats: ", "+" + (activeThreats.size() - 5) + " more Mobs...", 0xAAAAAA));
            }
        }

        return lines;
    }

    private int getDistanceColor(double dist) {
        if (dist < 5.0) return 0xFF5555;   // Hellrot (Nahkampf)
        if (dist < 15.0) return 0xFFAA00;  // Orange (Fernkampf-Gefahr)
        if (dist < 25.0) return 0xFFFF55;  // Gelb (In Sichtweite)
        return 0xAAAAAA;                  // Grau (Außerhalb direkter Gefahr)
    }

    private static class AggroEntry {
        final EntityMob mob;
        final double distance;
        AggroEntry(EntityMob m, double d) { this.mob = m; this.distance = d; }
    }
}