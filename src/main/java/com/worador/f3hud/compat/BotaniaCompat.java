package com.worador.f3hud.compat;

import com.worador.f3hud.InfoModule.InfoLine;
import com.worador.f3hud.ModConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.mana.IManaSpreader;
import vazkii.botania.api.subtile.ISubTileContainer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BotaniaCompat {

    public static List<InfoLine> getManaLines(EntityPlayer player, World world) {
        List<InfoLine> lines = new ArrayList<>();

        if (player == null || world == null) return lines;

        // 1. Player Mana Items Count
        int manaItemCount = countManaItems(player);
        if (manaItemCount > 0) {
            lines.add(new InfoLine("Mana Items:  ", String.valueOf(manaItemCount), ModConfig.colors.colorBotania));
        }

        // 2. Player Mana (Items & Baubles)
        long currentMana = 0;
        long maxMana = 0;

        try {
            List<ItemStack> mainInv = ManaItemHandler.getManaItems(player);
            if (mainInv != null) {
                for (ItemStack stack : mainInv) {
                    if (!stack.isEmpty() && stack.getItem() instanceof IManaItem) {
                        IManaItem item = (IManaItem) stack.getItem();
                        currentMana += item.getMana(stack);
                        maxMana += item.getMaxMana(stack);
                    }
                }
            }

            Map<Integer, ItemStack> baubles = ManaItemHandler.getManaBaubles(player);
            if (baubles != null) {
                for (ItemStack stack : baubles.values()) {
                    if (!stack.isEmpty() && stack.getItem() instanceof IManaItem) {
                        IManaItem item = (IManaItem) stack.getItem();
                        currentMana += item.getMana(stack);
                        maxMana += item.getMaxMana(stack);
                    }
                }
            }

            if (maxMana > 0) {
                double pct = (double) currentMana / maxMana * 100.0;
                lines.add(new InfoLine("Player Mana: ", String.format("%d / %d (%.1f%%)", currentMana, maxMana, pct), ModConfig.colors.colorBotania));
            }
        } catch (Exception e) {
            // Fehler ignorieren
        }

        // 3. Raytrace (Pool, Spreader, Flowers)
        try {
            RayTraceResult rtr = player.rayTrace(5.0D, 1.0F);

            if (rtr != null && rtr.typeOfHit == RayTraceResult.Type.BLOCK) {
                TileEntity te = world.getTileEntity(rtr.getBlockPos());

                if (te != null) {
                    // Pool & Receiver Logik
                    if (te instanceof IManaReceiver) {
                        IManaReceiver receiver = (IManaReceiver) te;

                        int pCurrent = receiver.getCurrentMana();
                        int pMax = getMaxManaFromTile(te);

                        if (pMax > 0) {
                            double pPct = (double) pCurrent / pMax * 100.0;
                            lines.add(new InfoLine("Pool Mana: ", String.format("%d / %d (%.1f%%)", pCurrent, pMax, pPct), ModConfig.colors.colorBotania));
                        }

                        // Pool Color
                        if (te instanceof IManaPool) {
                            IManaPool pool = (IManaPool) te;
                            EnumDyeColor color = pool.getColor();
                            if (color != null) {
                                String colorName = color.getName();
                                colorName = colorName.substring(0, 1).toUpperCase() + colorName.substring(1);
                                lines.add(new InfoLine("Pool Color: ", colorName, getColorForDye(color)));
                            }
                        }

                        // Pool Status
                        if (te instanceof IManaPool) {
                            IManaPool pool = (IManaPool) te;
                            boolean outputting = pool.isOutputtingPower();
                            String status = outputting ? "Outputting" : "Accepting";
                            int statusColor = outputting ? 0xFF5555 : 0x55FF55;
                            lines.add(new InfoLine("Pool Status: ", status, statusColor));
                        }
                    }

                    // --- Spreader Binding via Reflection ---
                    if (te instanceof IManaSpreader) {
                        try {
                            // Wir suchen das Feld "pinPos", das in Botania-Spreadern die Zielkoordinaten hält
                            Field pinField = findField(te.getClass(), "pinPos");
                            if (pinField != null) {
                                pinField.setAccessible(true);
                                BlockPos target = (BlockPos) pinField.get(te);
                                if (target != null) {
                                    String targetCoord = String.format("%d, %d, %d", target.getX(), target.getY(), target.getZ());
                                    lines.add(new InfoLine("Spreader Target: ", targetCoord, ModConfig.colors.colorBotania));
                                }
                            }
                        } catch (Exception e) {
                            // Fehler ignorieren
                        }
                    }

                    // --- Flower Status (SubTiles) ---
                    if (te instanceof ISubTileContainer) {
                        ISubTileContainer container = (ISubTileContainer) te;
                        Object subTile = container.getSubTile();
                        if (subTile != null) {
                            String flowerName = subTile.getClass().getSimpleName();
                            lines.add(new InfoLine("Flower: ", flowerName, ModConfig.colors.colorBotania));

                            // BurnTime via Reflection
                            try {
                                Field burnField = findField(subTile.getClass(), "burnTime");
                                if (burnField != null) {
                                    burnField.setAccessible(true);
                                    int burnTime = (int) burnField.get(subTile);
                                    if (burnTime > 0) {
                                        String timeStr = String.format("%d Ticks (%.1fs)", burnTime, burnTime / 20.0);
                                        lines.add(new InfoLine("Burn Time: ", timeStr, 0xFFAA00));
                                    }
                                }
                            } catch (Exception e) {
                                // Fehler ignorieren
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Fehler ignorieren
        }

        return lines;
    }

    private static int countManaItems(EntityPlayer player) {
        int count = 0;
        try {
            List<ItemStack> mainInv = ManaItemHandler.getManaItems(player);
            if (mainInv != null) {
                for (ItemStack stack : mainInv) {
                    if (!stack.isEmpty() && stack.getItem() instanceof IManaItem) {
                        count += stack.getCount();
                    }
                }
            }
            Map<Integer, ItemStack> baubles = ManaItemHandler.getManaBaubles(player);
            if (baubles != null) {
                for (ItemStack stack : baubles.values()) {
                    if (!stack.isEmpty() && stack.getItem() instanceof IManaItem) {
                        count += stack.getCount();
                    }
                }
            }
        } catch (Exception e) { }
        return count;
    }

    private static int getColorForDye(EnumDyeColor color) {
        switch (color) {
            case WHITE: return 0xFFFFFF;
            case ORANGE: return 0xFF7F00;
            case MAGENTA: return 0xFF00FF;
            case LIGHT_BLUE: return 0x00FFFF;
            case YELLOW: return 0xFFFF00;
            case LIME: return 0x00FF00;
            case PINK: return 0xFF69B4;
            case GRAY: return 0x808080;
            case SILVER: return 0xC0C0C0;
            case CYAN: return 0x00FFFF;
            case PURPLE: return 0x800080;
            case BLUE: return 0x0000FF;
            case BROWN: return 0x8B4513;
            case GREEN: return 0x008000;
            case RED: return 0xFF0000;
            case BLACK: return 0x000000;
            default: return 0xFFFFFF;
        }
    }

    private static int getMaxManaFromTile(TileEntity tile) {
        try {
            Method method = findGetMaxManaMethod(tile.getClass());
            if (method != null) {
                Object result = method.invoke(tile);
                if (result instanceof Integer) return (Integer) result;
            }
        } catch (Exception e) { }
        return 1000000;
    }

    private static Method findGetMaxManaMethod(Class<?> clazz) {
        if (clazz == null || clazz == Object.class) return null;
        try {
            return clazz.getDeclaredMethod("getMaxMana");
        } catch (NoSuchMethodException e) {
            return findGetMaxManaMethod(clazz.getSuperclass());
        } catch (Exception e) {
            return null;
        }
    }

    private static Field findField(Class<?> clazz, String fieldName) {
        while (clazz != null && clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
} 
