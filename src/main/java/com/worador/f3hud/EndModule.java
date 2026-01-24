package com.worador.f3hud;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EndModule extends InfoModule {

    @Override
    public String getName() {
        return "End Survival";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showEnd;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();
        // Nur im End aktiv (Dimension 1)
        if (mc.player == null || mc.world == null || mc.world.provider.getDimension() != 1) return lines;

        long time = System.currentTimeMillis();

        // 1. VOID ALARM (Kritisch unter Y=40 oder bei schnellem Fall)
        double y = mc.player.posY;
        double motionY = mc.player.motionY;

        if (y < 50.0) {
            int blink = (time / 200) % 2 == 0 ? 0xFFFFFF : 0xFF5555;
            String warning = y < 30.0 ? "!!! VOID !!!" : "Low Altitude";
            lines.add(new InfoLine(warning, String.format(Locale.US, "Y: %.1f (Fall: %.2f)", y, motionY), blink));
        }

        // 2. FLUG-STATUS (Elytra / Jetpacks / Morph)
        // Zeigt Speed nur an, wenn der Spieler sich wirklich schnell bewegt (über 5 m/s)
        double speed = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ) * 20.0;
        if (speed > 5.0) {
            lines.add(new InfoLine("Speed: ", String.format(Locale.US, "%.1f m/s", speed), 0x55FF55));
        }

        // 3. GATEWAY FINDER (Wichtig für die Rückkehr)
        // Nur alle 20 Ticks scannen, um CPU zu sparen
        if (mc.player.ticksExisted % 20 == 0 || mc.player.ticksExisted < 5) {
            BlockPos nearestGateway = null;
            double minDistSq = 16384; // 128 Blöcke Radius

            for (TileEntity te : mc.world.loadedTileEntityList) {
                if (te instanceof TileEntityEndGateway) {
                    double d = mc.player.getDistanceSq(te.getPos());
                    if (d < minDistSq) {
                        minDistSq = d;
                        nearestGateway = te.getPos();
                    }
                }
            }

            if (nearestGateway != null) {
                int dist = (int)Math.sqrt(minDistSq);
                lines.add(new InfoLine("Exit: ", dist + "m " + getDirectionArrow(new BlockPos(mc.player), nearestGateway), 0x55FFFF));
            }
        }

        return lines;
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
}