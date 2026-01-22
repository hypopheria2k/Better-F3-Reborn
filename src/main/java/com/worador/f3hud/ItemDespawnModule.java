package com.worador.f3hud;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import java.util.ArrayList;
import java.util.List;

public class ItemDespawnModule extends InfoModule {

    private static final String[] AGE_FIELDS = new String[] {"age", "field_70292_b"};

    @Override
    public String getName() {
        return "Item Despawn";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showItemDespawn;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null) return lines;

        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
            if (mc.objectMouseOver.entityHit instanceof EntityItem) {
                EntityItem entityItem = (EntityItem) mc.objectMouseOver.entityHit;
                ItemStack stack = entityItem.getItem();

                // 1. Name und Menge
                String displayName = stack.getDisplayName();
                if (stack.getCount() > 1) {
                    displayName += " x" + stack.getCount();
                }
                lines.add(new InfoLine("Item: ", displayName, 0xFFFFFF));

                // 2. NEU: Haltbarkeits-Check für Werkzeuge am Boden
                if (stack.isItemStackDamageable()) {
                    int maxDamage = stack.getMaxDamage();
                    int currentDamage = maxDamage - stack.getItemDamage();
                    int duraColor = (currentDamage * 100 / maxDamage < 20) ? 0xFF5555 : 0xAAAAAA;
                    lines.add(new InfoLine("Durability: ", currentDamage + "/" + maxDamage, duraColor));
                }

                try {
                    int age = ReflectionHelper.getPrivateValue(EntityItem.class, entityItem, AGE_FIELDS);
                    int remainingTicks = 6000 - age;

                    if (remainingTicks > 0) {
                        float percent = (remainingTicks / 6000.0f) * 100;
                        int color = 0x55FF55;

                        // Warnstufen
                        if (remainingTicks < 1200) color = 0xFFAA00; // 1 Min
                        if (remainingTicks < 400) color = 0xFF5555;  // 20 Sek

                        // 3. NEU: Visueller Zeitbalken [|||||-----]
                        String timeBar = getProgressBar(percent);
                        lines.add(new InfoLine("Life: ", timeBar + String.format(" %.0f%%", percent), color));

                        // 4. Formatierte Zeit (bestehend)
                        String prefix = (remainingTicks < 400) ? TextFormatting.BOLD.toString() : "";
                        lines.add(new InfoLine("Despawn: ", prefix + formatTime(remainingTicks), color));

                    } else {
                        lines.add(new InfoLine("Status: ", "DESPAWNING NOW", 0xFF5555));
                    }
                } catch (Exception e) {
                    lines.add(new InfoLine("Despawn: ", "Unknown (Server Mappings?)", 0xAAAAAA));
                }
            }
        }

        return lines;
    }

    /**
     * Erstellt einen Fortschrittsbalken für die verbleibende Zeit
     */
    private String getProgressBar(float percent) {
        int barLength = 10;
        int filled = Math.round(percent / 10);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            sb.append(i < filled ? "|" : ".");
        }
        sb.append("]");
        return sb.toString();
    }

    private String formatTime(int ticks) {
        int totalSeconds = Math.max(0, ticks) / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02ds", minutes, seconds);
    }
}