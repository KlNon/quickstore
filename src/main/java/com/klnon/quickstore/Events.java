package com.klnon.quickstore;

import com.klnon.quickstore.QuickStore;
import com.klnon.quickstore.gui.render.Render;
import com.klnon.quickstore.gui.render.RenderEnqueue;
import com.klnon.quickstore.utils.Utils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Utils.MOD_ID, value = Dist.CLIENT)
public class Events {

    @SubscribeEvent
    public static void onWorldRenderLast(RenderWorldLastEvent event) {
        if (Utils.clientPlayer == null || !Utils.isQuickSeeActive())
            return;
        if (Utils.isQuickSeeActive()) {
            Render.renderBlocks(event);
        }
    }


    @SubscribeEvent
    public static void tickEnd(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if(!Utils.storedList.isEmpty())
                Utils.requestBlockFinder(true); //refresh
            Utils.requestBlockFinder(false);
        }
    }

    @SubscribeEvent
    public static void chunkLoad(ChunkEvent.Load event) {
        Utils.requestBlockFinder(true);
    }


    @SubscribeEvent
    public static void placeItem(BlockEvent.EntityPlaceEvent event) {
        Utils.storedList.clear();
        RenderEnqueue.checkBlock(event.getPos(), event.getState(), true);
    }

    @SubscribeEvent
    public static void pickupItem(BlockEvent.BreakEvent event) {
        Utils.storedList.clear();
        RenderEnqueue.checkBlock(event.getPos(), event.getState(), false);
    }
}
