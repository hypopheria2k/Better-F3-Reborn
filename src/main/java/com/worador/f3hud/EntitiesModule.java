package com.worador.f3hud;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import java.util.ArrayList;
import java.util.List;

public class EntitiesModule extends InfoModule {

    @Override
    public String getName() {
        return "Entities";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showEntities;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.world == null) return lines;

        // 1. Render-Statistiken (Wie viele siehst du wirklich)
        String debugEntities = mc.renderGlobal.getDebugInfoEntities();
        lines.add(new InfoLine("Rendered: ", debugEntities, 0xAAAAAA));

        int monsters = 0, animals = 0, water = 0, ambient = 0, items = 0, xp = 0, others = 0;

        // Effizientes Durchlaufen der geladenen Entities
        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof IMob) monsters++;
            else if (e instanceof EntityAnimal) animals++;
            else if (e instanceof EntityWaterMob) water++;
            else if (e instanceof EntityAmbientCreature) ambient++;
            else if (e instanceof EntityItem) items++;
            else if (e instanceof EntityXPOrb) xp++;
            else others++;
        }

        // 2. Mob-Cap Analyse (Vanilla 1.12.2 Hostile Cap ist meist 70)
        int monsterColor = (monsters > 60) ? 0xFF5555 : ModConfig.colors.colorMonsters;
        lines.add(new InfoLine("Hostiles: ", monsters + " / 70", monsterColor));

        // 3. Passive & Umgebung
        lines.add(new InfoLine("Animals: ", String.valueOf(animals), ModConfig.colors.colorCreatures));

        // 4. Loot & Performance-Fresser (Items und XP-Orbs)
        if (items > 100 || xp > 50) {
            // Warnung bei zu vielen Entities am Boden (Lag-Gefahr auf deinem 5600G)
            lines.add(new InfoLine("Loot: ", String.format("Items: %d | XP: %d", items, xp), 0xFFAA00));
        } else {
            lines.add(new InfoLine("Items: ", String.valueOf(items), 0xAAAAAA));
        }

        // 5. Andere (Rahmen, Boote, etc.)
        if (others > 0) {
            lines.add(new InfoLine("Misc: ", String.valueOf(others), 0x777777));
        }

        return lines;
    }
}