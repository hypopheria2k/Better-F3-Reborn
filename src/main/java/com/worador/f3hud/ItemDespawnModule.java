package com.worador.f3hud;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import java.util.ArrayList;
import java.util.List;

public class ItemDespawnModule extends InfoModule {

    // SRG-Namen für 1.12.2 Obfuskation
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
        if (!isEnabledInConfig() || mc.player == null || mc.world == null) return lines;

        // Nur prüfen, wenn der Spieler direkt auf ein Entity schaut (spart CPU)
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
            if (mc.objectMouseOver.entityHit instanceof EntityItem) {
                EntityItem entityItem = (EntityItem) mc.objectMouseOver.entityHit;
                ItemStack stack = entityItem.getItem();

                // 1. Name & Menge
                String displayName = stack.getDisplayName();
                if (stack.getCount() > 1) displayName += " x" + stack.getCount();
                lines.add(new InfoLine("Item: ", displayName, 0xFFFFFF));

                try {
                    // Reflection-Zugriff
                    int age = ReflectionHelper.getPrivateValue(EntityItem.class, entityItem, AGE_FIELDS);
                    int remainingTicks = 6000 - age; // 5 Minuten Standard

                    if (remainingTicks > 0) {
                        int color = 0x55FF55; // Grün Standard
                        if (remainingTicks < 1200) color = 0xFFAA00; // Gelb < 1 Min
                        if (remainingTicks < 400)  color = 0xFF5555; // Rot < 20 Sek

                        lines.add(new InfoLine("Despawn in: ", formatTime(remainingTicks), color));
                    }
                } catch (Exception e) {
                    lines.add(new InfoLine("Despawn: ", "Unknown", 0xAAAAAA));
                }
            }
        }
        return lines;
    }

    private String formatTime(int ticks) {
        int seconds = ticks / 20;
        int m = seconds / 60;
        int s = seconds % 60;
        return String.format("%d:%02d min", m, s);
    }
}