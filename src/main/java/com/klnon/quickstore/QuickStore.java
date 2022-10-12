package com.klnon.quickstore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.klnon.quickstore.command.QuickStoreCommand;
import com.klnon.quickstore.model.ContainerInformation;
import com.klnon.quickstore.model.Events;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;


@Mod("quickstore")
@Mod.EventBusSubscriber
public class QuickStore {

    public static PlayerEntity player;

    public static boolean keyIsDown = false;

    public static List<ContainerInformation> nearbyContainers;

    public static Map<String, Integer> storedItems=new HashMap<>();

    @SubscribeEvent
    public void setup() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void onServerStaring(RegisterCommandsEvent event) {
        //注册命令
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        LiteralCommandNode<CommandSource> cmd = dispatcher.register(
                Commands.literal("quickstore")
                    .requires((commandSource) -> commandSource.hasPermissionLevel(0))
                    .executes(QuickStoreCommand.instance)
        );
        dispatcher.register(Commands.literal("qs").redirect(cmd));
        MinecraftForge.EVENT_BUS.register(new Events());
    }
    public QuickStore() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientProxy::setup);
    }
}
