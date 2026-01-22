package com.worador.f3hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeybindHandler {
    private final Minecraft mc = Minecraft.getMinecraft();

    private static final String CATEGORY = "key.categories.betterf3reborn";

    public static KeyBinding kbCoords, kbSystem, kbGraph, kbCompass, kbFPS, kbRotation;
    public static KeyBinding kbWorld, kbEntities, kbTargeted, kbDimension, kbMagic, kbBackground, kbBeacon;

    public KeybindHandler() {
        kbCoords = new KeyBinding("key.toggle.coordinates", Keyboard.KEY_NONE, CATEGORY);
        kbSystem = new KeyBinding("key.toggle.system", Keyboard.KEY_NONE, CATEGORY);
        kbGraph = new KeyBinding("key.toggle.graph", Keyboard.KEY_NONE, CATEGORY);
        kbCompass = new KeyBinding("key.toggle.compass", Keyboard.KEY_NONE, CATEGORY);
        kbFPS = new KeyBinding("key.toggle.fps", Keyboard.KEY_NONE, CATEGORY);
        kbRotation = new KeyBinding("key.toggle.rotation", Keyboard.KEY_NONE, CATEGORY);
        kbWorld = new KeyBinding("key.toggle.world", Keyboard.KEY_NONE, CATEGORY);
        kbEntities = new KeyBinding("key.toggle.entities", Keyboard.KEY_NONE, CATEGORY);
        kbTargeted = new KeyBinding("key.toggle.targeted", Keyboard.KEY_NONE, CATEGORY);
        kbDimension = new KeyBinding("key.toggle.dimension", Keyboard.KEY_NONE, CATEGORY);
        kbMagic = new KeyBinding("key.toggle.magic", Keyboard.KEY_NONE, CATEGORY);
        kbBackground = new KeyBinding("key.toggle.background", Keyboard.KEY_NONE, CATEGORY);
        // Neues Keybind für Beacon
        kbBeacon = new KeyBinding("key.toggle.beacon", Keyboard.KEY_MINUS, CATEGORY);

        ClientRegistry.registerKeyBinding(kbCoords);
        ClientRegistry.registerKeyBinding(kbSystem);
        ClientRegistry.registerKeyBinding(kbGraph);
        ClientRegistry.registerKeyBinding(kbCompass);
        ClientRegistry.registerKeyBinding(kbFPS);
        ClientRegistry.registerKeyBinding(kbRotation);
        ClientRegistry.registerKeyBinding(kbWorld);
        ClientRegistry.registerKeyBinding(kbEntities);
        ClientRegistry.registerKeyBinding(kbTargeted);
        ClientRegistry.registerKeyBinding(kbDimension);
        ClientRegistry.registerKeyBinding(kbMagic);
        ClientRegistry.registerKeyBinding(kbBackground);
        ClientRegistry.registerKeyBinding(kbBeacon);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_F3)) {
            handleF3Combinations();
            return;
        }
        checkCustomKeybinds();
    }

    private void handleF3Combinations() {
        if (Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent()) {
            int keyCode = Keyboard.getEventKey();
            switch (keyCode) {
                case Keyboard.KEY_C: toggle("Coordinates", !ModConfig.modules.showCoordinates); break;
                case Keyboard.KEY_S: toggle("System Info", !ModConfig.modules.showSystem); break;
                case Keyboard.KEY_X: case Keyboard.KEY_G: togglePerformanceGraph(); break;
                case Keyboard.KEY_K: toggle("Compass", !ModConfig.modules.showCompass); break;
                case Keyboard.KEY_F: toggle("FPS Display", !ModConfig.modules.showFPS); break;
                case Keyboard.KEY_R: toggle("Rotation", !ModConfig.modules.showRotation); break;
                case Keyboard.KEY_W: toggle("World Info", !ModConfig.modules.showWorld); break;
                case Keyboard.KEY_E: toggle("Entities", !ModConfig.modules.showEntities); break;
                case Keyboard.KEY_T: toggle("Targeted Block", !ModConfig.modules.showTargetedBlock); break;
                case Keyboard.KEY_D: toggle("Dimension", !ModConfig.modules.showDimension); break;
                case Keyboard.KEY_M: toggle("Magic Modules", !ModConfig.modules.showBotania); break;
                case Keyboard.KEY_J: toggleBackground(); break;
                case Keyboard.KEY_MINUS: toggle("Beacon Range", !ModConfig.modules.showBeaconRange); break;
                case Keyboard.KEY_Q: sendHelpMessage(); break;
            }
        }
    }

    private void checkCustomKeybinds() {
        if (kbCoords.isPressed()) toggle("Coordinates", !ModConfig.modules.showCoordinates);
        if (kbSystem.isPressed()) toggle("System Info", !ModConfig.modules.showSystem);
        if (kbGraph.isPressed()) togglePerformanceGraph();
        if (kbCompass.isPressed()) toggle("Compass", !ModConfig.modules.showCompass);
        if (kbFPS.isPressed()) toggle("FPS Display", !ModConfig.modules.showFPS);
        if (kbRotation.isPressed()) toggle("Rotation", !ModConfig.modules.showRotation);
        if (kbWorld.isPressed()) toggle("World Info", !ModConfig.modules.showWorld);
        if (kbEntities.isPressed()) toggle("Entities", !ModConfig.modules.showEntities);
        if (kbTargeted.isPressed()) toggle("Targeted Block", !ModConfig.modules.showTargetedBlock);
        if (kbDimension.isPressed()) toggle("Dimension", !ModConfig.modules.showDimension);
        if (kbMagic.isPressed()) toggle("Magic Modules", !ModConfig.modules.showBotania);
        if (kbBackground.isPressed()) toggleBackground();
        if (kbBeacon.isPressed()) toggle("Beacon Range", !ModConfig.modules.showBeaconRange);
    }

    private void toggleBackground() {
        ModConfig.animation.showTextBackground = !ModConfig.animation.showTextBackground;
        ConfigManager.sync("betterf3reborn", Config.Type.INSTANCE);
        if (mc.player != null) {
            mc.player.playSound(net.minecraft.init.SoundEvents.UI_BUTTON_CLICK, 0.5f, 1.0f);
        }
        sendToggleMessage("Text Background", ModConfig.animation.showTextBackground);
    }

    private void toggle(String name, boolean newState) {
        if (name.equals("Coordinates")) ModConfig.modules.showCoordinates = newState;
        else if (name.equals("System Info")) ModConfig.modules.showSystem = newState;
        else if (name.equals("Compass")) ModConfig.modules.showCompass = newState;
        else if (name.equals("FPS Display")) ModConfig.modules.showFPS = newState;
        else if (name.equals("Rotation")) ModConfig.modules.showRotation = newState;
        else if (name.equals("World Info")) ModConfig.modules.showWorld = newState;
        else if (name.equals("Entities")) ModConfig.modules.showEntities = newState;
        else if (name.equals("Targeted Block")) ModConfig.modules.showTargetedBlock = newState;
        else if (name.equals("Dimension")) ModConfig.modules.showDimension = newState;
        else if (name.equals("Beacon Range")) ModConfig.modules.showBeaconRange = newState;
        else if (name.equals("Magic Modules")) {
            ModConfig.modules.showBotania = newState;
            ModConfig.modules.showThaumcraft = newState;
            ModConfig.modules.showBloodMagic = newState;
            ModConfig.modules.showAstralSorcery = newState;
        }
        ConfigManager.sync("betterf3reborn", Config.Type.INSTANCE);
        sendToggleMessage(name, newState);
    }

    private void togglePerformanceGraph() {
        ModConfig.modules.showPerformanceGraph = !ModConfig.modules.showPerformanceGraph;
        if (ModConfig.modules.showPerformanceGraph) ModConfig.forceOpen = true;
        ConfigManager.sync("betterf3reborn", Config.Type.INSTANCE);
        sendToggleMessage("Performance Graph", ModConfig.modules.showPerformanceGraph);
    }

    private void sendToggleMessage(String moduleName, boolean enabled) {
        if (mc.player != null && ModConfig.modules.showChatMessages) {
            String status = enabled ? TextFormatting.GREEN + "ENABLED" : TextFormatting.RED + "DISABLED";
            mc.player.sendMessage(new TextComponentString(
                    TextFormatting.DARK_AQUA + "[Better F3] " + TextFormatting.GRAY + moduleName + ": " + status
            ));
        }
    }

    private void sendHelpMessage() {
        if (mc.player == null) return;
        mc.player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Better F3 Reborn - Shortcuts (F3 + Key):"));
        mc.player.sendMessage(new TextComponentString(TextFormatting.GRAY + "C, S, F, K, G/X, R, W, E, T, D, M, J, -"));
    }
}