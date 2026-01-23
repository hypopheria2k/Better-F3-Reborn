package com.worador.f3hud.gui;

import com.worador.f3hud.InfoModule;
import com.worador.f3hud.ModConfig;
import com.worador.f3hud.ModuleRegistry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.common.config.ConfigManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiCustomConfig extends GuiScreen {

    private enum Tab { MODULES, LAYOUT, COLORS }
    private Tab currentTab = Tab.MODULES;

    private static final int BUTTON_WIDTH = 180;
    private static final int SCROLL_SPEED = 24;

    private int scrollOffset = 0;
    private int maxScroll = 0;

    private final Map<Integer, Integer> colorCache = new HashMap<>();
    private long lastCacheUpdate = 0;

    private final Map<Class<? extends InfoModule>, String> moduleFieldMapping = new HashMap<>();
    private List<InfoModule> leftModules;
    private List<InfoModule> rightModules;

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.colorCache.clear();
        this.scrollOffset = 0;
        leftModules = ModuleRegistry.getLeftModules();
        rightModules = ModuleRegistry.getRightModules();
        setupFieldMapping();

        int centerX = this.width / 2;

        this.buttonList.add(new GuiButtonExt(10, centerX - 155, 30, 100, 20, (currentTab == Tab.MODULES ? "§6§l" : "") + "Modules"));
        this.buttonList.add(new GuiButtonExt(11, centerX - 50, 30, 100, 20, (currentTab == Tab.LAYOUT ? "§6§l" : "") + "Layout"));
        this.buttonList.add(new GuiButtonExt(12, centerX + 55, 30, 100, 20, (currentTab == Tab.COLORS ? "§6§l" : "") + "Colors"));

        this.buttonList.add(new GuiButtonExt(0, centerX - (currentTab == Tab.COLORS ? 160 : 75), this.height - 30, 150, 20, "§aSave & Exit"));
        if (currentTab == Tab.COLORS) {
            this.buttonList.add(new GuiButtonExt(999, centerX + 10, this.height - 30, 150, 20, "§cReset Colors"));
        }

        switch (currentTab) {
            case MODULES: initModulesTab(); break;
            case LAYOUT:  initLayoutTab(); break;
            case COLORS:  initColorsTab(); break;
        }
    }

    private void initModulesTab() {
        int areaHeight = this.height - 140;
        int totalContentHeight = Math.max(leftModules.size(), rightModules.size()) * 25;
        maxScroll = Math.max(0, totalContentHeight - areaHeight);
        int centerX = this.width / 2;
        this.buttonList.add(new GuiButtonExt(800, centerX - 75, 58, 150, 20, getPerformanceButtonText()));
        for (int i = 0; i < leftModules.size(); i++)
            this.buttonList.add(new GuiButtonExt(200 + i, centerX - 190, 85 + i * 25, BUTTON_WIDTH, 20, ""));
        for (int i = 0; i < rightModules.size(); i++)
            this.buttonList.add(new GuiButtonExt(300 + i, centerX + 10, 85 + i * 25, BUTTON_WIDTH, 20, ""));
    }

    private void initLayoutTab() {
        int centerX = this.width / 2;
        int y = 70;
        this.buttonList.add(new GuiSlider(102, centerX - 155, y, 150, 20, "HUD Scale: ", "", 0.1, 2.0, ModConfig.position.userScale, false, true, s -> {
            ModConfig.position.userScale = Math.round(s.getValue() * 10.0) / 10.0;
            s.displayString = "HUD Scale: " + ModConfig.position.userScale;
        }));
        this.buttonList.add(new GuiSlider(103, centerX + 5, y, 150, 20, "BG Alpha: ", "", 0, 255, ModConfig.animation.textBackgroundAlpha, false, true, s -> ModConfig.animation.textBackgroundAlpha = s.getValueInt()));
        y += 25;
        this.buttonList.add(new GuiSlider(100, centerX - 155, y, 150, 20, "Left X: ", "", 0, 200, ModConfig.position.leftX, false, true, s -> ModConfig.position.leftX = s.getValueInt()));
        this.buttonList.add(new GuiSlider(101, centerX + 5, y, 150, 20, "Right X: ", "", 0, 200, ModConfig.position.rightX, false, true, s -> ModConfig.position.rightX = s.getValueInt()));
        y += 25;
        this.buttonList.add(new GuiSlider(104, centerX - 155, y, 150, 20, "Compass Y: ", "", 0, 200, ModConfig.position.compassYOffset, false, true, s -> ModConfig.position.compassYOffset = s.getValueInt()));
        this.buttonList.add(new GuiSlider(107, centerX + 5, y, 150, 20, "Anim Dist: ", "", 0, 200, ModConfig.animation.slideDistance, false, true, s -> ModConfig.animation.slideDistance = s.getValueInt()));
        y += 30;
        this.buttonList.add(new GuiButtonExt(105, centerX - 155, y, 150, 20, getAnimationButtonText()));
        this.buttonList.add(new GuiButtonExt(106, centerX + 5, y, 150, 20, getBackgroundButtonText()));
        maxScroll = 0;
    }

    private void initColorsTab() {
        int centerX = this.width / 2;
        // Korrigiert auf die exakten Feldnamen in deiner ModConfig.Colors Klasse
        String[] fields = {"colorFPS", "colorX", "colorY", "colorZ", "colorSlimeChunk", "colorChunk", "colorBlock", "colorLight", "colorBiome", "colorDimension", "colorRotation", "colorMonsters", "colorCreatures", "colorSystem", "colorDefault", "colorCompass", "colorRegion", "colorTargetedBlock", "colorEntityStats", "colorEntities", "colorPerformance", "colorVersion", "colorServer", "colorEnd", "colorOxygen", "colorDurability", "colorPotion", "colorBeacon", "colorWeather", "colorGrowth", "colorSpeedometer", "colorVillagerTrade", "colorItemDespawn", "colorHealthAndHunger", "colorMobAggro", "colorMachineProgress", "colorExplosion", "colorAstral", "colorStellar", "colorBotania", "colorBloodMagic", "colorThaumcraft"};
        String[] labels = {"FPS", "X", "Y", "Z", "Slime", "Chunks", "Block", "Light", "Biome", "Dim", "Rot", "Monsters", "Creatures", "System", "Default", "Compass", "Region", "Target", "E-Stats", "Entities", "Perf", "Ver", "Server", "End", "O2", "Dura", "Potion", "Beacon", "Weather", "Growth", "Speed", "Trade", "Despawn", "Health", "Aggro", "Machine", "Explo", "Astral", "Stellar", "Botania", "Blood", "Thaum"};

        int areaHeight = this.height - 110;
        maxScroll = Math.max(0, (fields.length / 2) * 45 - areaHeight + 65);
        for(int i = 0; i < fields.length; i++) {
            int xPos = (i % 2 == 0) ? centerX - 155 : centerX + 5;
            int yPos = 85 + (i / 2) * 45;
            addColorSlider(500 + i, xPos, yPos, labels[i], fields[i]);
        }
    }

    private void addColorSlider(int id, int x, int y, String label, String configField) {
        try {
            Field field = ModConfig.Colors.class.getField(configField);
            int currentColor = field.getInt(ModConfig.colors);
            float[] hsb = Color.RGBtoHSB((currentColor >> 16) & 0xFF, (currentColor >> 8) & 0xFF, currentColor & 0xFF, null);

            this.buttonList.add(new GuiSlider(id, x, y, 150, 20, label + ": ", "", 0, 1, hsb[0], false, true, s -> {
                try {
                    field.setInt(ModConfig.colors, Color.HSBtoRGB((float)s.getValue(), 0.8f, 1.0f) & 0xFFFFFF);
                } catch (Exception e) { e.printStackTrace(); }
                colorCache.remove(id);
            }));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(0, 0, this.width, this.height, 0xD0101010, 0xE0101010);
        int centerX = this.width / 2;
        drawRect(centerX - 160, 52, centerX + 160, 54, 0xFF555555);
        ScaledResolution res = new ScaledResolution(mc);
        int areaTop = (currentTab == Tab.MODULES) ? 80 : 65;
        int areaBottom = this.height - 40;

        for (int i = 0; i < buttonList.size(); i++) {
            GuiButton b = buttonList.get(i);
            if (b.id < 100 || b.id == 800 || b.id == 999) b.drawButton(mc, mouseX, mouseY, partialTicks);
        }

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(0, (this.height - areaBottom) * res.getScaleFactor(), this.width * res.getScaleFactor(), (areaBottom - areaTop) * res.getScaleFactor());
        for (int i = 0; i < buttonList.size(); i++) {
            GuiButton b = buttonList.get(i);
            if (b.id >= 100 && b.id != 800 && b.id != 999) {
                b.y -= scrollOffset;
                if (b.id >= 200 && b.id < 400) b.displayString = getButtonTextById(b.id);
                b.drawButton(mc, mouseX, mouseY, partialTicks);
                b.y += scrollOffset;
            }
        }
        if (currentTab == Tab.COLORS) renderColorPreviews(areaTop, areaBottom);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        drawCenteredString(fontRenderer, "§bBetter F3 Editor", width / 2, 10, 0xFFFFFF);
    }

    private void renderColorPreviews(int top, int bottom) {
        for (int i = 0; i < buttonList.size(); i++) {
            GuiButton b = buttonList.get(i);
            if (b.id >= 500 && b instanceof GuiSlider) {
                int renderY = b.y - scrollOffset;
                if (renderY > top - 35 && renderY < bottom) {
                    int color = Color.HSBtoRGB((float)((GuiSlider)b).getValue(), 0.8f, 1.0f);                    String name = b.displayString.split(":")[0];
                    fontRenderer.drawStringWithShadow(name, b.x + 2, renderY - 11, color | 0xFF000000);
                    drawRect(b.x, renderY + 19, b.x + 150, renderY + 20, color | 0xAA000000);
                    drawRect(b.x + 144, renderY + 4, b.x + 148, renderY + 16, color | 0xFF000000);
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (int i = 0; i < this.buttonList.size(); i++) {
            GuiButton b = this.buttonList.get(i);
            if (b.id >= 100 && b.id != 800 && b.id != 999) {
                b.y -= scrollOffset;
                if (b.mousePressed(this.mc, mouseX, mouseY)) {
                    b.playPressSound(this.mc.getSoundHandler());
                    this.actionPerformed(b);
                    if (i >= this.buttonList.size()) break;
                }
                b.y += scrollOffset;
            } else if (b.mousePressed(this.mc, mouseX, mouseY)) {
                b.playPressSound(this.mc.getSoundHandler());
                this.actionPerformed(b);
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (int i = 0; i < this.buttonList.size(); i++) {
            GuiButton b = this.buttonList.get(i);
            if (b.id >= 100 && b.id != 800 && b.id != 999) {
                b.y -= scrollOffset;
                b.mouseReleased(mouseX, mouseY);
                b.y += scrollOffset;
            } else {
                b.mouseReleased(mouseX, mouseY);
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) { ConfigManager.sync("betterf3reborn", net.minecraftforge.common.config.Config.Type.INSTANCE); mc.displayGuiScreen(null); return; }
        if (button.id == 999) { ModConfig.colors = new ModConfig.Colors(); colorCache.clear(); initGui(); return; }
        if (button.id >= 10 && button.id <= 12) { currentTab = Tab.values()[button.id - 10]; initGui(); return; }
        if (button.id == 800) { togglePerformanceMode(); initGui(); return; }
        if (button.id == 105) { ModConfig.animation.enableAnimation = !ModConfig.animation.enableAnimation; return; }
        if (button.id == 106) { ModConfig.animation.showTextBackground = !ModConfig.animation.showTextBackground; return; }
        if (button.id >= 200 && button.id < 400) toggleModule((button.id < 300) ? leftModules.get(button.id - 200) : rightModules.get(button.id - 300));
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int delta = Mouse.getDWheel();
        if (delta != 0) scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - (delta > 0 ? SCROLL_SPEED : -SCROLL_SPEED)));
    }

    private void setupFieldMapping() {
        // Linker Bereich
        moduleFieldMapping.put(com.worador.f3hud.CoordinatesModule.class, "showCoordinates");
        moduleFieldMapping.put(com.worador.f3hud.SlimeChunkModule.class, "showSlimeDistance");
        moduleFieldMapping.put(com.worador.f3hud.ChunkPosModule.class, "showChunkPos");
        moduleFieldMapping.put(com.worador.f3hud.LightLevelModule.class, "showLightLevel");
        moduleFieldMapping.put(com.worador.f3hud.RotationModule.class, "showRotation");
        moduleFieldMapping.put(com.worador.f3hud.WorldModule.class, "showWorld");
        moduleFieldMapping.put(com.worador.f3hud.DimensionModule.class, "showDimension");
        moduleFieldMapping.put(com.worador.f3hud.TravelModule.class, "showTravelModule");
        moduleFieldMapping.put(com.worador.f3hud.RegionModule.class, "showRegion");
        moduleFieldMapping.put(com.worador.f3hud.TargetedBlockModule.class, "showTargetedBlock");
        moduleFieldMapping.put(com.worador.f3hud.EntitiesModule.class, "showEntities");
        moduleFieldMapping.put(com.worador.f3hud.EntityStatsModule.class, "showEntityStats");
        moduleFieldMapping.put(com.worador.f3hud.CompassModule.class, "showCompass");
        moduleFieldMapping.put(com.worador.f3hud.PerformanceModule.class, "showPerformanceGraph");
        moduleFieldMapping.put(com.worador.f3hud.EndModule.class, "showEndModule");

        // Rechter Bereich
        moduleFieldMapping.put(com.worador.f3hud.SystemModule.class, "showSystem");
        moduleFieldMapping.put(com.worador.f3hud.VersionModule.class, "showVersion");
        moduleFieldMapping.put(com.worador.f3hud.AstralModule.class, "showAstralSorcery");
        moduleFieldMapping.put(com.worador.f3hud.StellarModule.class, "showStellar");
        moduleFieldMapping.put(com.worador.f3hud.BotaniaModule.class, "showBotania");
        moduleFieldMapping.put(com.worador.f3hud.BloodMagicModule.class, "showBloodMagic");
        moduleFieldMapping.put(com.worador.f3hud.ThaumcraftModule.class, "showThaumcraft");
        moduleFieldMapping.put(com.worador.f3hud.HealthAndHungerModule.class, "showHealthStats");
        moduleFieldMapping.put(com.worador.f3hud.OxygenModule.class, "showOxygen");
        moduleFieldMapping.put(com.worador.f3hud.PotionModule.class, "showPotions");
        moduleFieldMapping.put(com.worador.f3hud.DurabilityModule.class, "showDurability");
        moduleFieldMapping.put(com.worador.f3hud.SpeedometerModule.class, "showSpeedometer");
        moduleFieldMapping.put(com.worador.f3hud.WeatherModule.class, "showWeather");
        moduleFieldMapping.put(com.worador.f3hud.GrowthModule.class, "showGrowth");
        moduleFieldMapping.put(com.worador.f3hud.VillagerTradeModule.class, "showVillagerStatus");
        moduleFieldMapping.put(com.worador.f3hud.ItemDespawnModule.class, "showItemDespawn");
        moduleFieldMapping.put(com.worador.f3hud.MachineProgressModule.class, "showMachineProgress");
        moduleFieldMapping.put(com.worador.f3hud.MobAggroModule.class, "showAggroModule");
        moduleFieldMapping.put(com.worador.f3hud.BeaconModule.class, "showBeaconRange");
    }

    private String getButtonTextById(int id) {
        if (id >= 200 && id < 300) return getModuleButtonText(leftModules.get(id - 200));
        if (id >= 300 && id < 400) return getModuleButtonText(rightModules.get(id - 300));
        return "";
    }

    private String getModuleButtonText(InfoModule module) {
        String fieldName = moduleFieldMapping.get(module.getClass());
        if (fieldName == null) return module.getName() + ": §cN/A";
        try {
            Field field = ModConfig.Modules.class.getField(fieldName);
            return module.getName() + ": " + (field.getBoolean(ModConfig.modules) ? "§aON" : "§cOFF");
        } catch (Exception e) { return module.getName() + ": §cERR"; }
    }

    private String getPerformanceButtonText() {
        return "Optimize HUD: " + ((ModConfig.modules.showTravelModule || ModConfig.modules.showLightLevel) ? "§cOFF" : "§aON");
    }

    private String getAnimationButtonText() { return "Animations: " + (ModConfig.animation.enableAnimation ? "§aON" : "§cOFF"); }
    private String getBackgroundButtonText() { return "Text BG: " + (ModConfig.animation.showTextBackground ? "§aON" : "§cOFF"); }

    private void togglePerformanceMode() {
        ModConfig.modules.showTravelModule = false;
        ModConfig.modules.showLightLevel = false;
        ModConfig.modules.showDetailedChunks = false;
        ConfigManager.sync("betterf3reborn", net.minecraftforge.common.config.Config.Type.INSTANCE);
    }

    private void toggleModule(InfoModule module) {
        String fieldName = moduleFieldMapping.get(module.getClass());
        if (fieldName == null) return;
        try {
            Field field = ModConfig.Modules.class.getField(fieldName);
            field.setBoolean(ModConfig.modules, !field.getBoolean(ModConfig.modules));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override public boolean doesGuiPauseGame() { return false; }
}