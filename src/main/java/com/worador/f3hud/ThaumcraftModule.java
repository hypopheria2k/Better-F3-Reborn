package com.worador.f3hud;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader; // WICHTIG

import java.util.ArrayList;
import java.util.List;

// IMPORT VON ThaumcraftCompat ENTFERNEN!

public class ThaumcraftModule extends InfoModule {

    @Override
    public String getName() {
        return "Thaumcraft";
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();

        // Prüfung, ob das Modul in der Config aktiviert ist UND ob die Mod überhaupt existiert
        if (!ModConfig.modules.showThaumcraft || !Loader.isModLoaded("thaumcraft")) {
            return lines;
        }

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        World world = mc.world;

        if (player != null && world != null) {
            // Vollqualifizierter Aufruf, um den Classloader erst bei Bedarf zu triggern
            lines.addAll(com.worador.f3hud.compat.ThaumcraftCompat.getThaumcraftLines(player, world));
        }

        return lines;
    }
}