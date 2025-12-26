package com.worador.f3hud;

import java.util.ArrayList;
import java.util.List;

public class RotationModule extends InfoModule {
    
    @Override
    public String getName() {
        return "Rotation";
    }
    
    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showRotation;
    }
    
    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        
        if (mc.player == null) return lines;
        
        float yaw = mc.player.rotationYaw % 360;
        if (yaw < 0) yaw += 360;
        float pitch = mc.player.rotationPitch;
        
        String rotation = String.format("Yaw: %.1f Pitch: %.1f", yaw, pitch);
        lines.add(new InfoLine("Rotation: ", rotation, ModConfig.colors.colorDefault));
        
        return lines;
    }
}