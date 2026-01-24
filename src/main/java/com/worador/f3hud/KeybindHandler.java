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

    public static KeyBinding kbEditor;

    public KeybindHandler() {
        // Standard-Keybind bleibt registriert für das Minecraft-Menü
        kbEditor = new KeyBinding("key.open.editor", Keyboard.KEY_COMMA, CATEGORY);
        ClientRegistry.registerKeyBinding(kbEditor);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (mc.player == null) return;

        boolean isCtrlDown = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);

        // Wir prüfen:
        // 1. KEY_ADD (Numpad +)
        // 2. KEY_EQUALS (US +)
        // 3. KEY_RBRACKET (DE +) -> Backup 1
        // 4. KEY_LBRACKET (DE Ü) -> Backup 2 (Universell neben 'P')
        boolean openKeyPressed = Keyboard.isKeyDown(Keyboard.KEY_ADD) ||
                Keyboard.isKeyDown(Keyboard.KEY_EQUALS) ||
                Keyboard.isKeyDown(Keyboard.KEY_RBRACKET) ||
                Keyboard.isKeyDown(Keyboard.KEY_LBRACKET);

        if (isCtrlDown && openKeyPressed) {
            if (mc.currentScreen == null) {
                openCustomConfig();
            }
            return;
        }

        // 2. Event-basierte Abfragen (F3-Kombis & Keybindings)
        int eventKey = Keyboard.getEventKey();
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_F3)) {
                if (!Keyboard.isRepeatEvent()) {
                    handleF3Combinations(eventKey);
                }
                return;
            }

            // Normale Keybind-Abfrage (Komma-Taste)
            if (kbEditor.isPressed()) {
                openCustomConfig();
            }
        }

        // --- NORMALE KEYBINDS (aus dem Menü) ---
        if (kbEditor.isPressed()) {
            openCustomConfig();
        }
    }

    private void handleF3Combinations(int keyCode) {
        switch (keyCode) {
            case Keyboard.KEY_C: toggle("Coordinates", !ModConfig.modules.showCoordinates); break;
            case Keyboard.KEY_S: toggle("System Info", !ModConfig.modules.showSystem); break;
            case Keyboard.KEY_G:
            case Keyboard.KEY_X: togglePerformanceGraph(); break;
            case Keyboard.KEY_K: toggle("Compass", !ModConfig.modules.showCompass); break;
            case Keyboard.KEY_F: toggle("FPS Display", !ModConfig.modules.showFPS); break;
            case Keyboard.KEY_R: toggle("Rotation", !ModConfig.modules.showRotation); break;
            case Keyboard.KEY_W: toggle("World Info", !ModConfig.modules.showWorld); break;
            case Keyboard.KEY_B: toggle("Beacon", !ModConfig.modules.showBeacon); break; // Geändert auf showBeacon (Sync mit GUI)
            case Keyboard.KEY_J: toggleBackground(); break;
            case Keyboard.KEY_Q: sendHelpMessage(); break;
        }
    }

    private void openCustomConfig() {
        if (mc.currentScreen == null) {
            mc.displayGuiScreen(new com.worador.f3hud.gui.GuiCustomConfig());
            mc.player.playSound(net.minecraft.init.SoundEvents.UI_BUTTON_CLICK, 0.5f, 1.2f);
        }
    }

    private void toggle(String name, boolean newState) {
        if (name.equals("Coordinates")) ModConfig.modules.showCoordinates = newState;
        else if (name.equals("System Info")) ModConfig.modules.showSystem = newState;
        else if (name.equals("Compass")) ModConfig.modules.showCompass = newState;
        else if (name.equals("FPS Display")) ModConfig.modules.showFPS = newState;
        else if (name.equals("Rotation")) ModConfig.modules.showRotation = newState;
        else if (name.equals("World Info")) ModConfig.modules.showWorld = newState;
        else if (name.equals("Beacon")) ModConfig.modules.showBeacon = newState;

        saveAndNotify(name, newState);
    }

    private void togglePerformanceGraph() {
        ModConfig.modules.showPerformanceGraph = !ModConfig.modules.showPerformanceGraph;
        saveAndNotify("Performance Graph", ModConfig.modules.showPerformanceGraph);
    }

    private void toggleBackground() {
        ModConfig.animation.showTextBackground = !ModConfig.animation.showTextBackground;
        saveAndNotify("Text Background", ModConfig.animation.showTextBackground);
    }

    private void saveAndNotify(String name, boolean state) {
        ConfigManager.sync("betterf3reborn", Config.Type.INSTANCE);
        if (ModConfig.modules.showChatMessages && mc.player != null) {
            String color = state ? TextFormatting.GREEN.toString() : TextFormatting.RED.toString();
            mc.player.sendMessage(new TextComponentString(
                    TextFormatting.DARK_AQUA + "[Better F3] " + TextFormatting.GRAY + name + ": " + color + (state ? "ON" : "OFF")
            ));
        }
    }

    private void sendHelpMessage() {
        mc.player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Better F3 Help (F3 + Key):"));
        mc.player.sendMessage(new TextComponentString(TextFormatting.GRAY + "C: Coords, S: System, G: Graph, K: Compass, F: FPS, R: Rotation, B: Beacon, J: BG"));
    }
}