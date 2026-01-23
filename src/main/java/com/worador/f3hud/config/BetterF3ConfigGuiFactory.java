package com.worador.f3hud.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.ConfigManager;
import com.worador.f3hud.ModConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BetterF3ConfigGuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new BetterF3ConfigGui(parentScreen);
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    public static class BetterF3ConfigGui extends GuiConfig {
        public BetterF3ConfigGui(GuiScreen parentScreen) {
            super(parentScreen,
                    getConfigElements(),
                    "betterf3reborn",
                    false,
                    false,
                    "Better F3 Reborn Configuration");
        }

        private static List<IConfigElement> getConfigElements() {
            List<IConfigElement> list = new java.util.ArrayList<>();

            // Wir greifen direkt auf die .cfg Datei zu.
            // Forge hat diese bereits erstellt, wir lesen sie hier nur für das Menü aus.
            Configuration config = new Configuration(new java.io.File("config/betterf3reborn.cfg"));

            // Wir fügen die Kategorien hinzu, die wir in deiner ModConfig.java definiert haben.
            // WICHTIG: Die Namen müssen exakt mit @Config.Name übereinstimmen (meist kleingeschrieben im File)
            list.add(new ConfigElement(config.getCategory("modules")));
            list.add(new ConfigElement(config.getCategory("colors")));
            list.add(new ConfigElement(config.getCategory("position")));
            list.add(new ConfigElement(config.getCategory("animation")));

            return list;
        }
    }
}