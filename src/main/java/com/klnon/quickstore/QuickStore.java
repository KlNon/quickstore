package com.klnon.quickstore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.klnon.quickstore.command.QuickStoreCommand;
import com.klnon.quickstore.command.SearchCommand;
import com.klnon.quickstore.config.StoreConfig_Client;
import com.klnon.quickstore.config.StoreConfig_Server;
import com.klnon.quickstore.model.ContainerInformation;
import com.klnon.quickstore.model.ItemInfo;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod("quickstore")
@Mod.EventBusSubscriber
public class QuickStore {
    public static final String MOD_ID = "quickstore";
    public static final String PREFIX_GUI = String.format("%s:textures/gui/", MOD_ID);

    public static List<ContainerInformation> nearbyContainers;

    public static Map<String, ItemInfo> storedItems=new HashMap<>();



    public static Logger logger = LogManager.getLogger();


    @SubscribeEvent
    public void setup() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public QuickStore() {
        final ModLoadingContext ctx = ModLoadingContext.get();

        ctx.registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        //注册配置文件
        if(FMLEnvironment.dist.isClient())
            ctx.registerConfig(ModConfig.Type.CLIENT, StoreConfig_Client.SPEC);
        ctx.registerConfig(ModConfig.Type.SERVER, StoreConfig_Server.SPEC);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientProxy::setup);

    }

    @SubscribeEvent
    public static void onServerStaring(RegisterCommandsEvent event) {
        //注册命令
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        dispatcher.register(
                Commands.literal("quickstore")
                        .executes(QuickStoreCommand.instance)
        );

        dispatcher.register(
                Commands.literal("quicksearch")
                        .executes(SearchCommand.instance)
        );

        MinecraftForge.EVENT_BUS.register(new Events());
    }

}
