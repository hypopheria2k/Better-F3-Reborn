package com.worador.f3hud;

import com.worador.f3hud.layout.LayoutEngine;
import com.worador.f3hud.render.RendererRegistry;
import com.worador.f3hud.util.RenderUtils;
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

        renderLeftSide(sr);
        renderRightSide(sr);
        renderStaticElements(sr);
    }

    private void renderStaticElements(ScaledResolution sr) {
        if (ModConfig.modules.showPerformanceGraph) {
            float eased = easeOutCubic(animProgress);
            int graphX = 5;
            int graphY = ModConfig.forceOpen ? sr.getScaledHeight() - 75 : sr.getScaledHeight() - 65;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, (1.0f - eased) * 70, 0);
            performanceGraph.renderAt(graphX, graphY, 120, 60, animProgress);
            GlStateManager.popMatrix();
        }

        if (ModConfig.modules.showCompass) {
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

                // Korrektur: -68 ist der ideale Abstand zum Standard-HUD
                int compassY = sr.getScaledHeight() - 50 - ModConfig.position.compassYOffset;

                GlStateManager.pushMatrix();
                GlStateManager.translate(0, (1.0f - easedCompass) * 30, 0);
                RendererRegistry.getRenderer(compass).render(compass, centerX - 50, compassY, animProgress);
                GlStateManager.popMatrix();
            }
        }
    }

    private void renderLeftSide(ScaledResolution sr) {
        GlStateManager.pushMatrix();
        float scale = (float) ModConfig.position.userScale;

        if (ModConfig.animation.enableAnimation) {
            float eased = easeOutCubic(animProgress);
            float slide = (float) ModConfig.animation.slideDistance;
            GlStateManager.translate((1.0f - eased) * -slide, 0, 0);
        }

        GlStateManager.scale(scale, scale, 1.0f);
        int x = (int) (ModConfig.position.leftX / scale);
        int y = (int) (ModConfig.position.leftY / scale);

        if (ModConfig.modules.showFPS) {
            drawFPSLine(x, y);
            y += 11;
        }

        for (InfoModule module : ModuleRegistry.getLeftModules()) {
            if (!module.isEnabled()) continue;
            String name = module.getName();
            if (name.equals("Compass") || name.equals("Performance Graph") || name.equals("FPS")) continue;

            RendererRegistry.getRenderer(module).render(module, x, y, animProgress);
            y += (module.getLines().size() * 11);
        }
        GlStateManager.popMatrix();
    }

    private void renderRightSide(ScaledResolution sr) {
        GlStateManager.pushMatrix();
        float scale = (float) ModConfig.position.userScale;

        if (ModConfig.animation.enableAnimation) {
            float eased = easeOutCubic(animProgress);
            float slide = (float) ModConfig.animation.slideDistance;
            GlStateManager.translate((1.0f - eased) * slide, 0, 0);
        }

        GlStateManager.scale(scale, scale, 1.0f);
        float xBase = sr.getScaledWidth() / scale;
        float y = (float) (ModConfig.position.rightY / scale);
        float rXOffset = (float) (ModConfig.position.rightX / scale);

        for (InfoModule module : ModuleRegistry.getRightModules()) {
            if (!module.isEnabled()) continue;

            int moduleWidth = module.getMaxLineWidth();
            float x = xBase - moduleWidth - rXOffset;

            RendererRegistry.getRenderer(module).render(module, (int) x, (int) y, animProgress);
            y += (module.getLines().size() * 11);
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
        String fullText = "FPS: " + currentFps + "/" + limitStr + " fps";

        int maxWidth = mc.fontRenderer.getStringWidth(fullText);
        RenderUtils.drawComponentBackground(x, y, maxWidth, 11, animProgress);

        mc.fontRenderer.drawStringWithShadow(fullText, x, y, ModConfig.colors.colorFPS);
    }
} 
