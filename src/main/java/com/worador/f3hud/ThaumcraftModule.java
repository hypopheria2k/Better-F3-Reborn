package com.worador.f3hud;

import com.worador.f3hud.InfoModule;
import com.worador.f3hud.ModConfig;
import com.worador.f3hud.compat.ThaumcraftCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ThaumcraftModule extends InfoModule {

    @Override
    public String getName() {
        return "Thaumcraft";
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();

        // Prüfung, ob das Modul in der Config aktiviert ist
        if (!ModConfig.modules.showThaumcraft) {
            return lines;
        }

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        World world = mc.world;

        if (player != null && world != null) {
            // Aufruf der Compat-Klasse für Vis und Flux
            lines.addAll(ThaumcraftCompat.getThaumcraftLines(player, world));
        }

        return lines;
    }
}