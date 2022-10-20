package com.klnon.quickstore.utils;

import com.klnon.quickstore.QuickStore;
import com.klnon.quickstore.config.StoreConfig_Server;
import com.klnon.quickstore.gui.model.ItemStore;
import com.klnon.quickstore.gui.render.Render;
import com.klnon.quickstore.gui.render.RenderBlockProps;
import com.klnon.quickstore.model.BlockData;
import com.klnon.quickstore.model.ContainerInformation;
import com.klnon.quickstore.networking.Networking;
import com.klnon.quickstore.networking.StoreToServerPack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils_Server {

    public static ServerPlayerEntity serverPlayer;

    private static boolean quickSee = false; // Off by default

    public static final String PREFIX_GUI = String.format("%s:textures/gui/", QuickStore.MOD_ID);

    public static final int[] distanceList = new int[]{2, 4, 6, 8, 12, 16, 24, 30};


    //储存完成储存后箱子的位置
    public static List<RenderBlockProps> storedList = Collections.synchronizedList( new ArrayList<>() ); // this is accessed by threads

    public static List<RenderBlockProps> searchList = Collections.synchronizedList( new ArrayList<>() );

    public static ServerPlayerEntity getSPlayer() {
        return serverPlayer;
    }

    public static void setSPlayer(ServerPlayerEntity serverPlayer) {
        Utils_Server.serverPlayer = serverPlayer;
    }

    public static void sendCommand() {
        Render.syncRenderList.clear();
        Utils_Server.searchList.clear();
        Utils_Server.storedList.clear();
        Utils_Server.getItems().clear();
        //刷新生成的箱子
        assert (Minecraft.getInstance()).player != null;
        (Minecraft.getInstance()).player.sendChatMessage("/quickstore");

        Utils_Client.requestBlockFinder(true); //refresh
    }


    public static void sendSearchCommand() {
        Render.syncRenderList.clear();
        Utils_Server.storedList.clear();
        Utils_Server.searchList.clear();
        Utils_Server.getItems().clear();
        //将本地储存的要搜索的物品发送给服务器
        if(FMLEnvironment.dist.isClient())
            for(ItemStack item : ItemStore.getItemStack()){
                Networking.INSTANCE.sendToServer(new StoreToServerPack(item));
            }
        //刷新生成的箱子
        assert (Minecraft.getInstance()).player != null;
        (Minecraft.getInstance()).player.sendChatMessage("/quicksearch");

        Utils_Client.requestBlockFinder(true); //refresh
    }

    public static boolean isQuickSeeActive() {
        return quickSee && Minecraft.getInstance().world != null && Minecraft.getInstance().player != null;
    }

    public static List<ContainerInformation> getNearbyContainers(ServerPlayerEntity player,double range) {
        BlockPos playerPosition = player.getPosition();
        List<ChestTileEntity> chests = new ArrayList<>();

        List<ContainerInformation> containers = new ArrayList<>();
        for (TileEntity tileEntity : (player.getEntityWorld()).loadedTileEntityList) {
            if (tileEntity instanceof ChestTileEntity)
                if (Math.abs(playerPosition.getX() - tileEntity.getPos().getX()) <= range
                && Math.abs(playerPosition.getY() - tileEntity.getPos().getY()) <= range
                && Math.abs(playerPosition.getZ() - tileEntity.getPos().getZ()) <= range) {
                    chests.add((ChestTileEntity) tileEntity);
                }
            if (tileEntity instanceof IInventory
            && !(tileEntity instanceof ChestTileEntity)
            && ((IInventory) tileEntity).getSizeInventory() > StoreConfig_Server.general.checkSlot.get()) {
                if (Math.abs(playerPosition.getX() - tileEntity.getPos().getX()) <= range
                && Math.abs(playerPosition.getY() - tileEntity.getPos().getY()) <= range
                && Math.abs(playerPosition.getZ() - tileEntity.getPos().getZ()) <= range) {
                    ContainerInformation ci = new ContainerInformation();
                    ci.inventoryObject = (IInventory) tileEntity;
                    ci.blockPositionOfInventory = tileEntity;
                    containers.add(ci);
                }
            }
        }
        for (ChestTileEntity chest : chests) {
            ContainerInformation ci = new ContainerInformation();
            ci.chest1 = chest;
            containers.add(ci);
        }
        return containers;
    }

    //------------------------------------------------用于Gui-----------------------------------------------

    private static List<ItemStack> items = new ArrayList<>();

    public static List<ItemStack> getItems() {
        return items;
    }

    public static void setItems(List<ItemStack> items) {
        Utils_Server.items = items;
    }
}
