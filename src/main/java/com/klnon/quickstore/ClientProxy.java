package com.klnon.quickstore;

import com.klnon.quickstore.config.StoreConfig_Client;
import com.klnon.quickstore.gui.model.GameItemStore;
import com.klnon.quickstore.gui.model.ItemData;
import com.klnon.quickstore.gui.model.ItemStore;
import com.klnon.quickstore.gui.model.JsonStore;
import com.klnon.quickstore.keybinding.KeyBindings;
import com.klnon.quickstore.utils.Utils_Client;
import com.klnon.quickstore.utils.Utils_Server;
import com.klnon.quickstore.model.BlockData;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ClientProxy {

    public static Logger logger = LogManager.getLogger();
    public static JsonStore itemStore = new JsonStore();
    public static GameItemStore gameItemStore = new GameItemStore();

    public static void setup() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(ClientProxy::onSetup);
        eventBus.addListener(ClientProxy::onLoadComplete);

        MinecraftForge.EVENT_BUS.register(KeyBindings.class);
    }

    private static void onSetup(final FMLCommonSetupEvent event) {
        logger.debug(I18n.format("quickstore.debug.init"));
        KeyBindings.setup();
        //-----------------------------------------------------------------------Gui
        //TODO Gui 可能要修改
        List<ItemData.SerializableItemData> items = ClientProxy.itemStore.read();
        if( items.isEmpty() )
            return;

        ArrayList<ItemData> map = ItemStore.getFromSimpleItemList(items);
        Utils_Client.getItemStore().setStore(map);
        //-----------------------------------------------------------------------Gui

        ItemStack itemStack = new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:chest")),1);
        BlockData data=new BlockData("箱子", "minecraft:chest",StoreConfig_Client.general.BLUE.get(),itemStack,true,11);
        Utils_Client.setBlockData(data);
    }

    private static void onLoadComplete(FMLLoadCompleteEvent event)
    {
        ClientProxy.gameItemStore.populate();
    }
}
