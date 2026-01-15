package com.worador.f3hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class DebugRenderer {
    private final Minecraft mc = Minecraft.getMinecraft();
    private float animProgress = 0.0f;
    private final PerformanceGraph performanceGraph = new PerformanceGraph();

    // NEU: Verhindert den sofortigen Reset beim Loslassen der F3+X Kombi
    private boolean f3WasDown = false;

    private final List<InfoModule> leftModules = new ArrayList<>();
    private final List<InfoModule> rightModules = new ArrayList<>();

    public DebugRenderer() {
        // LINKE SEITE
        leftModules.add(new CoordinatesModule());
        leftModules.add(new RotationModule());
        leftModules.add(new WorldModule());
        leftModules.add(new DimensionModule());
        leftModules.add(new RegionModule());
        leftModules.add(new TargetedBlockModule());
        leftModules.add(new ServerModule());
        leftModules.add(new EntitiesModule());

        // RECHTE SEITE
        rightModules.add(new SystemModule());
    }

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
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            updateAnimation();
            performanceGraph.update();

            if (animProgress > 0.001f) {
                boolean originalShowDebug = mc.gameSettings.showDebugInfo;
                mc.gameSettings.showDebugInfo = false;

                renderBetterF3();

                if (ModConfig.modules.showPerformanceGraph) {
                    performanceGraph.render(animProgress);
                }

                mc.gameSettings.showDebugInfo = originalShowDebug;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatReceived(ClientChatReceivedEvent event) {
        String text = event.getMessage().getUnformattedText();

        if (text.contains("Debug") || text.contains("Chunk borders")) {
            if (mc.gameSettings.showDebugInfo || ModConfig.forceOpen || animProgress > 0.01f) {
                event.setMessage(new net.minecraft.util.text.TextComponentString(""));
                event.setCanceled(true);
            }
        }
    }

    private void updateAnimation() {
        if (mc.player == null || mc.world == null) {
            animProgress = 0.0f;
            ModConfig.forceOpen = false;
            ModConfig.modules.showPerformanceGraph = false;
            return;
        }

        boolean f3IsDown = Keyboard.isKeyDown(Keyboard.KEY_F3);

        // LOGIK-FIX:
        // Wir prüfen, ob F3 NEU gedrückt wurde (Transition false -> true).
        // Wenn das passiert, während forceOpen aktiv ist, schalten wir es aus,
        // außer man drückt gleichzeitig X (für die Aktivierung).
        if (f3IsDown && !f3WasDown && ModConfig.forceOpen) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_X)) {
                ModConfig.forceOpen = false;
                ModConfig.modules.showPerformanceGraph = false;
            }
        }

        // Status für den nächsten Frame merken
        f3WasDown = f3IsDown;

        float speed = (float) ModConfig.animation.animationSpeed;
        if (mc.gameSettings.showDebugInfo || ModConfig.forceOpen) {
            if (animProgress < 1.0f) animProgress += speed;
        } else {
            if (animProgress > 0.0f) animProgress -= speed;
        }
        animProgress = Math.max(0.0f, Math.min(1.0f, animProgress));
    }

    private void renderBetterF3() {
        if (mc.player == null || mc.world == null || mc.fontRenderer == null) return;
        ScaledResolution sr = new ScaledResolution(mc);
        renderLeftSide();
        renderRightSide(sr);
    }

    private void renderLeftSide() {
        GlStateManager.pushMatrix();
        if (ModConfig.animation.enableAnimation) {
            float eased = easeOutCubic(animProgress);
            float slideDistance = (float) ModConfig.animation.slideDistance;
            GlStateManager.translate((1.0f - eased) * -slideDistance, 0, 0);
            GlStateManager.color(1.0f, 1.0f, 1.0f, animProgress);
        }

        float scale = (float) ModConfig.position.scale;
        GlStateManager.scale(scale, scale, 1.0f);

        int x = (int)(ModConfig.position.leftOffsetX / scale);
        int y = (int)(ModConfig.position.leftOffsetY / scale);

        if (ModConfig.modules.showFPS) {
            drawFPSLine(x, y);
            y += 17;
        }

        boolean firstModule = true;
        for (InfoModule module : leftModules) {
            if (!module.isEnabled()) continue;
            if (!firstModule) y += 5;
            firstModule = false;

            List<InfoModule.InfoLine> lines = module.getLines();
            for (int i = 0; i < lines.size(); i++) {
                InfoModule.InfoLine line = lines.get(i);
                if (module instanceof CoordinatesModule && i == 1) {
                    drawXYZLine(x, y);
                } else {
                    drawRow(line.label, line.value, x, y, line.color);
                }
                y += 11;
            }
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    private void renderRightSide(ScaledResolution sr) {
        GlStateManager.pushMatrix();
        if (ModConfig.animation.enableAnimation) {
            float eased = easeOutCubic(animProgress);
            float slideDistance = (float) ModConfig.animation.slideDistance;
            GlStateManager.translate((1.0f - eased) * slideDistance, 0, 0);
            GlStateManager.color(1.0f, 1.0f, 1.0f, animProgress);
        }

        float scale = (float) ModConfig.position.scale;
        GlStateManager.scale(scale, scale, 1.0f);
        int screenWidth = (int)(sr.getScaledWidth() / scale);
        int y = (int)(ModConfig.position.rightOffsetY / scale);

        for (InfoModule module : rightModules) {
            if (!module.isEnabled()) continue;
            for (InfoModule.InfoLine line : module.getLines()) {
                int lineWidth = mc.fontRenderer.getStringWidth(line.label + line.value);
                int x = screenWidth - lineWidth - (int)(ModConfig.position.rightOffsetX / scale);
                drawRow(line.label, line.value, x, y, line.color);
                y += 11;
            }
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    private float easeOutCubic(float t) { return 1.0f - (float)Math.pow(1.0f - t, 3); }

    private void drawFPSLine(int x, int y) {
        int fps = Minecraft.getDebugFPS();
        int fpsLimit = mc.gameSettings.limitFramerate;
        String limitText = (fpsLimit == 260) ? "Unlimited fps" : fpsLimit + " fps";
        String text = fps + " fps / " + limitText;
        int w = mc.fontRenderer.getStringWidth(text);
        int alpha = (int)(animProgress * ModConfig.animation.backgroundAlpha) << 24;
        Gui.drawRect(x - 1, y - 1, x + w + 2, y + 9, alpha | 0x000000);
        mc.fontRenderer.drawStringWithShadow(text, x, y, ModConfig.colors.colorFPS);
    }

    private void drawXYZLine(int x, int y) {
        if (mc.player == null) return;
        String xS = String.format("%.5f", mc.player.posX);
        String yS = String.format("%.5f", mc.player.getEntityBoundingBox().minY);
        String zS = String.format("%.5f", mc.player.posZ);
        int w = mc.fontRenderer.getStringWidth("XYZ: " + xS + " " + yS + " " + zS);
        int alpha = (int)(animProgress * ModConfig.animation.backgroundAlpha) << 24;
        Gui.drawRect(x - 1, y - 1, x + w + 4, y + 9, alpha | 0x000000);
        int cx = x;
        mc.fontRenderer.drawStringWithShadow("XYZ: ", cx, y, ModConfig.colors.colorDefault);
        cx += mc.fontRenderer.getStringWidth("XYZ: ");
        mc.fontRenderer.drawStringWithShadow(xS, cx, y, ModConfig.colors.colorX);
        cx += mc.fontRenderer.getStringWidth(xS + " ");
        mc.fontRenderer.drawStringWithShadow(yS, cx, y, ModConfig.colors.colorY);
        cx += mc.fontRenderer.getStringWidth(yS + " ");
        mc.fontRenderer.drawStringWithShadow(zS, cx, y, ModConfig.colors.colorZ);
    }

    private void drawRow(String label, String value, int x, int y, int color) {
        int w = mc.fontRenderer.getStringWidth(label + value);
        int alpha = (int)(animProgress * ModConfig.animation.backgroundAlpha) << 24;
        Gui.drawRect(x - 1, y - 1, x + w + 2, y + 9, alpha | 0x000000);
        mc.fontRenderer.drawStringWithShadow(label, x, y, color);
        mc.fontRenderer.drawStringWithShadow(value, x + mc.fontRenderer.getStringWidth(label), y, color);
    }
}