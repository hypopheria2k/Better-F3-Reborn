package com.worador.f3hud;

import com.worador.f3hud.layout.LayoutEngine;
import com.worador.f3hud.render.RendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class DebugRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();
    private float animProgress = 0.0f;
    private boolean f3WasDown = false;
    private final PerformanceGraph performanceGraph = new PerformanceGraph();

    // Festgelegte Schriftgröße (0.8 entspricht ca. Schriftgröße 7-8)
    private final float fontScale = 0.8f;

    public DebugRenderer() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onRenderGameOverlayPre(RenderGameOverlayEvent.Pre event) {
        if (mc.gameSettings.showDebugInfo || ModConfig.forceOpen || animProgress > 0.01f) {
            RenderGameOverlayEvent.ElementType type = event.getType();
            if (type == RenderGameOverlayEvent.ElementType.DEBUG ||
                    type == RenderGameOverlayEvent.ElementType.TEXT ||
                    type == RenderGameOverlayEvent.ElementType.CHAT) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderGameOverlayPost(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        updateAnimation();
        if (animProgress > 0.001f) {
            boolean original = mc.gameSettings.showDebugInfo;
            mc.gameSettings.showDebugInfo = false;
            renderBetterF3();
            mc.gameSettings.showDebugInfo = original;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (mc.gameSettings.showDebugInfo || ModConfig.forceOpen || animProgress > 0.01f) {
            String text = event.getMessage().getUnformattedText();
            if (text.contains("Debug") || text.contains("Chunk borders")) {
                event.setCanceled(true);
            }
        }
    }

    private void updateAnimation() {
        if (mc.player == null || mc.world == null) {
            animProgress = 0.0f;
            ModConfig.forceOpen = false;
            return;
        }
        boolean f3Down = Keyboard.isKeyDown(Keyboard.KEY_F3);
        if (f3Down && !f3WasDown && ModConfig.forceOpen) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_X)) {
                ModConfig.forceOpen = false;
            }
        }
        f3WasDown = f3Down;
        float speed = (float) ModConfig.animation.animationSpeed;
        if (mc.gameSettings.showDebugInfo || ModConfig.forceOpen) {
            animProgress = Math.min(1.0f, animProgress + speed);
        } else {
            animProgress = Math.max(0.0f, animProgress - speed);
        }
    }

    private void renderBetterF3() {
        if (mc.player == null || mc.world == null || mc.fontRenderer == null) return;
        ScaledResolution sr = new ScaledResolution(mc);
        LayoutEngine.update(sr.getScaledWidth());
        performanceGraph.update();

        renderLeftSide();
        renderRightSide(sr);
        renderStaticElements(sr);
    }

    private void renderStaticElements(ScaledResolution sr) {
        // 1. PERFORMANCE GRAPH (NUR EINMAL!)
        if (ModConfig.modules.showPerformanceGraph) {
            float eased = easeOutCubic(animProgress);
            int graphX = 5;

            // Wenn F3+X (forceOpen) aktiv ist, etwas höher rücken, sonst Standard unten links
            int graphY = ModConfig.forceOpen ? sr.getScaledHeight() - 75 : sr.getScaledHeight() - 65;

            GlStateManager.pushMatrix();
            GlStateManager.translate(0, (1.0f - eased) * 70, 0);
            performanceGraph.renderAt(graphX, graphY, 120, 60, animProgress);
            GlStateManager.popMatrix();
        }

        // 2. KOMPASS (Oben mittig fixieren)
        if (ModConfig.modules.showCompass) {
            // Wir suchen uns das Modul direkt aus der Liste, falls getCompassModule() fehlschlägt
            InfoModule compass = null;
            for (InfoModule m : ModuleRegistry.getLeftModules()) {
                if (m instanceof CompassModule) {
                    compass = m;
                    break;
                }
            }

            if (compass != null) {
                float compassProgress = Math.max(0.0f, (animProgress - 0.4f) / 0.6f);
                float easedCompass = easeOutCubic(compassProgress);
                int centerX = sr.getScaledWidth() / 2;
                int compassY = 5;

                GlStateManager.pushMatrix();
                GlStateManager.translate(0, (1.0f - easedCompass) * -30, 0);
                // Wir nutzen den Renderer direkt
                RendererRegistry.getRenderer(compass).render(compass, centerX - 50, compassY, animProgress);
                GlStateManager.popMatrix();
            }
        }
    }

    private void renderLeftSide() {
        GlStateManager.pushMatrix();
        if (ModConfig.animation.enableAnimation) {
            float eased = easeOutCubic(animProgress);
            float slide = (float) ModConfig.animation.slideDistance;
            GlStateManager.translate((1.0f - eased) * -slide, 0, 0);
        }

        float scale = LayoutEngine.leftScale;
        GlStateManager.scale(scale, scale, 1.0f);

        int x = (int) (ModConfig.position.leftOffsetX / scale);
        int y = (int) (ModConfig.position.leftOffsetY / scale);

        // FPS mit verkleinerter Schrift
        if (ModConfig.modules.showFPS) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(fontScale, fontScale, 1.0f);
            drawFPSLine((int)(x / fontScale), (int)(y / fontScale));
            GlStateManager.popMatrix();
            y += 9; // Reduzierter Zeilenabstand
        }

        for (InfoModule module : ModuleRegistry.getLeftModules()) {
            if (!module.isEnabled()) continue;
            // WICHTIG: Diese drei müssen hier ignoriert werden, da sie spezialbehandelt werden!
            String name = module.getName();
            if (name.equals("Compass") || name.equals("Performance Graph") || name.equals("FPS")) continue;

            GlStateManager.pushMatrix();
            GlStateManager.scale(fontScale, fontScale, 1.0f);
            RendererRegistry.getRenderer(module).render(module, (int)(x / fontScale), (int)(y / fontScale), animProgress);
            GlStateManager.popMatrix();

            // Berechnet Höhe basierend auf Zeilenanzahl * kleinerem Abstand
            y += (module.getLines().size() * 9);
        }
        GlStateManager.popMatrix();
    }

    private void renderRightSide(ScaledResolution sr) {
        GlStateManager.pushMatrix();
        if (ModConfig.animation.enableAnimation) {
            float eased = easeOutCubic(animProgress);
            float slide = (float) ModConfig.animation.slideDistance;
            GlStateManager.translate((1.0f - eased) * slide, 0, 0);
        }

        float scale = LayoutEngine.rightScale;
        GlStateManager.scale(scale, scale, 1.0f);

        int screenWidthVirtual = (int) (sr.getScaledWidth() / scale);
        int rightOffsetVirtual = (int) (ModConfig.position.rightOffsetX / scale);
        int y = (int) (ModConfig.position.rightOffsetY / scale);

        for (InfoModule module : ModuleRegistry.getRightModules()) {
            if (!module.isEnabled()) continue;

            // WICHTIG: Die Modulbreite muss auch die kleine Schrift berücksichtigen
            int moduleWidth = (int)(module.getMaxLineWidth() * fontScale);
            int x = screenWidthVirtual - moduleWidth - rightOffsetVirtual;

            GlStateManager.pushMatrix();
            GlStateManager.scale(fontScale, fontScale, 1.0f);
            RendererRegistry.getRenderer(module).render(module, (int)(x / fontScale), (int)(y / fontScale), animProgress);
            GlStateManager.popMatrix();

            y += (module.getLines().size() * 9);
        }
        GlStateManager.popMatrix();
    }

    private float easeOutCubic(float t) {
        return 1.0f - (float) Math.pow(1.0f - t, 3);
    }

    private void drawFPSLine(int x, int y) {
        int currentFps = Minecraft.getDebugFPS();
        int limit = mc.gameSettings.limitFramerate;
        String limitStr = (limit >= 260) ? "Unlimited" : String.valueOf(limit);
        mc.fontRenderer.drawStringWithShadow("FPS: " + currentFps + "/" + limitStr + " fps", x, y, ModConfig.colors.colorFPS);
    }
}