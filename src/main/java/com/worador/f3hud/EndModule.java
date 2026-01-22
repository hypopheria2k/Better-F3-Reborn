package com.worador.f3hud;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EndModule extends InfoModule {

    @Override
    public String getName() {
        return "End Exploration";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showEndModule;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        if (mc.player == null || mc.world == null || mc.world.provider.getDimension() != 1) return lines;

        long time = System.currentTimeMillis();

        // 1. VOID & MOMENTUM ALARM
        double y = mc.player.posY;
        double motionY = mc.player.motionY;

        if (y < 40.0) {
            int blink = (time / 200) % 2 == 0 ? 0xFFFFFF : 0xFF5555;
            lines.add(new InfoLine("!!! VOID RISK !!!", String.format(Locale.US, "Y: %.1f | Falling: %.2f", y, motionY), blink));
        }

        // 2. FLUG-VEKTOR (Elytra Status)
        if (mc.player.isElytraFlying()) {
            double speed = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ) * 20.0;
            int speedColor = speed < 15.0 ? 0xFFAA00 : 0x55FF55;
            lines.add(new InfoLine("Flight Speed: ", String.format(Locale.US, "%.1f m/s", speed), speedColor));
        }

        // 3. END-CITY & GATEWAY RADAR
        int chunkX = mc.player.chunkCoordX;
        int chunkZ = mc.player.chunkCoordZ;
        if (isPotentialCityChunk(chunkX, chunkZ)) {
            lines.add(new InfoLine("Structure: ", TextFormatting.GOLD + "END CITY CHUNK", 0xFFAA00));
        }

        // Gateway Finder
        BlockPos nearestGateway = null;
        double minDist = Double.MAX_VALUE;
        for (TileEntity te : mc.world.loadedTileEntityList) {
            if (te instanceof TileEntityEndGateway) {
                double d = mc.player.getDistanceSq(te.getPos());
                if (d < minDist) {
                    minDist = d;
                    nearestGateway = te.getPos();
                }
            }
        }
        if (nearestGateway != null) {
            lines.add(new InfoLine("Nearest Gateway: ", (int)Math.sqrt(minDist) + "m " + getDirectionArrow(new BlockPos(mc.player), nearestGateway), 0x55FFFF));
        }

        // 4. COMBAT TRACKER (Drache & Shulker)
        int shulkerBullets = 0;
        int crystals = 0;
        boolean inDragonBreath = false;

        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityShulkerBullet) shulkerBullets++;
            if (e instanceof EntityEnderCrystal) crystals++;

            // FIX: Namensbasierter Check statt Klassen-Import
            String className = e.getClass().getSimpleName();
            if ((className.equals("EntityAreaEffectCloud") || className.equals("AreaEffectCloud")) && e.getDistanceSq(mc.player) < 9.0) {
                inDragonBreath = true;
            }
        }

        if (shulkerBullets > 0) {
            lines.add(new InfoLine("Shulker Target: ", shulkerBullets + " Bullets locked!", 0xFF55FF));
        }
        if (crystals > 0) {
            lines.add(new InfoLine("Dragon Status: ", crystals + " Crystals remaining", 0xFF5555));
        }
        if (inDragonBreath) {
            int blink = (time / 150) % 2 == 0 ? 0xFF00FF : 0xAA00AA;
            lines.add(new InfoLine("!!! BREATH !!!", "EXIT CLOUD NOW", blink));
        }

        return lines;
    }

    private boolean isPotentialCityChunk(int cx, int cz) {
        return Math.abs(cx) % 20 == 0 && Math.abs(cz) % 20 == 0 && (cx != 0 || cz != 0);
    }

    private String getDirectionArrow(BlockPos player, BlockPos target) {
        double angle = Math.atan2(target.getZ() - player.getZ(), target.getX() - player.getX()) * 180 / Math.PI;
        double relativeAngle = (angle - mc.player.rotationYaw + 90) % 360;
        if (relativeAngle < 0) relativeAngle += 360;
        if (relativeAngle >= 315 || relativeAngle < 45) return "↑";
        if (relativeAngle >= 45 && relativeAngle < 135) return "→";
        if (relativeAngle >= 135 && relativeAngle < 225) return "↓";
        return "←";
    }

    @Override
    public int getHeight() {
        return 11 * 6 + 2;
    }
}