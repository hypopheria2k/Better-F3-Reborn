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

    private static final String CATEGORY = "Better F3 Reloaded";

    public static KeyBinding toggleCoords;
    public static KeyBinding toggleEntities;
    public static KeyBinding toggleSystem;
    public static KeyBinding toggleGraph;

    public KeybindHandler() {
        toggleCoords = new KeyBinding("Toggle Coordinates", Keyboard.KEY_NONE, CATEGORY);
        toggleEntities = new KeyBinding("Toggle Entities", Keyboard.KEY_NONE, CATEGORY);
        toggleSystem = new KeyBinding("Toggle System Info", Keyboard.KEY_NONE, CATEGORY);
        toggleGraph = new KeyBinding("Toggle Performance Graph", Keyboard.KEY_NONE, CATEGORY);

        ClientRegistry.registerKeyBinding(toggleCoords);
        ClientRegistry.registerKeyBinding(toggleEntities);
        ClientRegistry.registerKeyBinding(toggleSystem);
        ClientRegistry.registerKeyBinding(toggleGraph);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_F3)) {
            handleF3Combinations();
        }

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

        // Fix für den eigenen Hotkey aus dem Menü
        if (toggleGraph.isPressed()) {
            ModConfig.modules.showPerformanceGraph = !ModConfig.modules.showPerformanceGraph;
            // Kopplung an forceOpen: Graph an = HUD bleibt offen
            ModConfig.forceOpen = ModConfig.modules.showPerformanceGraph;
            sendToggleMessage("Performance Graph", ModConfig.modules.showPerformanceGraph);
        }
    }

    private void handleF3Combinations() {
        if (Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent()) {
            int keyCode = Keyboard.getEventKey();

            if (keyCode == Keyboard.KEY_C) {
                ModConfig.modules.showCoordinates = !ModConfig.modules.showCoordinates;
                sendToggleMessage("Coordinates", ModConfig.modules.showCoordinates);
            } else if (keyCode == Keyboard.KEY_E) {
                ModConfig.modules.showEntities = !ModConfig.modules.showEntities;
                sendToggleMessage("Entities", ModConfig.modules.showEntities);
            } else if (keyCode == Keyboard.KEY_S) {
                ModConfig.modules.showSystem = !ModConfig.modules.showSystem;
                sendToggleMessage("System Info", ModConfig.modules.showSystem);
            } else if (keyCode == Keyboard.KEY_W) {
                ModConfig.modules.showWorld = !ModConfig.modules.showWorld;
                sendToggleMessage("World Info", ModConfig.modules.showWorld);
            } else if (keyCode == Keyboard.KEY_R) {
                ModConfig.modules.showRotation = !ModConfig.modules.showRotation;
                sendToggleMessage("Rotation", ModConfig.modules.showRotation);
            } else if (keyCode == Keyboard.KEY_T) {
                ModConfig.modules.showTargetedBlock = !ModConfig.modules.showTargetedBlock;
                sendToggleMessage("Targeted Block", ModConfig.modules.showTargetedBlock);
            } else if (keyCode == Keyboard.KEY_D) {
                ModConfig.modules.showDimension = !ModConfig.modules.showDimension;
                sendToggleMessage("Dimension", ModConfig.modules.showDimension);
            } else if (keyCode == Keyboard.KEY_X) {
                ModConfig.modules.showPerformanceGraph = !ModConfig.modules.showPerformanceGraph;

                // DAS HIER IST NEU:
                // Wenn der Graph per F3+X aktiviert wird, setzen wir forceOpen auf true.
                // Das HUD bleibt dann auch nach dem Loslassen von F3 offen.
                ModConfig.forceOpen = ModConfig.modules.showPerformanceGraph;

                sendToggleMessage("Performance Graph", ModConfig.modules.showPerformanceGraph);
            } else if (keyCode == Keyboard.KEY_F) {
                ModConfig.modules.showFPS = !ModConfig.modules.showFPS;
                sendToggleMessage("FPS Display", ModConfig.modules.showFPS);
            }
        }
    }

    private void sendToggleMessage(String moduleName, boolean enabled) {
        if (mc.player != null && ModConfig.modules.showChatMessages) {
            String status = enabled ? TextFormatting.GREEN + "enabled" : TextFormatting.RED + "disabled";
            mc.player.sendMessage(new TextComponentString(
                    TextFormatting.YELLOW + "[Better F3] " + TextFormatting.WHITE + moduleName + " " + status
            ));
        }
    }
}