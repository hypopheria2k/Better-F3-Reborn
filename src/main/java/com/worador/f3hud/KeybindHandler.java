package com.worador.f3hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeybindHandler {
    private final Minecraft mc = Minecraft.getMinecraft();
    
    // Keybinds (optional - falls User eigene Tasten setzen will)
    public static KeyBinding toggleCoords;
    public static KeyBinding toggleEntities;
    public static KeyBinding toggleSystem;
    public static KeyBinding toggleGraph;
    
    // Für F3 + Taste Kombinationen
    private boolean f3Pressed = false;
    
    public KeybindHandler() {
        // Registriere Custom Keybinds (erscheinen in Controls)
        toggleCoords = new KeyBinding("Toggle Coordinates", Keyboard.KEY_NONE, "F3 HUD");
        toggleEntities = new KeyBinding("Toggle Entities", Keyboard.KEY_NONE, "F3 HUD");
        toggleSystem = new KeyBinding("Toggle System Info", Keyboard.KEY_NONE, "F3 HUD");
        toggleGraph = new KeyBinding("Toggle Performance Graph", Keyboard.KEY_NONE, "F3 HUD");
        
        ClientRegistry.registerKeyBinding(toggleCoords);
        ClientRegistry.registerKeyBinding(toggleEntities);
        ClientRegistry.registerKeyBinding(toggleSystem);
        ClientRegistry.registerKeyBinding(toggleGraph);
    }
    
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        // Prüfe ob F3 gedrückt ist
        if (Keyboard.isKeyDown(Keyboard.KEY_F3)) {
            f3Pressed = true;
            handleF3Combinations();
        } else {
            f3Pressed = false;
        }
        
        // Custom Keybinds (falls User eigene gesetzt hat)
        if (toggleCoords.isPressed()) {
            ModConfig.modules.showCoordinates = !ModConfig.modules.showCoordinates;
            sendToggleMessage("Coordinates", ModConfig.modules.showCoordinates);
        }
        
        if (toggleEntities.isPressed()) {
            ModConfig.modules.showEntities = !ModConfig.modules.showEntities;
            sendToggleMessage("Entities", ModConfig.modules.showEntities);
        }
        
        if (toggleSystem.isPressed()) {
            ModConfig.modules.showSystem = !ModConfig.modules.showSystem;
            sendToggleMessage("System Info", ModConfig.modules.showSystem);
        }
        
        if (toggleGraph.isPressed()) {
            ModConfig.modules.showPerformanceGraph = !ModConfig.modules.showPerformanceGraph;
            sendToggleMessage("Performance Graph", ModConfig.modules.showPerformanceGraph);
        }
    }
    
    private void handleF3Combinations() {
        // F3 + C → Toggle Coordinates
        if (Keyboard.isKeyDown(Keyboard.KEY_C) && !Keyboard.isRepeatEvent()) {
            ModConfig.modules.showCoordinates = !ModConfig.modules.showCoordinates;
            sendToggleMessage("Coordinates", ModConfig.modules.showCoordinates);
        }
        
        // F3 + E → Toggle Entities
        if (Keyboard.isKeyDown(Keyboard.KEY_E) && !Keyboard.isRepeatEvent()) {
            ModConfig.modules.showEntities = !ModConfig.modules.showEntities;
            sendToggleMessage("Entities", ModConfig.modules.showEntities);
        }
        
        // F3 + S → Toggle System Info (Rechte Seite)
        if (Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isRepeatEvent()) {
            ModConfig.modules.showSystem = !ModConfig.modules.showSystem;
            sendToggleMessage("System Info", ModConfig.modules.showSystem);
        }
        
        // F3 + W → Toggle World Info
        if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isRepeatEvent()) {
            ModConfig.modules.showWorld = !ModConfig.modules.showWorld;
            sendToggleMessage("World Info", ModConfig.modules.showWorld);
        }
        
        // F3 + R → Toggle Rotation
        if (Keyboard.isKeyDown(Keyboard.KEY_R) && !Keyboard.isRepeatEvent()) {
            ModConfig.modules.showRotation = !ModConfig.modules.showRotation;
            sendToggleMessage("Rotation", ModConfig.modules.showRotation);
        }
        
        // F3 + T → Toggle Targeted Block
        if (Keyboard.isKeyDown(Keyboard.KEY_T) && !Keyboard.isRepeatEvent()) {
            ModConfig.modules.showTargetedBlock = !ModConfig.modules.showTargetedBlock;
            sendToggleMessage("Targeted Block", ModConfig.modules.showTargetedBlock);
        }
        
        // F3 + D → Toggle Dimension
        if (Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isRepeatEvent()) {
            ModConfig.modules.showDimension = !ModConfig.modules.showDimension;
            sendToggleMessage("Dimension", ModConfig.modules.showDimension);
        }
        
        // F3 + G → Toggle Performance Graph
        if (Keyboard.isKeyDown(Keyboard.KEY_G) && !Keyboard.isRepeatEvent()) {
            ModConfig.modules.showPerformanceGraph = !ModConfig.modules.showPerformanceGraph;
            sendToggleMessage("Performance Graph", ModConfig.modules.showPerformanceGraph);
        }
        
        // F3 + F → Toggle FPS Display
        if (Keyboard.isKeyDown(Keyboard.KEY_F) && !Keyboard.isRepeatEvent()) {
            ModConfig.modules.showFPS = !ModConfig.modules.showFPS;
            sendToggleMessage("FPS Display", ModConfig.modules.showFPS);
        }
    }
    
    private void sendToggleMessage(String moduleName, boolean enabled) {
        if (mc.player != null) {
            String status = enabled ? 
                TextFormatting.GREEN + "enabled" : 
                TextFormatting.RED + "disabled";
            mc.player.sendMessage(new TextComponentString(
                TextFormatting.YELLOW + "[F3 HUD] " + 
                TextFormatting.WHITE + moduleName + " " + status
            ));
        }
    }
}