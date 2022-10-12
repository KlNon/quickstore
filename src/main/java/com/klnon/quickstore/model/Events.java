package com.klnon.quickstore.model;

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
        if (QuickStore.player == null || QuickStore.nearbyContainers == null || QuickStore.nearbyContainers.size() == 0)
            return;
        if (Utils.isQuickSeeActive()) {
            Render.renderBlocks(event);
        }
    }


    @SubscribeEvent
    public static void tickEnd( TickEvent.ClientTickEvent event ) {
        if ( event.phase == TickEvent.Phase.END ) {
            Utils.requestBlockFinder( false );
        }
    }

    @SubscribeEvent
    public static void chunkLoad( ChunkEvent.Load event )
    {
        Utils.requestBlockFinder( true );
    }


    @SubscribeEvent
    public static void placeItem( BlockEvent.EntityPlaceEvent event ) {
        RenderEnqueue.checkBlock( event.getPos(), event.getState(), true);
    }

    @SubscribeEvent
    public static void pickupItem( BlockEvent.BreakEvent event ) {
        RenderEnqueue.checkBlock( event.getPos(), event.getState(), false);
    }

    @SubscribeEvent
    public void onTickEvent(TickEvent.PlayerTickEvent event) {
        if (!Utils.isKeyPressed() && QuickStore.keyIsDown) {
            QuickStore.player = Utils.getPlayer();
            QuickStore.nearbyContainers = Utils.getNearbyContainers(QuickStore.player);
            Utils.storeIntoChests();
            QuickStore.keyIsDown = false;
        }
    }
}
