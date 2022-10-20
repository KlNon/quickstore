package com.klnon.quickstore;

import com.klnon.quickstore.gui.render.Render;
import com.klnon.quickstore.gui.render.RenderEnqueue;
import com.klnon.quickstore.utils.Utils_Client;
import com.klnon.quickstore.utils.Utils_Server;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = QuickStore.MOD_ID, value = Dist.CLIENT)
public class Events {

    @SubscribeEvent
    public static void onWorldRenderLast(RenderWorldLastEvent event) {
        if (Utils_Client.clientPlayer == null || !Utils_Client.isQuickSeeActive())
            return;
        if (Utils_Client.isQuickSeeActive()) {
            Render.renderBlocks(event);
        }
    }


    @SubscribeEvent
    public static void tickEnd(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if(!Utils_Server.storedList.isEmpty())
                Utils_Client.requestBlockFinder(true); //refresh
            if(!Utils_Server.searchList.isEmpty())
                Utils_Client.requestBlockFinder(true); //refresh
            Utils_Client.requestBlockFinder(false);
        }
    }

    @SubscribeEvent
    public static void chunkLoad(ChunkEvent.Load event) {
        Utils_Client.requestBlockFinder(true);
    }


    @SubscribeEvent
    public static void placeItem(BlockEvent.EntityPlaceEvent event) {
        Utils_Server.storedList.clear();
        Utils_Server.searchList.clear();
        Utils_Server.getItems().clear();
        RenderEnqueue.checkBlock(event.getPos(), event.getState(), true);
    }

    @SubscribeEvent
    public static void pickupItem(BlockEvent.BreakEvent event) {
        Utils_Server.storedList.clear();
        Utils_Server.searchList.clear();
        Utils_Server.getItems().clear();
        RenderEnqueue.checkBlock(event.getPos(), event.getState(), false);
    }
}
