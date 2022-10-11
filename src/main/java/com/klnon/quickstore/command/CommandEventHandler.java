package com.klnon.quickstore.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author KlNon
 * @version 1.0
 * @className CommandEventHandler
 * @description
 * @date 2022/10/11 0:56
 **/

@Mod.EventBusSubscriber
public class CommandEventHandler {
    @SubscribeEvent
    public static void onServerStaring(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        LiteralCommandNode<CommandSource> cmd = dispatcher.register(
                Commands.literal("quickstore").then(
                        Commands.literal("test")
                                .requires((commandSource) -> commandSource.hasPermissionLevel(0))
                                .executes(TestCommand.instance)
                )
        );
        dispatcher.register(Commands.literal("bs").redirect(cmd));
    }

}

