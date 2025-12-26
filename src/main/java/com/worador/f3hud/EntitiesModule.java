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
        
        // Particles
        int particles = 0;
        try {
            String particleStats = mc.effectRenderer.getStatistics();
            if (particleStats != null && !particleStats.isEmpty()) {
                particles = Integer.parseInt(particleStats);
            }
        } catch (Exception e) {
            particles = 0;
        }
        lines.add(new InfoLine("Particles: ", String.valueOf(particles), ModConfig.colors.colorMonsters));
        
        // Entities rendered/total
        lines.add(new InfoLine("Entities rendered/total: ", mc.renderGlobal.getDebugInfoEntities(), ModConfig.colors.colorMonsters));
        
        // Count different entity types
        List<Entity> allEntities = new ArrayList<>();
        mc.world.loadedEntityList.forEach(allEntities::add);
        
        int monsters = 0;
        int creatures = 0;
        int waterCreatures = 0;
        int underground = 0;
        
        for (Entity e : allEntities) {
            if (e instanceof IMob) monsters++;
            else if (e instanceof EntityAnimal) creatures++;
            else if (e instanceof EntityWaterMob) waterCreatures++;
            
            if (e.posY < 63) underground++;
        }
        
        // Monsters
        lines.add(new InfoLine("Monsters: ", String.valueOf(monsters), ModConfig.colors.colorMonsters));
        
        // Creatures
        lines.add(new InfoLine("Creatures: ", String.valueOf(creatures), ModConfig.colors.colorCreatures));
        
        // Underground Water Creatures
        lines.add(new InfoLine("Underground Water Creatures: ", String.valueOf(underground), 0x5555FF));
        
        // Water Creatures
        lines.add(new InfoLine("Water Creatures: ", String.valueOf(waterCreatures), 0x5555FF));
        
        return lines;
    }
}