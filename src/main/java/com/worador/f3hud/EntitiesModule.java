package com.worador.f3hud;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;

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

        // Particles & Entities (Standard Debug Info)
        lines.add(new InfoLine("Particles: ", mc.effectRenderer.getStatistics(), ModConfig.colors.colorMonsters));
        lines.add(new InfoLine("Entities: ", mc.renderGlobal.getDebugInfoEntities(), ModConfig.colors.colorMonsters));

        int monsters = 0, creatures = 0, water = 0, ambient = 0, other = 0;

        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof IMob) monsters++;
            else if (e instanceof EntityAnimal) creatures++;
            else if (e instanceof EntityWaterMob) water++;
            else if (e instanceof net.minecraft.entity.passive.EntityAmbientCreature) ambient++;
            else other++;
        }

        lines.add(new InfoLine("Monsters: ", String.valueOf(monsters), ModConfig.colors.colorMonsters));
        lines.add(new InfoLine("Creatures: ", String.valueOf(creatures), ModConfig.colors.colorCreatures));
        lines.add(new InfoLine("Water: ", String.valueOf(water), 0x5555FF));
        lines.add(new InfoLine("Other: ", String.valueOf(other), 0xAAAAAA));

        return lines;
    }
} 
