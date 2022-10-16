package com.klnon.quickstore.utils;


import com.klnon.quickstore.config.StoreConfig_Client;
import com.klnon.quickstore.config.StoreConfig_Server;
import com.klnon.quickstore.gui.render.Render;
import com.klnon.quickstore.gui.render.RenderEnqueue;
import com.klnon.quickstore.keybinding.KeyBindings;
import com.klnon.quickstore.model.BlockData;
import com.klnon.quickstore.model.Region;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Utils_Client {

    @OnlyIn(value = Dist.CLIENT)
    public static PlayerEntity clientPlayer;
    @OnlyIn(value = Dist.CLIENT)
    public static BlockData getBlockData() {
        return blockData;
    }
    @OnlyIn(value = Dist.CLIENT)
    private static boolean quickSee = false; // Off by default
    @OnlyIn(value = Dist.CLIENT)
    private static BlockData blockData;
    @OnlyIn(value = Dist.CLIENT)
    private static Vector3i lastPlayerPos = null;


    @OnlyIn(value = Dist.CLIENT)
    public static PlayerEntity getCPlayer() {
        return clientPlayer;
    }

    @OnlyIn(value = Dist.CLIENT)
    public static void setCPlayer(PlayerEntity clientPlayer) {
        Utils_Client.clientPlayer = clientPlayer;
    }


    @OnlyIn(value = Dist.CLIENT)
    public static boolean isQuickSeeActive() {
        return quickSee && Minecraft.getInstance().world != null && Minecraft.getInstance().player != null;
    }


    @OnlyIn(value = Dist.CLIENT)
    public static double blockDistance(BlockPos positionA, BlockPos positionB) {
        //TODO 距离不知道对不对
        return positionA.distanceSq(positionB.getX(), positionB.getY(), positionB.getZ(), false);
    }

    @OnlyIn(value = Dist.CLIENT)
    public static void setBlockData(BlockData blockData) {
        Utils_Client.blockData = blockData;
    }

    @OnlyIn(value = Dist.CLIENT)
    public static int getRadius() {
        return Utils_Server.distanceList[StoreConfig_Server.general.distance.get()];
    }

    @OnlyIn(value = Dist.CLIENT)
    public static boolean isKeyPressed() {
        return KeyBindings.toggleStore.getKeyBinding().isPressed();
    }

    @OnlyIn(value = Dist.CLIENT)
    public static boolean isKeyDown() {
        return KeyBindings.toggleStore.getKeyBinding().isKeyDown();
    }

    @OnlyIn(value = Dist.CLIENT)
    private static boolean playerHasMoved() {
        if (Minecraft.getInstance().player == null)
            return false;

        return lastPlayerPos == null
                || lastPlayerPos.getX() != Minecraft.getInstance().player.getPosition().getX()
                || lastPlayerPos.getY() != Minecraft.getInstance().player.getPosition().getY()
                || lastPlayerPos.getZ() != Minecraft.getInstance().player.getPosition().getZ();
    }

    @OnlyIn(value = Dist.CLIENT)
    private static void updatePlayerPosition() {
        assert Minecraft.getInstance().player != null;
        lastPlayerPos = Minecraft.getInstance().player.getPosition();
    }

    @OnlyIn(value = Dist.CLIENT)
    public static synchronized void requestBlockFinder(boolean force) {
        if (isQuickSeeActive() && (force || playerHasMoved())) // world/player check done by xrayActive()
        {
            updatePlayerPosition(); // since we're about to run, update the last known position
            Region region = new Region(lastPlayerPos, getRadius()); // the region to scan for syncRenderList
            Util.getServerExecutor().execute(new RenderEnqueue(region));
        }
    }

    @OnlyIn(value = Dist.CLIENT)
    public static void toggleQuickSee() {
        Utils_Server.storedList.clear();
        Utils_Client.clientPlayer = Minecraft.getInstance().player;
        if (!quickSee) // enable drawing
        {
            Render.syncRenderList.clear(); // first, clear the buffer
            quickSee = true; // then, enable drawing
            requestBlockFinder(true); // finally, force a refresh

            if (!StoreConfig_Client.general.showOverlay.get() && Minecraft.getInstance().player != null)
                Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("quickstore.toggle.activated"), false);
        } else // disable drawing
        {
            if (!StoreConfig_Client.general.showOverlay.get() && Minecraft.getInstance().player != null)
                Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("quickstore.toggle.deactivated"), false);

            quickSee = false;
        }
    }
}
