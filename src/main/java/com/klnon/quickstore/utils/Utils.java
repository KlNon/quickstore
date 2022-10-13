package com.klnon.quickstore.utils;

import com.klnon.quickstore.QuickStore;
import com.klnon.quickstore.config.StoreConfig;
import com.klnon.quickstore.gui.render.Render;
import com.klnon.quickstore.gui.render.RenderEnqueue;
import com.klnon.quickstore.keybinding.KeyBindings;
import com.klnon.quickstore.model.BlockData;
import com.klnon.quickstore.model.ContainerInformation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static BlockData blockData;
    private static boolean quickSee = false; // Off by default
    private static Vector3i lastPlayerPos = null;
    public static final String MOD_ID = "quickstore";

    @OnlyIn(value = Dist.CLIENT)
    public static PlayerEntity clientPlayer;
    public static ServerPlayerEntity serverPlayer;

    public static final String PREFIX_GUI = String.format("%s:textures/gui/", MOD_ID);

    private static final int[] distanceList = new int[] {2, 4, 6, 8, 12, 16, 24, 30};

    public static ArrayList<Item> blackList = new ArrayList<Item>() {{
        add(Items.AIR);
        add(Items.BEDROCK);
        add(Items.STONE);
        add(Items.GRASS);
        add(Items.DIRT);
    }};

    public static BlockData getBlockData() {
        return blockData;
    }

    public static void setBlockData(BlockData blockData) {
        Utils.blockData = blockData;
    }

    public static double blockDistance(BlockPos positionA, BlockPos positionB) {
        //TODO 距离不知道对不对
        return positionA.distanceSq(positionB.getX(), positionB.getY(), positionB.getZ(),false);
    }

    public static void sendCommand() {
        (Minecraft.getInstance()).player.sendChatMessage("/quickstore");
    }

    public static ServerPlayerEntity getSPlayer() {
        return serverPlayer;
    }

    public static void setSPlayer(ServerPlayerEntity serverPlayer) {
        Utils.serverPlayer =serverPlayer;
    }

    @OnlyIn(value = Dist.CLIENT)
    public static PlayerEntity getCPlayer() {
        return clientPlayer;
    }

    @OnlyIn(value = Dist.CLIENT)
    public static void setCPlayer(PlayerEntity clientPlayer) {
        Utils.clientPlayer = clientPlayer;
    }

    public static int getRadius() { return distanceList[StoreConfig.general.distance.get()]; }

    public static boolean isKeyPressed() {
        return KeyBindings.toggleStore.getKeyBinding().isPressed();
    }

    public static boolean isKeyDown() {
        return KeyBindings.toggleStore.getKeyBinding().isKeyDown();
    }


    public static boolean isQuickSeeActive() {
        return quickSee && Minecraft.getInstance().world != null && Minecraft.getInstance().player != null;
    }

    private static boolean playerHasMoved() {
        if (Minecraft.getInstance().player == null)
            return false;

        return lastPlayerPos == null
                || lastPlayerPos.getX() != Minecraft.getInstance().player.getPosition().getX()
                || lastPlayerPos.getY() != Minecraft.getInstance().player.getPosition().getY()
                || lastPlayerPos.getZ() != Minecraft.getInstance().player.getPosition().getZ();
    }

    private static void updatePlayerPosition()
    {
        assert Minecraft.getInstance().player != null;
        lastPlayerPos = Minecraft.getInstance().player.getPosition();
    }

    public static synchronized void requestBlockFinder( boolean force )
    {
        if ( isQuickSeeActive() && (force || playerHasMoved()) ) // world/player check done by xrayActive()
        {
            updatePlayerPosition(); // since we're about to run, update the last known position
            Region region = new Region( lastPlayerPos, getRadius() ); // the region to scan for syncRenderList
            Util.getServerExecutor().execute(new RenderEnqueue(region));
        }
    }

    @OnlyIn(value = Dist.CLIENT)
    public static void toggleQuickSee()
    {
        Utils.clientPlayer=Minecraft.getInstance().player;
        if ( !quickSee) // enable drawing
        {
            Render.syncRenderList.clear(); // first, clear the buffer
            quickSee = true; // then, enable drawing
            requestBlockFinder( true ); // finally, force a refresh

            if( !StoreConfig.general.showOverlay.get() && Minecraft.getInstance().player != null )
                Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("quickstore.toggle.activated"), false);
        }
        else // disable drawing
        {
            if( !StoreConfig.general.showOverlay.get() && Minecraft.getInstance().player != null )
                Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("quickstore.toggle.deactivated"), false);

            quickSee = false;
        }
    }


    public static List<ContainerInformation> getNearbyContainers(ServerPlayerEntity player) {
        BlockPos playerPosition = player.getPosition();
        List<ChestTileEntity> chests = new ArrayList<>();
        //TODO 可能有误
        double range = distanceList[StoreConfig.general.distance.get()];
        for (TileEntity tileEntity : (player.getEntityWorld()).loadedTileEntityList) {
            if (tileEntity instanceof ChestTileEntity )
//                && blockDistance(playerPosition, tileEntity.getPos()) < range
                if(Math.abs(playerPosition.getX()-tileEntity.getPos().getX())<=range
                        &&Math.abs(playerPosition.getY()-tileEntity.getPos().getY())<=range
                        &&Math.abs(playerPosition.getZ()-tileEntity.getPos().getZ())<=range){
                    chests.add((ChestTileEntity) tileEntity);
                }
        }
        List<ContainerInformation> containers = new ArrayList<>();
        for (TileEntity tileEntity : (player.getEntityWorld()).loadedTileEntityList) {
            if (tileEntity instanceof IInventory && !(tileEntity instanceof ChestTileEntity)) {
                if(Math.abs(playerPosition.getX()-tileEntity.getPos().getX())<=range
                        &&Math.abs(playerPosition.getY()-tileEntity.getPos().getY())<=range
                        &&Math.abs(playerPosition.getZ()-tileEntity.getPos().getZ())<=range) {
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
        }
        for (ChestTileEntity chest : chests) {
            ContainerInformation ci = new ContainerInformation();
            ci.ignoredSlot = -1000;
            ci.chest1 = chest;
            containers.add(ci);
        }
        return containers;
    }
}
