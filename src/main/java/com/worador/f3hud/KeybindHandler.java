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
    private static final String CATEGORY = "Better F3 Reborn";

    public static KeyBinding toggleCoords;
    public static KeyBinding toggleSystem;
    public static KeyBinding toggleGraph;

    public KeybindHandler() {
        toggleCoords = new KeyBinding("Toggle Coordinates", Keyboard.KEY_NONE, CATEGORY);
        toggleSystem = new KeyBinding("Toggle System Info", Keyboard.KEY_NONE, CATEGORY);
        toggleGraph = new KeyBinding("Toggle Performance Graph", Keyboard.KEY_NONE, CATEGORY);

        ClientRegistry.registerKeyBinding(toggleCoords);
        ClientRegistry.registerKeyBinding(toggleSystem);
        ClientRegistry.registerKeyBinding(toggleGraph);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        // F3-Kombinationen abfangen
        if (Keyboard.isKeyDown(Keyboard.KEY_F3)) {
            handleF3Combinations();
            return;
        }

        // Normale Keybinds aus dem Minecraft-Menü
        if (toggleCoords.isPressed()) {
            ModConfig.modules.showCoordinates = !ModConfig.modules.showCoordinates;
            sendToggleMessage("Coordinates", ModConfig.modules.showCoordinates);
        }
        if (toggleSystem.isPressed()) {
            ModConfig.modules.showSystem = !ModConfig.modules.showSystem;
            sendToggleMessage("System Info", ModConfig.modules.showSystem);
        }
        if (toggleGraph.isPressed()) {
            togglePerformanceGraph();
        }
    }

    private void handleF3Combinations() {
        // EventKeyState prüft, ob die Taste gerade GEDRÜCKT wurde (verhindert Mehrfach-Trigger)
        if (Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent()) {
            int keyCode = Keyboard.getEventKey();

            switch (keyCode) {
                case Keyboard.KEY_C:
                    ModConfig.modules.showCoordinates = !ModConfig.modules.showCoordinates;
                    sendToggleMessage("Coordinates", ModConfig.modules.showCoordinates);
                    break;
                case Keyboard.KEY_S:
                    ModConfig.modules.showSystem = !ModConfig.modules.showSystem;
                    sendToggleMessage("System Info", ModConfig.modules.showSystem);
                    break;
                case Keyboard.KEY_X: // Dein Kürzel für den Graph
                    togglePerformanceGraph();
                    break;
                case Keyboard.KEY_K: // Kompass
                    ModConfig.modules.showCompass = !ModConfig.modules.showCompass;
                    sendToggleMessage("Compass", ModConfig.modules.showCompass);
                    break;
                case Keyboard.KEY_F: // FPS Zeile
                    ModConfig.modules.showFPS = !ModConfig.modules.showFPS;
                    sendToggleMessage("FPS Display", ModConfig.modules.showFPS);
                    break;
                case Keyboard.KEY_G: // Klassisches Minecraft Kürzel für Graph
                    togglePerformanceGraph();
                    break;
                case Keyboard.KEY_Q:
                    sendHelpMessage();
                    break;
            }
        }
    }

    private void togglePerformanceGraph() {
        ModConfig.modules.showPerformanceGraph = !ModConfig.modules.showPerformanceGraph;

        // Fix: forceOpen nur umschalten, wenn wir F3 NICHT halten,
        // damit man das HUD auch wieder schließen kann.
        if (ModConfig.modules.showPerformanceGraph) {
            ModConfig.forceOpen = true;
        }

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

        mc.player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Better F3 Reborn - Key Bindings:"));
        mc.player.sendMessage(new TextComponentString(TextFormatting.YELLOW + " F3 + Q: " + TextFormatting.WHITE + "Show this help list"));
        mc.player.sendMessage(new TextComponentString(TextFormatting.YELLOW + " F3 + C: " + TextFormatting.WHITE + "Toggle Coordinates"));
        mc.player.sendMessage(new TextComponentString(TextFormatting.YELLOW + " F3 + S: " + TextFormatting.WHITE + "Toggle System Info"));
        mc.player.sendMessage(new TextComponentString(TextFormatting.YELLOW + " F3 + F: " + TextFormatting.WHITE + "Toggle FPS Display"));
        mc.player.sendMessage(new TextComponentString(TextFormatting.YELLOW + " F3 + K: " + TextFormatting.WHITE + "Toggle Compass"));
        mc.player.sendMessage(new TextComponentString(TextFormatting.YELLOW + " F3 + G: " + TextFormatting.WHITE + "Toggle Performance Graph"));
        mc.player.sendMessage(new TextComponentString(TextFormatting.YELLOW + " F3 + X: " + TextFormatting.WHITE + "Fix Graph & HUD (Force Open)"));
    }

}