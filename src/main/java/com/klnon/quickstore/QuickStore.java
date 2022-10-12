package com.klnon.quickstore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.klnon.quickstore.command.QuickStoreCommand;
import com.klnon.quickstore.config.Configuration;
import com.klnon.quickstore.utils.model.ContainerInformation;
import com.klnon.quickstore.gui.GuiMethods;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


//@Mod(modid = "quickstore", name = "QuickStore", version = "1.4", acceptedMinecraftVersions = "[1.12.2]", guiFactory = "com.ekincan.quickstore.config.ConfigGuiFactory")
@Mod("quickstore")
@Mod.EventBusSubscriber
public class QuickStore {

    private static final Logger LOGGER = LogManager.getLogger();

    public static PlayerEntity player;

    public static boolean keyIsDown = false;

    public static List<Item> currentItems;

    public static List<ContainerInformation> nearbyContainers;

    public static Map<String, Integer> storedItems=new HashMap<>();




//    @SidedProxy(clientSide = "com.klnon.quickstore.proxy.ProxyClient", serverSide = "com.klnon.quickstore.proxy.ProxyServer")
//    public static Proxy proxy;

    @SubscribeEvent
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
        //注册配置文件
        //TODO 按键注册可能要删除
//        MinecraftForge.EVENT_BUS.register(proxy);
//        proxy.preInit();
    }



    // You can use SubscribeEvent and let the Event Bus discover methods to call

    //TODO 注册指令
    @SubscribeEvent
    public static void onServerStaring(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        LiteralCommandNode<CommandSource> cmd = dispatcher.register(
                Commands.literal("quickstore")
                    .requires((commandSource) -> commandSource.hasPermissionLevel(0))
                    .executes(QuickStoreCommand.instance)
        );
        dispatcher.register(Commands.literal("qs").redirect(cmd));
        MinecraftForge.EVENT_BUS.register(new GuiMethods());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.SPEC);
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }



//    @SubscribeEvent
//    public void serverLoad(RegisterCommandsEvent event) {
//        proxy.serverLoad();
//        event.registerServerCommand(new QuickStoreCommand());
//    }
}
