package com.klnon.quickstore.utils;

import com.klnon.quickstore.config.StoreConfig;
import com.klnon.quickstore.container.ContainerInformation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KlNon
 * @version 1.0
 * @className Utils
 * @description
 * @date 2022/10/11 10:31
 **/
public class Utils {

    public static String MOD_ID = "quickstore";
    
    public static List<ContainerInformation> getNearbyContainers(PlayerEntity player, double rangeBonus) {
        BlockPos playerPosition = player.getPosition();
        List<ChestTileEntity> chests = new ArrayList<>();
        //TODO 可能有误
        double range = PlayerEntity.getRenderDistanceWeight() + rangeBonus;
        for (TileEntity tileEntity : (player.getEntityWorld()).loadedTileEntityList) {
            if (tileEntity instanceof ChestTileEntity && blockDistance(playerPosition, tileEntity.getPos()) < range)
                chests.add((ChestTileEntity) tileEntity);
        }
        List<ChestTileEntity> ignoredChests = new ArrayList<>();
        List<ContainerInformation> containers = new ArrayList<>();
        for (TileEntity tileEntity : (player.getEntityWorld()).loadedTileEntityList) {
            if (tileEntity instanceof IInventory && !(tileEntity instanceof ChestTileEntity) && blockDistance(playerPosition, tileEntity.getPos()) < range) {
                ContainerInformation ci = new ContainerInformation();
                ci.inventoryObject = (IInventory) tileEntity;
                ci.blockPositionOfInventory = tileEntity;
                //为熔炉的时候
                if (tileEntity instanceof net.minecraft.tileentity.FurnaceTileEntity) {
                    ci.ignoredSlot = 2;
                } else {
                    ci.ignoredSlot = -1000;
                }
                containers.add(ci);
            }
        }
        for (ChestTileEntity chest : chests) {
            if (!ignoredChests.contains(chest)) {
                boolean addThis = true;
                ContainerInformation ci = new ContainerInformation();
                ci.ignoredSlot = -1000;
                ci.chest1 = chest;
//                //TODO 检查相邻的箱子
//                chest.checkForAdjacentChests();
//                if (chest.adjacentChestXNeg != null) {
//                    addThis = false;
//                } else if (chest.adjacentChestXPos != null) {
//                    ci.chest2 = chest.adjacentChestXPos;
//                } else if (chest.adjacentChestZNeg != null) {
//                    addThis = false;
//                } else if (chest.adjacentChestZPos != null) {
//                    ci.chest2 = chest.adjacentChestZPos;
//                }
                if (addThis) {
                    if (ci.chest2 != null)
                        ignoredChests.add(ci.chest2);
                    containers.add(ci);
                }
            }
        }
        return containers;
    }

    public static double blockDistance(BlockPos positionA, BlockPos positionB) {
        //TODO 距离不知道对不对
        return positionA.distanceSq(positionB.getX(), positionB.getY(), positionB.getZ(),false);
    }

}
