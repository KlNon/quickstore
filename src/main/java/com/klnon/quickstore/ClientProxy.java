package com.klnon.quickstore;

import com.klnon.quickstore.config.StoreConfig;
import com.klnon.quickstore.keybinding.KeyBindings;
import com.klnon.quickstore.networking.Networking;
import com.klnon.quickstore.utils.Utils;
import com.klnon.quickstore.model.BlockData;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientProxy {

    public static Logger logger = LogManager.getLogger();

    public static void setup() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(ClientProxy::onSetup);

        MinecraftForge.EVENT_BUS.register(KeyBindings.class);
    }

    private static void onSetup(final FMLCommonSetupEvent event) {
        logger.debug(I18n.format("quickstore.debug.init"));
        KeyBindings.setup();

        ItemStack itemStack = new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft:chest")),1);
        BlockData data=new BlockData("箱子", "minecraft:chest",42495,itemStack,true,11);
        Utils.setBlockData(data);
    }

}
