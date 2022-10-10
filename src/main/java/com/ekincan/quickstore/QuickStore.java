package com.ekincan.quickstore;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = "quickstore", name = "QuickStore", version = "1.0", acceptedMinecraftVersions = "[1.12.2]")
public class QuickStore {
    public static final String MODID = "quickstore";

    public static final String NAME = "QuickStore";

    public static final String VERSION = "1.0";

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("quickstore");

    public static EntityPlayer player;

    public static boolean keyIsDown = false;

    public static float nextUpdateCooldown = 0.5F;

    public static float lostItemsCheckCooldown;

    public static int leftItemChecks = 0;

    public static List<Item> currentItems;

    public static List<ContainerInformation> nearbyContainers;

    public static List<Item> spawnItems;

    public static List<EntityItem> flyingItems;

    public static List<Vec3d> flyingTargets;

    public static List<ContainerInformation> flyingContainers;

    public static float spawnItemCooldown;

    @SidedProxy(clientSide = "com.ekincan.quickstore.ProxyClient", serverSide = "com.ekincan.quickstore.ProxyServer")
    public static Proxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(this);
        FMLCommonHandler.instance().bus().register(proxy);
        proxy.preInit();
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        proxy.serverLoad();
        event.registerServerCommand((ICommand)new QuickStoreCommand());
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (proxy.isKeyPressed() && !keyIsDown)
            proxy.onKeyPressed();
    }

    public static List<ContainerInformation> getNearbyContainers(EntityPlayer player, float rangeBonus) {
        BlockPos playerPosition = player.getPosition();
        List<TileEntityChest> chests = new ArrayList<>();
        double range = EntityPlayer.REACH_DISTANCE.getDefaultValue() + rangeBonus;
        for (TileEntity tileEntity : (player.getEntityWorld()).loadedTileEntityList) {
            if (tileEntity instanceof TileEntityChest && blockDistance(playerPosition, tileEntity.getPos()) < range)
                chests.add((TileEntityChest)tileEntity);
        }
        List<TileEntityChest> ignoredChests = new ArrayList<>();
        List<ContainerInformation> containers = new ArrayList<>();
        for (TileEntity tileEntity : (player.getEntityWorld()).loadedTileEntityList) {
            //TODO 可能有误
            if (tileEntity instanceof IInventory && !(tileEntity instanceof TileEntityChest) && blockDistance(playerPosition, tileEntity.getPos()) < range) {
                ContainerInformation ci = new ContainerInformation();
                ci.inventoryObject = (IInventory)tileEntity;
                ci.blockPositionOfInventory = tileEntity;
                if (tileEntity instanceof net.minecraft.tileentity.TileEntityFurnace) {
                    ci.ignoredSlot = 2;
                } else {
                    ci.ignoredSlot = -1000;
                }
                containers.add(ci);
            }
        }
        for (TileEntityChest chest : chests) {
            if (!ignoredChests.contains(chest)) {
                boolean addThis = true;
                ContainerInformation ci = new ContainerInformation();
                ci.ignoredSlot = -1000;
                ci.chest1 = chest;
                chest.checkForAdjacentChests();
                if (chest.adjacentChestXNeg != null) {
                    addThis = false;
                } else if (chest.adjacentChestXPos != null) {
                    ci.chest2 = chest.adjacentChestXPos;
                } else if (chest.adjacentChestZNeg!= null) {
                    addThis = false;
                } else if (chest.adjacentChestZPos != null) {
                    ci.chest2 = chest.adjacentChestZPos;
                }
                if (addThis) {
                    if (ci.chest2 != null)
                        ignoredChests.add(ci.chest2);
                    containers.add(ci);
                }
            }
        }
        return containers;
    }

    public static List<Item> getCurrentItems(EntityPlayer player) {
        InventoryPlayer inventory = player.inventory;
        int inventorySize = inventory.getSizeInventory();
        List<Item> playerItems = new ArrayList<>();
        for (int n = 0; n < inventorySize; n++) {
            ItemStack stack = player.inventory.getStackInSlot(n);
            //TODO getCount 可能有误
            if (stack != null && stack.getMaxStackSize() > 1 && stack.getCount() > 0) {
                Item item = stack.getItem();
                if (Item.getIdFromItem(item) != 0)
                    playerItems.add(item);
            }
        }
        return playerItems;
    }

    public static double blockDistance(BlockPos positionA, BlockPos positionB) {
        return positionA.getDistance(positionB.getX(), positionB.getY(), positionB.getZ());
    }
}
